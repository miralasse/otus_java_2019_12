package ru.otus.l03;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * DIYArrayList.
 *
 * @author Evgeniya_Yanchenko
 */
public class DIYArrayList<E> implements List<E> {

    private static final String WRONG_CAPACITY_MSG = "Initial capacity must be greater than zero";
    private static final String EMPTY_LIST_MSG = "List is empty";
    private static final String NULL_COLLECTION_MSG = "Collection to be added is null";

    private static final int INITIAL_CAPACITY = 10;
    private static final float INCREASE_FACTOR = 2.0f;
    private int size;
    private Object[] array;

    public DIYArrayList() {
        this(INITIAL_CAPACITY);
    }

    public DIYArrayList(int capacity) {
        if (capacity <=0) {
            throw new IllegalArgumentException(WRONG_CAPACITY_MSG);
        }
        array = new Object[capacity];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (size >= array.length) {
            array = Arrays.copyOf(array, (int)(size * INCREASE_FACTOR));
        }
        array[size++] = e;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        if (collection == null) {
            throw new IllegalArgumentException(NULL_COLLECTION_MSG);
        } else if (collection.isEmpty()) {
            return false;
        }
        int newLength = size + collection.size();
        if (newLength > array.length) {
            array = Arrays.copyOf(array, newLength);
        }
        System.arraycopy(collection.toArray(), 0, array, size, collection.size());
        return true;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public E remove(int index) {
        if (isEmpty()) {
            throw new IllegalArgumentException(EMPTY_LIST_MSG);
        }
        E element = (E) array[index];
        System.arraycopy(array, index + 1, array, index, array.length - index - 1);
        array[size--] = null;
        return element;
    }

    @Override
    public E get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}
