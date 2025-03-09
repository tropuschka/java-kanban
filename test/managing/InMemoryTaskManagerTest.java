package managing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;
import taskmodels.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;
    Task task;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Task taskWithDeadline;
    Epic epicWithDeadline;
    Subtask subtask1WithDeadline;
    Subtask subtask2WithDeadline;
    Subtask subtask3WithDeadline;

    @BeforeEach
    void createManager() {
        taskManager = Managers.createTaskManager();
        task = new Task(1, "Task", "Task");
        epic = new Epic(2, "Epic", "Epic description");
        subtask1 = new Subtask(3, "Subtask", "Subtask description", 2);
        subtask2 = new Subtask(4, "Subtask", "Subtask description", 2);
        subtask3 = new Subtask(5, "Subtask", "Subtask description", 2);
        taskWithDeadline = new Task(6, "Task", "Task", "01.01.2000 07:00", "PT15M");
        epicWithDeadline = new Epic(7, "Epic", "Epic description");
        subtask1WithDeadline = new Subtask(8, "Subtask", "Subtask description", 7, "02.01.2000 07:00", "PT15M");
        subtask2WithDeadline = new Subtask(9, "Subtask", "Subtask description", 7, "05.01.2000 07:00", "PT15M");
        subtask3WithDeadline = new Subtask(10, "Subtask", "Subtask description", 7, "04.01.2000 07:00", "PT15M");
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createTask(taskWithDeadline);
        taskManager.createEpic(epicWithDeadline);
        taskManager.createSubtask(subtask1WithDeadline);
        taskManager.createSubtask(subtask2WithDeadline);
        taskManager.createSubtask(subtask3WithDeadline);
    }

    @Test
    void returnNullIfSubtaskIsSelfEpic() {
        Subtask selfEpicSubtask = new Subtask(11, "Subtask", "Subtask", 11);
        assertNull(taskManager.createSubtask(selfEpicSubtask));
    }

    @Test
    void addAndFindDifferentTypeTasks() {
        assertEquals(taskManager.findTaskById(1), task);
        assertEquals(taskManager.findEpicById(2), epic);
        assertEquals(taskManager.findSubtaskById(3), subtask1);
    }

    @Test
    void taskInManagerEqualsInitialTask() {
        assertEquals(task, taskManager.findTaskById(1));
    }

    @Test
    void historyContainsOldTaskVersion() {
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
        Epic epicInManager = taskManager.findEpicById(2);
        assertEquals(TaskStatus.NEW, epicInManager.getStatus());
    }

    @Test
    void ifAllSubtasksNewEpicStatusNew() {
        Epic epicInManager = taskManager.findEpicById(2);
        assertEquals(TaskStatus.NEW, epicInManager.getStatus());
    }

    @Test
    void ifAllSubtasksDoneEpicStatusDone() {
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        Epic epicInManager = taskManager.findEpicById(2);
        assertEquals(TaskStatus.DONE, epicInManager.getStatus());
    }

    @Test
    void ifAllSubtasksDifferentEpicStatusInProgress() {
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        Epic epicInManager = taskManager.findEpicById(2);
        assertEquals(TaskStatus.IN_PROGRESS, epicInManager.getStatus());
    }

    @Test
    void ifAllSubtasksDeletedEpicStatusDone() {
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        taskManager.deleteAllSubtasks();

        Epic epicInManager = taskManager.findEpicById(2);
        assertEquals(TaskStatus.NEW, epicInManager.getStatus());
    }

    @Test
    void deletingTask() {
        taskManager.deleteTask(1);
        assertNull(taskManager.findTaskById(1));
    }

    @Test
    void deletingAllTasks() {
        Task task2 = new Task(11, "Task", "Task description");
        taskManager.createTask(task2);
        taskManager.deleteAllTasks();
        ArrayList<Epic> empty = new ArrayList<>();
        assertEquals(taskManager.getAllTasks(), empty);
    }

    @Test
    void gettingAllTasks() {
        Task task2 = new Task(6, "Task", "Task description");
        taskManager.createTask(task2);
        ArrayList<Task> taskArray = new ArrayList<>();
        taskArray.add(task);
        taskArray.add(taskWithDeadline);
        taskArray.add(task2);
        assertEquals(taskManager.getAllTasks(), taskArray);
    }

    @Test
    void deletingEpic() {
        taskManager.deleteEpic(2);
        assertNull(taskManager.findEpicById(2));
    }

    @Test
    void deletingAllEpics() {
        Epic epic2 = new Epic(11, "Task", "Task description");
        taskManager.createEpic(epic2);
        taskManager.deleteAllEpics();
        ArrayList<Epic> empty = new ArrayList<>();
        assertEquals(taskManager.getAllEpic(), empty);
    }

    @Test
    void gettingAllEpics() {
        Epic epic2 = new Epic(11, "Task", "Task description");
        taskManager.createEpic(epic2);
        ArrayList<Epic> taskArray = new ArrayList<>();
        taskArray.add(epic);
        taskArray.add(epicWithDeadline);
        taskArray.add(epic2);
        assertEquals(taskManager.getAllEpic(), taskArray);
    }

    @Test
    void deletingSubtask() {
        taskManager.deleteSubtask(subtask1);
        assertNull(taskManager.findSubtaskById(3));
    }

    @Test
    void deletingAllSubtasks() {
        taskManager.deleteAllSubtasks();
        ArrayList<Subtask> empty = new ArrayList<>();
        assertEquals(taskManager.getAllSubtasks(), empty);
    }

    @Test
    void gettingAllSubtasks() {
        ArrayList<Subtask> taskArray = new ArrayList<>();
        taskArray.add(subtask1);
        taskArray.add(subtask2);
        taskArray.add(subtask3);
        taskArray.add(subtask1WithDeadline);
        taskArray.add(subtask2WithDeadline);
        taskArray.add(subtask3WithDeadline);
        assertEquals(taskManager.getAllSubtasks(), taskArray);
    }

    @Test
    void gettingEpicSubtasks() {
        ArrayList<Subtask> taskArray = new ArrayList<>();
        taskArray.add(subtask1);
        taskArray.add(subtask2);
        taskArray.add(subtask3);
        assertEquals(taskManager.getEpicSubtasks(epic), taskArray);
    }

    @Test
    void deletingEpicSubtasks() {
        taskManager.deleteEpicSubtasks(epic);
        ArrayList<Subtask> taskArray = new ArrayList<>();
        assertEquals(taskManager.getEpicSubtasks(epic), taskArray);
    }

    @Test
    void epicUpdate() {
        Epic newEpic = new Epic(2, "Epic", "New task description");
        taskManager.updateEpic(newEpic);
        assertEquals(new Epic(2, "Epic", "New task description"), taskManager.findEpicById(2));
    }

    @Test
    void sortTasks() {
        ArrayList<Task> sortedTasks = new ArrayList<>();
        sortedTasks.add(taskWithDeadline);
        sortedTasks.add(subtask1WithDeadline);
        sortedTasks.add(subtask3WithDeadline);
        sortedTasks.add(subtask2WithDeadline);
        assertEquals(sortedTasks, taskManager.getPrioritizedTasks());
    }

    @Test
    void addOverlapSame() {
        Task overlap = taskWithDeadline = new Task(11, "Task", "Task", "01.01.2000 07:00", "PT15M");
        taskManager.createTask(overlap);
        assertNull(taskManager.findTaskById(11));
    }

    @Test
    void addOverlapBefore() {
        Task overlap = taskWithDeadline = new Task(11, "Task", "Task", "01.01.2000 06:55", "PT15M");
        taskManager.createTask(overlap);
        assertNull(taskManager.findTaskById(11));
    }

    @Test
    void addOverlapAfter() {
        Task overlap = taskWithDeadline = new Task(11, "Task", "Task", "01.01.2000 07:05", "PT15M");
        taskManager.createTask(overlap);
        assertNull(taskManager.findTaskById(11));
    }

    @Test
    void addOverlapIn() {
        Task overlap = taskWithDeadline = new Task(11, "Task", "Task", "01.01.2000 07:05", "PT5M");
        taskManager.createTask(overlap);
        assertNull(taskManager.findTaskById(11));
    }
}