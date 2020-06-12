/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    private Node first;
    private Node last;
    private int count;

    public Deque() {
        first = new Node();
        last = first;
        count = 0;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else if (count == 0) {
            first.item = item;
            count++;
        }
        else {
            Node node = new Node();
            first.previous = node;
            node.next = first;
            node.previous = null;
            node.item = item;
            first = node;
            count++;
        }
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else if (count == 0) {
            last.item = item;
            count++;
        }
        else {
            Node node = new Node();
            last.next = node;
            node.previous = last;
            node.item = item;
            node.next = null;
            last = node;
            count++;
        }
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        else if (count == 1) {
            Item item = last.item;
            last.item = null;
            count--;
            return item;
        }
        else {
            Item item = last.item;
            last = last.previous;
            last.next = null;
            count--;
            return item;
        }
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        else if (count == 1) {
            Item item = first.item;
            first.item = null;
            count--;
            return item;
        }
        else {
            Item item = first.item;
            first = first.next;
            first.previous = null;
            count--;
            return item;
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            else {
                Item item = current.item;
                current = current.next;
                return item;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        // Implement unit tests
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
        }

        for (int i : deque) {
            System.out.println(i);
        }
    }
}
