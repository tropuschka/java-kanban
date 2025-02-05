package managing;

import taskmodels.Task;

import java.util.ArrayList;

public interface HistoryManager {
    ArrayList<Task> getHistory();

    void add(Task task);

    void remove(int id);
}
