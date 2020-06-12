/* *****************************************************************************
 *  Name:           Mostafa Asaad
 *  Date:           29-4-2020
 *  Description:    Randomized Queue data structure for uniform random array
 *                  access.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final short MIN_SIZE = 8;
    private static final short FIRST = 0;
    private Item[] array;
    private int tail;


    public RandomizedQueue() {
        array = (Item[]) new Object[MIN_SIZE];
        tail = 0;
    }

    public boolean isEmpty() {
        return tail == 0;
    }

    public int size() {
        return tail;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else {
            if (tail >= array.length) {
                extend();
            }
            array[tail++] = item;
        }
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        else {
            StdRandom.shuffle(array, FIRST, tail);
            Item item = array[--tail];
            array[tail] = null;
            if (tail < array.length / 4) {
                shrink();
            }
            return item;
        }
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        else {
            StdRandom.shuffle(array, FIRST, tail);
            return array[tail - 1];
        }
    }

    private void extend() {
        Item[] copy = (Item[]) new Object[array.length + array.length];
        for (int i = 0; i < tail; i++) {
            copy[i] = array[i];
        }
        array = copy;
    }

    private void shrink() {
        Item[] copy = (Item[]) new Object[array.length / 2];
        for (int i = 0; i < tail; i++) {
            copy[i] = array[i];
        }
        array = copy;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int i = FIRST;

        private RandomizedQueueIterator() {
            StdRandom.shuffle(array, FIRST, tail);
        }

        public boolean hasNext() {
            return i < tail;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            else {
                return array[i++];
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        // implement unit tests
    }
}
