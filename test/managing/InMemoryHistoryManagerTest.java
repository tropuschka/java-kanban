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
        task = new Task(1, "Task", "Task description");
        for (int i = 0; i < 9; i++) {
            manager.add(task);
        }

    }

    @Test
    void returningHistory() {
        ArrayList<Task> taskArray = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            taskArray.add(task);
        }
        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void ifHistoryMaxSize10() {
        manager.add(task);
        assertEquals(manager.getHistory().size(), 10);
    }
}