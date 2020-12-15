package com.rainixstr;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BinaryHeap<T extends Comparable<T>> implements Queue<T> {
    private Object[] heap;
    private int size;
    private int capacity;

    private final static int START_INDEX = 1;
    private final static int MIN_CAPACITY = 20;
    private final static double LOAD_FACTOR = 1.5;

    public BinaryHeap() {
        this.size = 0;
        this.heap = new Object[MIN_CAPACITY];
        this.capacity = MIN_CAPACITY;
    }

    public BinaryHeap(int n) {
        this.size = 0;
        this.heap = new Object[n + 1];
        this.capacity = n + 1;
    }

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return Math.min(size, Integer.MAX_VALUE);
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * More formally, returns {@code true} if and only if this collection
     * contains at least one element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param o element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified
     * element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this collection
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              collection does not permit null elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean contains(Object o) {
        for (int i = 1; i <= size; i++) {
            if (heap[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an iterator over the elements in this collection.  There are no
     * guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    @Override
    public Iterator<T> iterator() {
        return new PriorityQueueIterator();
    }

    class PriorityQueueIterator implements Iterator<T> {
        int cursor = START_INDEX;
        Object lastNext = null;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return cursor <= size;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @SuppressWarnings("unchecked cast")
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastNext = heap[cursor++];
            return (T) lastNext;
        }
    }

    /**
     * Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order. The returned array's {@linkplain Class#getComponentType
     * runtime component type} is {@code Object}.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection.  (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * @return an array, whose {@linkplain Class#getComponentType runtime component
     * type} is {@code Object}, containing all of the elements in this collection
     * @apiNote This method acts as a bridge between array-based and collection-based APIs.
     * It returns an array whose runtime type is {@code Object[]}.
     * Use {@link #toArray(Object[]) toArray(T[])} to reuse an existing
     * array, or use {@link #toArray(IntFunction)} to control the runtime type
     * of the array.
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        for (int i = START_INDEX; i <= size; i++) {
            array[i - 1] = heap[i];
        }
        return array;
    }

    private void growUpHeap() {
        capacity = (int) (capacity * LOAD_FACTOR);
        Object[] oldHeap = heap.clone();
        heap = new Object[capacity];
        for (int i = 1; i < size; i++) {
            heap[i] = oldHeap[i];
        }
    }

    @SuppressWarnings("unchecked cast")
    private void swim(int k) {
        while (k > 1 && ((T) heap[k]).compareTo((T) heap[k / 2]) > 0) {
            swap(k, k / 2);
            k = k / 2;
        }
    }

    @SuppressWarnings("unchecked cast")
    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k; //left child
            if (j < size && ((T) heap[j]).compareTo((T) heap[j + 1]) < 0) {
                j++; //right child
            }
            if (((T) heap[k]).compareTo((T) heap[j]) >= 0) {
                break; //inv holds
            }
            swap(k, j);
            k = j;
        }
    }

    @SuppressWarnings("unchecked cast")
    private void swap(int i, int j) {
        T t = (T) heap[i];
        heap[i] = heap[j];
        heap[j] = t;
    }

    /**
     * Inserts the specified element into this queue if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * {@code true} upon success and throwing an {@code IllegalStateException}
     * if no space is currently available.
     *
     * @param t the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws IllegalStateException    if the element cannot be added at this
     *                                  time due to capacity restrictions
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this queue
     * @throws NullPointerException     if the specified element is null and
     *                                  this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *                                  prevents it from being added to this queue
     */
    @Override
    public boolean add(T t) {
        if (contains(t)) {
            return false;
        }
        if (++size > capacity - 1) {
            growUpHeap();
        }
        heap[size] = t;
        swim(size);
        return true;
    }

    /**
     * Returns {@code true} if this collection contains all of the elements
     * in the specified collection.
     *
     * @param c collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all of the elements
     * in the specified collection
     * @throws ClassCastException   if the types of one or more elements
     *                              in the specified collection are incompatible with this
     *                              collection
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *                              or more null elements and this collection does not permit null
     *                              elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                              or if the specified collection is null.
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation).  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in progress.
     * (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param c collection containing elements to be added to this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this collection
     * @throws NullPointerException          if the specified collection contains a
     *                                       null element and this collection does not permit null elements,
     *                                       or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this
     *                                       collection
     * @throws IllegalStateException         if not all the elements can be added at
     *                                       this time due to insertion restrictions
     * @see #add(Object)
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean result = true;
        for (Object element : c) {
            result &= add((T) element);
        }
        return result;
    }

    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *                                       is not supported by this collection
     */
    @Override
    public void clear() {
        heap = new Object[capacity];
    }

    /**
     * Retrieves and removes the head of this queue.  This method differs
     * from {@link #poll() poll()} only in that it throws an exception if
     * this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Override
    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public T poll() {
        if (isEmpty()) {
            return null;
        }
        T removeElement = (T) heap[1];
        heap[1] = heap[size--];
        sink(1);
        return removeElement;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from {@link #peek peek} only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Override
    public T element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return peek();
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return (T) heap[1];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = START_INDEX; i < size; i++) {
            sb.append(heap[i]);
            sb.append(", ");
        }
        sb.append(heap[size]);
        return "BinaryHeap (size=" + size + ") {" + sb + "}";
    }

    @SuppressWarnings("unused")
    @Override
    public void forEach(Consumer<? super T> action) {
    }

    @SuppressWarnings("unused")
    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @SuppressWarnings("unused")
    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        return null;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean remove(Object o) {
        return false;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return false;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @SuppressWarnings("unused")
    @Override
    public Spliterator<T> spliterator() {
        return null;
    }

    @SuppressWarnings("unused")
    @Override
    public Stream<T> stream() {
        return null;
    }

    @SuppressWarnings("unused")
    @Override
    public Stream<T> parallelStream() {
        return null;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean offer(T t) {
        return false;
    }
}
