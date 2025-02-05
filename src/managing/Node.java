package managing;

import java.util.ArrayList;

public class Node<T> {
    private Node<T> prev;
    private T data;
    private Node<T> next;

    public Node(T data, Node<T> prev) {
        this.prev = prev;
        this.data = data;
        this.next = null;
    }

    public void removeNode() {
        Node<T> targetPrev = prev;
        Node<T> targetNext = next;
        if (targetNext != null) targetNext.setPrev(targetPrev);
        if (targetPrev != null) targetPrev.setNext(targetNext);
    }

    private void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    private void setNext(Node<T> next) {
        this.next = next;
    }

    public T getData() {
        return data;
    }
}
