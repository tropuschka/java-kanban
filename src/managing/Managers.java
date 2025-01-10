package managing;

public class Managers {
    public InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    public InMemoryHistoryManager createHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
