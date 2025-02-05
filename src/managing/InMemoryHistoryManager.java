package managing;

import taskmodels.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Task> historyFootprints = new HashMap<>();
    private HashMap<Integer, Node<Task>> linkedHistory = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        for (Node<Task> node : linkedHistory.values()) {
            Task task = node.getData();
            history.add(historyFootprints.get(task.getId()));
        }
        return history;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            Task historyTask = task.createHistoryExample();
            Node<Task> taskNode;
            historyFootprints.put(task.getId(), historyTask);

            if (linkedHistory.containsKey(task.getId())) remove(task.getId());

            ArrayList<Task> currentHistory = getHistory();
            if (currentHistory.size() > 0) {
                Integer lastTaskId = currentHistory.getLast().getId();
                taskNode = new Node<>(historyTask, linkedHistory.get(lastTaskId));
            } else taskNode = new Node<>(historyTask, null);

            linkedHistory.put(task.getId(), taskNode);
        }
    }

    @Override
    public void remove(int id) {
        Node<Task> toRemove = linkedHistory.get(id);
        toRemove.removeNode();
        linkedHistory.remove(id);
    }
}
