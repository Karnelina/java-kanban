package Managers.History;

import java.util.*;

public class CustomLinkedList<T> {
    private Node<T> first;
    private Node<T> last;

    public Node<T> linkLast(T elem) {
        final Node<T> newNode = new Node<>(last, elem, null);

        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }

        last = newNode;

        return newNode;
    }

    public List<T> getTasks() {
        List<T> tasks = new ArrayList<>();
        Node<T> elem = first;

        while (elem != null) {
            tasks.add(elem.item);
            elem = elem.next;
        }

        return tasks;
    }

    public void removeNode(Node<T> node) {
        if (node == null) {
            return;
        }

        if (node.equals(first)) {
            first = node.next;

            if (node.next != null) {
                node.next.prev = null;
            }
        } else {
            node.prev.next = node.next;

            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
    }

    public void clear() {
        first = null;
        last = null;
    }

}
