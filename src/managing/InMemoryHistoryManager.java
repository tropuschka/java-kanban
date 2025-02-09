package managing;

import taskmodels.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> linkedHistory = new HashMap<>();
    private Node head;
    private Node tail;

    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node currentNode = head;
        do {
            history.add(currentNode.data);
            currentNode = currentNode.next;
        } while (currentNode.next != null);
        return history;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            Task historyTask = task.createHistoryExample();
            Node taskNode;

            if (linkedHistory.containsKey(task.getId())) remove(task.getId());

            ArrayList<Task> currentHistory = getHistory();
            if (!currentHistory.isEmpty()) {
                taskNode = new Node(historyTask, tail);
            } else {
                taskNode = new Node(historyTask, null);
                head = taskNode;
            }
            tail = taskNode;

            linkedHistory.put(task.getId(), taskNode);
        }
    }

    @Override
    public void remove(int id) {
        Node toRemove = linkedHistory.get(id);
        Node targetPrev = toRemove.prev;
        Node targetNext = toRemove.next;
        if (targetNext != null) targetNext = targetPrev;
        if (targetPrev != null) targetPrev = targetNext;
        linkedHistory.remove(id);
    }

    private static class Node {
        private Node prev;
        private Task data;
        private Node next;

        public Node(Task data, Node prev) {
            this.prev = prev;
            this.data = data;
            this.next = null;
        }
    }
}

