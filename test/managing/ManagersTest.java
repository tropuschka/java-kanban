package managing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    void taskManagerCreation() {
        TaskManager taskManager = Managers.createTaskManager();
        assertNotNull(taskManager);
    }

    @Test
    void historyManagerCreation() {
        HistoryManager historyManager = Managers.createHistoryManager();
        assertNotNull(historyManager);
    }
}