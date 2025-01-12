package managing;

public class Managers {
    public static TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager createHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
