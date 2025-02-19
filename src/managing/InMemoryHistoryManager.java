package managing;

import taskmodels.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> linkedHistory = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            history.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return history;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            Task historyTask = task.createHistoryExample();
            Node taskNode;

            remove(task.getId());
            taskNode = new Node(historyTask, tail);
            if (head == null) {
                head = taskNode;
            } else {
                tail.next = taskNode;
            }
            tail = taskNode;

            linkedHistory.put(task.getId(), taskNode);
        }
    }

    @Override
    public void remove(int id) {
        Node toRemove = linkedHistory.get(id);
        if (toRemove != null) {
            if (toRemove.prev != null) {
                toRemove.prev.next = toRemove.next;
                if (toRemove.next == null) tail = toRemove.prev;
                else toRemove.next.prev = toRemove.prev;
            } else {
                head = toRemove.next;
                if (head == null) tail = null;
                else head.prev = null;
            }
            linkedHistory.remove(id);
        }
    }

    private class Node {
        private Node prev;
        private Task data;
        private Node next;

        public Node(Task data, Node prev) {
            this.prev = prev;
            this.data = data;
        }
    }
}

