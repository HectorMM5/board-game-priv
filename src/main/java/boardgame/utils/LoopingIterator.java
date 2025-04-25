package boardgame.utils;

import java.util.Iterator;
import java.util.List;

/**
 * A custom iterator that loops endlessly over a list of items.
 * When the end of the list is reached, it wraps around to the beginning.
 *
 * Useful for managing turn-based logic such as rotating through players.
 *
 * @param <Item> the type of elements returned by this iterator
 * 
 * @author Hector Mendana Morales
 */
public class LoopingIterator<Item> implements Iterator<Item> {
    private final List<Item> list;
    private int index = 0;

    /**
     * Constructs a LoopingIterator for the specified list.
     *
     * @param list the list to iterate through in a loop
     * @throws IllegalArgumentException if the list is null or empty
     */
    public LoopingIterator(List<Item> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }
        this.list = list;
    }

    /**
     * Always returns true since the iterator loops indefinitely.
     *
     * @return {@code true}
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    /**
     * Returns the next element in the list, wrapping around if necessary.
     *
     * @return the next item in the iteration
     */
    @Override
    public Item next() {
        Item element = list.get(index);
        index = (index + 1) % list.size();
        return element;
    }
}
