package managing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import taskmodels.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static InMemoryHistoryManager manager;
    static Task task;

    @BeforeAll
    static void historyManagerCreation() {
        manager = new InMemoryHistoryManager();
        for (int i = 0; i < 9; i++) {
            manager.add(new Task(i, "Task", "Task description"));
        }

    }

    @Test
    void returningHistory() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            taskArray.add(new Task(i, "Task", "Task description"));
        }
        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void changingNodesWhenTaskAddedAgain() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            taskArray.add(new Task(i, "Task", "Task description"));
        }
        manager.add(new Task(3, "Task", "Task description"));
        taskArray.set(3, new Task(3, "Task", "Task description"));

        assertEquals(manager.getHistory(), taskArray);
    }
}