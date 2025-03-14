package managing;

import taskmodels.*;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int taskAmount = 0;
    protected HashMap<Integer, Task> commonTasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager history = Managers.createHistoryManager();
    protected TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private int generateId() {
        return ++taskAmount;
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            int counterNew = 0;
            int counterDone = 0;
            for (Integer subtaskId : epic.getSubtasks()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask.getStatus() == TaskStatus.NEW) counterNew++;
                if (subtask.getStatus() == TaskStatus.DONE) counterDone++;
            }
            if (counterDone == epic.getSubtasks().size()) epic.setStatus(TaskStatus.DONE);
            else if (counterNew == epic.getSubtasks().size()) epic.setStatus(TaskStatus.NEW);
            else epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private boolean checkTaskOverlap(Task task) {
        List<Task> tasks = new ArrayList<>(sortedTasks);
        boolean overlap = tasks.stream()
                .noneMatch(checkTask -> task.getStartTime().isBefore(checkTask.getEndTime())
                        && task.getEndTime().isAfter(checkTask.getStartTime()));
        return overlap;
    }

    @Override
    public Task createTask(Task task) {
        if (task.getStartTime() == null || checkTaskOverlap(task)) {
            Integer taskId = generateId();
            task.setId(taskId);
            commonTasks.put(task.getId(), task);
            if (task.getStartTime() != null) sortedTasks.add(task);
        }
        return task;
    }

    @Override
    public void updateTask(Task task) {
        Task targetTask = commonTasks.get(task.getId());
        if (targetTask != null && (task.getStartTime() == null || checkTaskOverlap(task))) {
            commonTasks.put(targetTask.getId(), task);
            if (task.getStartTime() != null) {
                sortedTasks.remove(targetTask);
                sortedTasks.add(task);
            }
        }
    }

    @Override
    public Task deleteTask(Integer id) {
        history.remove(id);
        Task task = commonTasks.get(id);
        if (task.getStartTime() != null) sortedTasks.remove(task);
        return commonTasks.remove(id);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(commonTasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Integer task : commonTasks.keySet()) {
            history.remove(task);
            Task taskItself = commonTasks.get(task);
            if (taskItself.getStartTime() != null) sortedTasks.remove(taskItself);
        }
        commonTasks.clear();
    }

    @Override
    public Task findTaskById(Integer id) {
        Task targetTask = commonTasks.get(id);
        history.add(targetTask);
        return targetTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Integer epicId = generateId();
        epic.setId(epicId);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic targetEpic = epics.get(epic.getId());
        if (targetEpic != null) {
            epics.put(targetEpic.getId(), epic);
        }
    }

    @Override
    public void deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        history.remove(epic.getId());
        deleteEpicSubtasks(epic);
        epics.remove(id);
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (Integer epic : epics.keySet()) {
            history.remove(epic);
        }
        sortedTasks.removeIf(task -> task.getType().equals(TaskType.SUBTASK));
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Epic findEpicById(Integer id) {
        Epic targetEpic = epics.get(id);
        history.add(targetEpic);
        return targetEpic;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        ArrayList<Integer> epicSubtasksId = epic.getSubtasks();
        for (Integer id : epicSubtasksId) {
            epicSubtasks.add(subtasks.get(id));
        }
        return epicSubtasks;
    }

    @Override
    public void deleteEpicSubtasks(Epic epic) {
        for (Integer id : epic.getSubtasks()) {
            if (subtasks.get(id).getStartTime() != null) sortedTasks.remove(subtasks.get(id));
            history.remove(id);
            subtasks.remove(id);
        }
        epic.clearAllSubs();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask.getId() == subtask.getEpicId()) return null;
        if (subtask.getStartTime() == null || checkTaskOverlap(subtask)) {
            Integer subtaskId = generateId();
            subtask.setId(subtaskId);
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic == null) updateEpicStatus(epic);
            epic.addSubtask(subtask.getId());
            if ((epic.getStartTime() == null || (epic.getStartTime() != null
                    && epic.getStartTime().isAfter(subtask.getStartTime()))) && subtask.getStartTime() != null) {
                epic.updateStartTime(subtask.getStartTime());
            }
            if (subtask.getDuration() != null) epic.addDuration(subtask.getDuration());
            if (subtask.getStartTime() != null) sortedTasks.add((Task) subtask);
        }
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask targetSubtask = subtasks.get(subtask.getId());
        if (targetSubtask != null && (subtask.getStartTime() == null || checkTaskOverlap(subtask))) {
            subtasks.remove(targetSubtask.getId());
            subtasks.put(targetSubtask.getId(), subtask);
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            Epic newEpic = new Epic(epic.getId(), epic.getName(), epic.getDetails());
            for (Integer subtaskID : epic.getSubtasks()) {
                newEpic.addSubtask(subtaskID);
            }
            updateEpicStatus(newEpic);
            if (subtask.getDuration() != null) {
                Duration newDuration = epic.getDuration().plus(subtask.getDuration());
                newEpic.setDuration(newDuration);
            }
            if (subtask.getDuration() != null) {
                if ((epic.getStartTime() == null || (epic.getStartTime() != null
                        && epic.getStartTime().isAfter(subtask.getStartTime())))) {
                    newEpic.setStartTime(subtask.getStartTime());
                } else {
                    newEpic.setStartTime(epic.getStartTime());
                }
                sortedTasks.remove(targetSubtask);
                sortedTasks.add(subtask);
            }
            updateEpic(newEpic);
        }
    }

    @Override
    public void deleteSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtask(subtask.getId());
        if (subtask.getDuration() != null) epic.decreaseDuration(subtask.getDuration());
        updateEpicStatus(epic);
        history.remove(subtask.getId());
        if (subtask.getStartTime() != null) sortedTasks.remove(subtask);
        subtasks.remove(subtask.getId());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer subtask : subtasks.keySet()) {
            history.remove(subtask);
            if (subtasks.get(subtask).getStartTime() != null) sortedTasks.remove(subtasks.get(subtask));
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearAllSubs();
            epic.setStatus(TaskStatus.NEW); // Поставила здесь просто всем эпикам статус NEW, тк теперь у всех нет подзадач
        }
    }

    @Override
    public Subtask findSubtaskById(Integer id) {
        Subtask targetSubtask = subtasks.get(id);
        history.add(targetSubtask);
        return targetSubtask;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<Task>(sortedTasks);
    }
}
