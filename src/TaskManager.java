import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskAmount = 0;
    private int epicAmount = 0;
    private int subtaskAmount = 0;
    private HashMap<Integer, Task> commonTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int generateTaskId() {
        return ++taskAmount;
    }
    private int generateEpicId() {
        return ++epicAmount;
    }
    private int generateSubtaskId() {
        return ++subtaskAmount;
    }

    public Task createTask(Task task) {
        Integer taskId = generateTaskId();
        task.setId(taskId);
        commonTasks.put(task.getId(), task);
        return task;
    }

    public Task updateTask(Task task) {
        if (commonTasks.containsKey(task.getId())) {
            Task targetTask = commonTasks.get(task.getId());
            targetTask.setName(task.getName());
            targetTask.setDetails(task.getDetails());
            targetTask.setStatus(task.getStatus());
            return targetTask;
        } else {
            return createTask(task);
        }
    }

    public Task deleteTask(Integer id) {
        return commonTasks.remove(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(commonTasks.values());
    }

    public Task findTaskById (Integer id) {
        return commonTasks.get(id);
    }

    public Epic createEpic(Epic epic) {
        Integer epicId = generateEpicId();
        epic.setId(epicId);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic targetEpic = epics.get(epic.getId());
            targetEpic.setName(epic.getName());
            targetEpic.setDetails(epic.getDetails());
            targetEpic.setStatus(epic.getStatus());
            targetEpic.setSubtasks(epic.getSubtasks());
            for (Integer subtaskId : targetEpic.getSubtasks()) {
                Subtask subtask = subtasks.get(subtaskId);
                subtask.setEpicId(epic.getId());
            }
            return targetEpic;
        } else {
            return createEpic(epic);
        }
    }

    public Epic deleteEpic(Integer id) {
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId().equals(id)) {
                subtasks.remove(subtask.getId());
            }
        }
        return epics.remove(id);
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public Epic findEpicById (Integer id) {
        return epics.get(id);
    }

    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        ArrayList<Integer> epicSubtasksId = epic.getSubtasks();
        for (Integer id : epicSubtasksId) {
            epicSubtasks.add(subtasks.get(id));
        }
        return epicSubtasks;
    }

    public Subtask createSubtask(Subtask subtask) {
        Integer subtaskId = generateSubtaskId();
        subtask.setId(subtaskId);
        subtasks.put(subtask.getId(), subtask);
        return subtask;
    }

    public Subtask updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask targetSubtask = subtasks.get(subtask.getId());
            targetSubtask.setName(subtask.getName());
            targetSubtask.setDetails(subtask.getDetails());
            targetSubtask.setStatus(subtask.getStatus());
            targetSubtask.setEpicId(subtask.getEpicId());
            Epic epic = epics.get(targetSubtask.getEpicId());
            epic.addSubtask(targetSubtask.getId());

            // Обновление статуса эпика
            if ((targetSubtask.getStatus() == TaskStatus.NEW && epic.getStatus() == TaskStatus.DONE) ||
                    targetSubtask.getStatus() == TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else if (targetSubtask.getStatus() == TaskStatus.DONE) {
                boolean areAllSubsDone = true;
                for (Integer subtaskId : epic.getSubtasks()) {
                    Subtask epicSubtask = subtasks.get(subtaskId);
                    if (epicSubtask.getStatus() != TaskStatus.DONE) areAllSubsDone = false;
                }
                if (areAllSubsDone) {
                    epic.setStatus(TaskStatus.DONE);
                } else if (epic.getStatus() == TaskStatus.NEW) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                }
            }

            return subtask;
        } else {
            return createSubtask(subtask);
        }
    }

    public Subtask deleteSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtask(subtask.getId());
        return subtask;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Subtask findSubtaskById(Integer id) {
        return subtasks.get(id);
    }
}
