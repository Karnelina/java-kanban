package managers.History;
import Tasks.*;

import java.util.*;

public class CustomLinkedList {
    private Node<Task> first;
    private Node<Task> last;

    public Node<Task> linkLast (Task task) {
        Node<Task> newNode = new Node<>(last, task, null);

        if(last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;

        return newNode;
    }

    public List<Task> getTasks() {
        List<Task> tasks =  new ArrayList<>();
        Node<Task> elem = first;

        while (elem != null) {
            tasks.add(elem.item);
            elem = elem.next;
        }

        return tasks;
    }

    public void removeNode(Node<Task> node) {
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
