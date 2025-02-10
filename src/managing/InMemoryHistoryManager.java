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
            Node targetNext = null;
            Node targetPrev = null;
            if (toRemove == head && toRemove != tail) head = toRemove.next;
            if (toRemove == tail && toRemove != head) tail = toRemove.prev;
            if (toRemove.next != null) {
                targetNext = toRemove.next;
            }
            if (toRemove.prev != null) {
                targetPrev = toRemove.prev;
            }
            if (targetNext != null) targetNext.prev = targetPrev;
            if (targetPrev != null) targetPrev.next = targetNext;
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

