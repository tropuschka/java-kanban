package managing;

import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;
import taskmodels.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int taskAmount = 0;
    private HashMap<Integer, Task> commonTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int generateId() {
        return ++taskAmount;
    }

    public Task createTask(Task task) {
        Integer taskId = generateId();
        task.setId(taskId);
        commonTasks.put(task.getId(), task);
        return task;
    }

    public Task updateTask(Task task) {
        Task targetTask = commonTasks.get(task.getId());
        if (targetTask != null) {
            targetTask.setName(task.getName());
            targetTask.setDetails(task.getDetails());
            targetTask.setStatus(task.getStatus());
            return targetTask;
        } else {
            return null;
        }
    }

    public Task deleteTask(Integer id) {
        return commonTasks.remove(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(commonTasks.values());
    }

    public HashMap<Integer, Task> deleteAllTasks() {
        commonTasks.clear();
        return commonTasks;
    }

    public Task findTaskById (Integer id) {
        return commonTasks.get(id);
    }

    public Epic createEpic(Epic epic) {
        Integer epicId = generateId();
        epic.setId(epicId);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic updateEpic(Epic epic) {
        Epic targetEpic = epics.get(epic.getId());
        if (targetEpic != null) {
            targetEpic.setName(epic.getName());
            targetEpic.setDetails(epic.getDetails());
            targetEpic.setSubtasks(epic.getSubtasks());
            for (Integer subtaskId : targetEpic.getSubtasks()) {
                Subtask subtask = subtasks.get(subtaskId);
                subtask.setEpicId(epic.getId());
            }
            return targetEpic;
        } else {
            return null;
        }
    }

    private Epic updateEpicStatus(Epic epic) {
        int counterNew = 0;
        int counterInProgress = 0;
        int counterDone = 0;
        for (Integer subtaskId : epic.getSubtasks()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() == TaskStatus.NEW) counterNew++;
            if (subtask.getStatus() == TaskStatus.IN_PROGRESS) counterInProgress++;
            if (subtask.getStatus() == TaskStatus.DONE) counterDone++;
        }
        if (counterDone == 0 && counterInProgress == 0) epic.setStatus(TaskStatus.NEW);
        else if (counterNew == 0 && counterInProgress == 0 && counterDone != 0) epic.setStatus(TaskStatus.DONE);
        else epic.setStatus(TaskStatus.IN_PROGRESS);
        return epic;
    }

    public Epic deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        for (Integer subtask : epic.getSubtasks()) {
            subtasks.remove(subtask);
        }
        return epics.remove(id);
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public HashMap<Integer, Epic> deleteAllEpics() {
        subtasks.clear();
        epics.clear();
        return epics;
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
        Integer subtaskId = generateId();
        subtask.setId(subtaskId);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
        return subtask;
    }

    public Subtask updateSubtask(Subtask subtask) {
        Subtask targetSubtask = subtasks.get(subtask.getId());
        if (targetSubtask != null) {
            targetSubtask.setName(subtask.getName());
            targetSubtask.setDetails(subtask.getDetails());
            targetSubtask.setStatus(subtask.getStatus());
            targetSubtask.setEpicId(subtask.getEpicId());
            Epic epic = epics.get(targetSubtask.getEpicId());
            epic.addSubtask(targetSubtask.getId());
            updateEpicStatus(epic);
            return subtask;
        } else {
            return null;
        }
    }

    public Subtask deleteSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtask(subtask.getId());
        updateEpicStatus(epic);
        return subtask;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public HashMap<Integer, Subtask> deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearAllSubs();
            epic.setStatus(TaskStatus.NEW); // Поставила здесь просто всем эпикам статус NEW, тк теперь у всех нет подзадач
        }
        return subtasks;
    }

    public Subtask findSubtaskById(Integer id) {
        return subtasks.get(id);
    }
}
