package managing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmodels.Epic;
import taskmodels.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends ManagersTest {
    static InMemoryHistoryManager manager;
    static Task task;

    @BeforeEach
    void historyManagerRefill() {
        manager = new InMemoryHistoryManager();
        for (int i = 0; i < 3; i++) {
            manager.add(new Task(i, "Task", "Task description"));
        }

    }

    @Test
    void returningHistory() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            taskArray.add(new Task(i, "Task", "Task description"));
        }
        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void changingNodesWhenTaskAddedAgain() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            taskArray.add(new Task(i, "Task", "Task description"));
        }
        manager.add(new Task(1, "Task", "Task description"));
        taskArray.remove(1);
        taskArray.add(new Task(1, "Task", "Task description"));

        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void changingNodesWhenTailAddedAgain() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            taskArray.add(new Task(i, "Task", "Task description"));
        }
        manager.add(new Task(2, "Task", "Task description"));
        taskArray.remove(2);
        taskArray.add(new Task(2, "Task", "Task description"));

        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void changingNodesWhenHeadAddedAgain() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            taskArray.add(new Task(i, "Task", "Task description"));
        }
        manager.add(new Task(0, "Task", "Task description"));
        taskArray.remove(0);
        taskArray.add(new Task(0, "Task", "Task description"));

        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void deleteMiddleNode() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            taskArray.add(new Task(i, "Task", "Task description"));
        }
        manager.remove(1);
        taskArray.remove(1);

        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void deleteHead() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            taskArray.add(new Task(i, "Task", "Task description"));
        }
        manager.remove(0);
        taskArray.remove(0);

        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void deleteTail() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            taskArray.add(new Task(i, "Task", "Task description"));
        }
        manager.remove(2);
        taskArray.remove(2);

        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void deleteAll() {
        for (int i = 0; i < 3; i++) manager.remove(i);
        ArrayList<Epic> empty = new ArrayList<>();
        assertEquals(manager.getHistory(), empty);
    }

    @Test
    void deleteFromEmpty() {
        InMemoryHistoryManager manager2 = new InMemoryHistoryManager();
        manager2.remove(1);
        ArrayList<Epic> empty = new ArrayList<>();
        assertEquals(manager2.getHistory(), empty);
    }
}