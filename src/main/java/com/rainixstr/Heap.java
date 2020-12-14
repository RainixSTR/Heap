package com.rainixstr;

import java.util.ArrayList;
import java.util.List;

public class Heap {
    private final List<Integer> heap;
    private int size = 0;

    public Heap() {
        heap = new ArrayList();
        heap.add(0);
    }

    //Поднимает эелемент, пока не найдем корень больший текущего
    private void swim(int k) {
        while (k > 1 && heap.get(k) > heap.get(k / 2)) {
            swap(k, k / 2);
            k = k / 2;
        }
    }

    //Опускаем элемент до тех пор, пока не будет потомков, больших текущего
    private void sink(int k) {
        while (2 * k <= size) {
            int leftChild = 2 * k; //левый потомок
            if (leftChild < size && heap.get(leftChild) < heap.get(leftChild + 1)) leftChild++; // leftChild + 1 -правый потомок
            if (heap.get(k) >= heap.get(leftChild)) break; //проверка инварианта
            swap(k, leftChild);
            k = leftChild;
        }
    }

    //Замена элементов
    private void swap(int i, int j) {
        int t = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, t);
    }

    //Добавление элемента
    public void add(int v) {
        size++;
        if (size <= heap.size() - 1)
            heap.set(size, v);
        else
            heap.add(v);
        swim(size);
    }

    //Извлечение корня кучи
    public int remove() {
        int max = heap.get(1);
        swap(1, size--);
        sink(1);
        return max;
    }

    @Override
    public String toString() {
        return "Heap{" + heap + "} " + size;
    }
}