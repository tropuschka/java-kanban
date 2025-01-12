package managing;

import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;
import taskmodels.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int taskAmount = 0;
    private HashMap<Integer, Task> commonTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager history = Managers.createHistoryManager();

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

    @Override
    public Task createTask(Task task) {
        Integer taskId = generateId();
        task.setId(taskId);
        commonTasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        Task targetTask = commonTasks.get(task.getId());
        if (targetTask != null) {
            targetTask.setName(task.getName());
            targetTask.setDetails(task.getDetails());
            targetTask.setStatus(task.getStatus());
        }
    }

    @Override
    public Task deleteTask(Integer id) {
        return commonTasks.remove(id);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(commonTasks.values());
    }

    @Override
    public void deleteAllTasks() {
        commonTasks.clear();
    }

    @Override
    public Task findTaskById (Integer id) {
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
            targetEpic.setName(epic.getName());
            targetEpic.setDetails(epic.getDetails());
        }
    }

    @Override
    public void deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        for (Integer subtask : epic.getSubtasks()) {
            subtasks.remove(subtask);
        }
        epics.remove(id);
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Epic findEpicById (Integer id) {
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
            subtasks.remove(id);
        }
        epic.clearAllSubs();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask.getId() == subtask.getEpicId()) return null;
        Integer subtaskId = generateId();
        subtask.setId(subtaskId);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) updateEpicStatus(epic);
        epic.addSubtask(subtask.getId());
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask targetSubtask = subtasks.get(subtask.getId());
        if (targetSubtask != null) {
            targetSubtask.setName(subtask.getName());
            targetSubtask.setDetails(subtask.getDetails());
            targetSubtask.setStatus(subtask.getStatus());
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) updateEpicStatus(epic);
    }

    @Override
    public void deleteSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtask(subtask.getId());
        updateEpicStatus(epic);
        subtasks.remove(subtask.getId());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
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
}
