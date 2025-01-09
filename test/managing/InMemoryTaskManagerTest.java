package managing;

import org.junit.jupiter.api.Test;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();
    @Test
    void returnNullIfSubtaskIsSelfEpic() {
        Subtask task = new Subtask(1, "Subtask", "Subtask", 1);
        assertNull(manager.createSubtask(task));
    }

    @Test
    void addAndFindDifferentTypeTasks() {
        Task task = new Task(1, "Task", "Tusk");
        Epic epic = new Epic(2, "Epic", "Epic task");
        Subtask subtask = new Subtask(3, "Sub", "Sub", 2);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);

        assertTrue(task.equals(manager.findTaskById(1)) && epic.equals(manager.findEpicById(2)) &&
                subtask.equals(manager.findSubtaskById(3)));
    }

    @Test
    void taskInManagerEqualsInitialTask() {
        Task task = new Task(1, "Task", "Task description");
        manager.createTask(task);
        Task managerTask = manager.findTaskById(1);
        assertTrue(managerTask.getId() == 1 && managerTask.getName().equals("Task") &&
                managerTask.getDetails().equals("Task description"));
    }

    @Test
    void historyContainsOldTaskVersion() {
        Task task = new Task(1, "Task", "Task description");
        manager.createTask(task);
        manager.findTaskById(1);
        task = new Task(1, "Task", "New task description");
        manager.updateTask(task);

        ArrayList<Task> history = manager.getHistory();
        Task oldTask = history.getFirst();
        Task newTask = manager.findTaskById(1);

        assertNotEquals(oldTask, newTask);
    }
}