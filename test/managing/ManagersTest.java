package managing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    static Managers mainManager;

    @BeforeAll
    static void mainManagerCreation() {
        mainManager = new Managers();
    }

    @Test
    void taskManagerCreation() {
        InMemoryTaskManager taskManager = mainManager.createTaskManager();
        assertNotNull(taskManager);
    }

    @Test
    void historyManagerCreation() {
        InMemoryHistoryManager historyManager = mainManager.createHistoryManager();
        assertNotNull(historyManager);
    }
}