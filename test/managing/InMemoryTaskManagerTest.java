package managing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;
import taskmodels.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    Managers managers = new Managers();
    InMemoryTaskManager taskManager;

    @BeforeEach
    void createManager() {
        taskManager = managers.createTaskManager();
    }

    @Test
    void returnNullIfSubtaskIsSelfEpic() {
        Subtask task = new Subtask(1, "Subtask", "Subtask", 1);
        assertNull(taskManager.createSubtask(task));
    }

    @Test
    void addAndFindDifferentTypeTasks() {
        Task task = new Task(1, "Task", "Tusk");
        Epic epic = new Epic(2, "Epic", "Epic task");
        Subtask subtask = new Subtask(3, "Sub", "Sub", 2);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        assertEquals(taskManager.findTaskById(1), task);
        assertEquals(taskManager.findEpicById(2), epic);
        assertEquals(taskManager.findSubtaskById(3), subtask);
    }

    @Test
    void taskInManagerEqualsInitialTask() {
        Task task = new Task(1, "Task", "Task description");
        taskManager.createTask(task);
        Task managerTask = taskManager.findTaskById(1);
        assertEquals(task, managerTask);
    }

    @Test
    void historyContainsOldTaskVersion() {
        Task task = new Task(1, "Task", "Task description");
        taskManager.createTask(task);
        taskManager.findTaskById(1);
        task = new Task(1, "Task", "New task description");
        taskManager.updateTask(task);

        ArrayList<Task> history = taskManager.getHistory();
        Task oldTask = history.getFirst();
        Task newTask = taskManager.findTaskById(1);

        assertNotEquals(oldTask, newTask);
    }

    @Test
    void ifNoSubtasksEpicStatusNew() {
        Epic task = new Epic(1, "Task", "Task description");
        taskManager.createEpic(task);
        Epic epicInManager = taskManager.findEpicById(1);
        assertEquals(TaskStatus.NEW, epicInManager.getStatus());
    }

    @Test
    void ifAllSubtasksNewEpicStatusNew() {
        Epic task = new Epic(1, "Task", "Task description");
        taskManager.createEpic(task);
        Subtask subtask1 = new Subtask(2, "Subtask", "Subtask description", 1);
        Subtask subtask2 = new Subtask(3, "Subtask", "Subtask description", 1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        Epic epicInManager = taskManager.findEpicById(1);
        assertEquals(TaskStatus.NEW, epicInManager.getStatus());
    }

    @Test
    void ifAllSubtasksDoneEpicStatusDone() {
        Epic task = new Epic(1, "Task", "Task description");
        taskManager.createEpic(task);
        Subtask subtask1 = new Subtask(2, "Subtask", "Subtask description", 1);
        Subtask subtask2 = new Subtask(3, "Subtask", "Subtask description", 1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        Epic epicInManager = taskManager.findEpicById(1);
        assertEquals(TaskStatus.DONE, epicInManager.getStatus());
    }

    @Test
    void ifAllSubtasksDifferentEpicStatusInProgress() {
        Epic task = new Epic(1, "Task", "Task description");
        taskManager.createEpic(task);
        Subtask subtask1 = new Subtask(2, "Subtask", "Subtask description", 1);
        Subtask subtask2 = new Subtask(3, "Subtask", "Subtask description", 1);
        Subtask subtask3 = new Subtask(4, "Subtask", "Subtask description", 1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        Epic epicInManager = taskManager.findEpicById(1);
        assertEquals(TaskStatus.IN_PROGRESS, epicInManager.getStatus());
    }

    @Test
    void ifAllSubtasksDeletedEpicStatusDone() {
        Epic task = new Epic(1, "Task", "Task description");
        taskManager.createEpic(task);
        Subtask subtask1 = new Subtask(2, "Subtask", "Subtask description", 1);
        Subtask subtask2 = new Subtask(3, "Subtask", "Subtask description", 1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        taskManager.deleteAllSubtasks();

        Epic epicInManager = taskManager.findEpicById(1);
        assertEquals(TaskStatus.NEW, epicInManager.getStatus());
    }

    @Test
    void deletingTask() {
        Task task = new Task(1, "Task", "Task description");
        taskManager.createTask(task);
        taskManager.deleteTask(1);
        assertNull(taskManager.findTaskById(1));
    }

    @Test
    void deletingAllTasks() {
        Task task1 = new Task(1, "Task", "Task description");
        Task task2 = new Task(2, "Task", "Task description");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.deleteAllTasks();
        ArrayList<Epic> empty = new ArrayList<>();
        assertEquals(taskManager.getAllTasks(), empty);
    }

    @Test
    void gettingAllTasks() {
        Task task1 = new Task(1, "Task", "Task description");
        Task task2 = new Task(2, "Task", "Task description");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        ArrayList<Task> taskArray = new ArrayList<>();
        taskArray.add(task1);
        taskArray.add(task2);
        assertEquals(taskManager.getAllTasks(), taskArray);
    }

    @Test
    void deletingEpic() {
        Epic task = new Epic(1, "Task", "Task description");
        taskManager.createEpic(task);
        taskManager.deleteEpic(1);
        assertNull(taskManager.findEpicById(1));
    }

    @Test
    void deletingAllEpics() {
        Epic task1 = new Epic(1, "Task", "Task description");
        Epic task2 = new Epic(2, "Task", "Task description");
        taskManager.createEpic(task1);
        taskManager.createEpic(task2);
        taskManager.deleteAllEpics();
        ArrayList<Epic> empty = new ArrayList<>();
        assertEquals(taskManager.getAllEpic(), empty);
    }

    @Test
    void gettingAllEpics() {
        Epic task1 = new Epic(1, "Task", "Task description");
        Epic task2 = new Epic(2, "Task", "Task description");
        taskManager.createEpic(task1);
        taskManager.createEpic(task2);
        ArrayList<Epic> taskArray = new ArrayList<>();
        taskArray.add(task1);
        taskArray.add(task2);
        assertEquals(taskManager.getAllEpic(), taskArray);
    }

    @Test
    void deletingSubtask() {
        Epic epic = new Epic(1, "Task", "Task description");
        Subtask subtask = new Subtask(2, "Subtask", "Subtask description", 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.deleteSubtask(subtask);
        assertNull(taskManager.findSubtaskById(2));
    }

    @Test
    void deletingAllSubtasks() {
        Epic epic = new Epic(1, "Task", "Task description");
        Subtask subtask1 = new Subtask(2, "Subtask", "Subtask description", 1);
        Subtask subtask2 = new Subtask(3, "Subtask", "Subtask description", 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.deleteAllSubtasks();
        ArrayList<Subtask> empty = new ArrayList<>();
        assertEquals(taskManager.getAllSubtasks(), empty);
    }

    @Test
    void gettingAllSubtasks() {
        Epic epic = new Epic(1, "Task", "Task description");
        Subtask subtask1 = new Subtask(2, "Subtask", "Subtask description", 1);
        Subtask subtask2 = new Subtask(3, "Subtask", "Subtask description", 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        ArrayList<Subtask> taskArray = new ArrayList<>();
        taskArray.add(subtask1);
        taskArray.add(subtask2);
        assertEquals(taskManager.getAllSubtasks(), taskArray);
    }

    @Test
    void gettingEpicSubtasks() {
        Epic epic = new Epic(1, "Task", "Task description");
        Subtask subtask1 = new Subtask(2, "Subtask", "Subtask description", 1);
        Subtask subtask2 = new Subtask(3, "Subtask", "Subtask description", 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        ArrayList<Subtask> taskArray = new ArrayList<>();
        taskArray.add(subtask1);
        taskArray.add(subtask2);
        assertEquals(taskManager.getEpicSubtasks(epic), taskArray);
    }

    @Test
    void deletingEpicSubtasks() {
        Epic epic = new Epic(1, "Task", "Task description");
        Subtask subtask1 = new Subtask(2, "Subtask", "Subtask description", 1);
        Subtask subtask2 = new Subtask(3, "Subtask", "Subtask description", 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.deleteEpicSubtasks(epic);
        ArrayList<Subtask> taskArray = new ArrayList<>();
        assertEquals(taskManager.getEpicSubtasks(epic), taskArray);
    }

    @Test
    void epicUpdate() {
        Epic epic = new Epic(1, "Task", "Task description");
        taskManager.createEpic(epic);
        Epic newEpic = new Epic(1, "Task", "New task description");
        taskManager.updateEpic(newEpic);
        assertEquals(newEpic, taskManager.findEpicById(1));
    }
}