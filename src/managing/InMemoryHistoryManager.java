package managing;

import taskmodels.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task newTask) {
        history.add(newTask);
        if (history.size() > 10) {
            history.removeFirst();
        }
    }
}
