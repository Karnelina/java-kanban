package Managers.History;

public class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E elem, Node<E> next) {
        this.item = elem;
        this.next = next;
        this.prev = prev;
    }
}
