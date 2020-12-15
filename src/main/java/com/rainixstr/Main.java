package com.rainixstr;

public class Main {
    public static void main(String[] arg) {
        BinaryHeap<Integer> heap = new BinaryHeap(5);
        for (int i = 0; i < 10000; i++) {
            heap.add(i);
        }
        System.out.println(heap);
        System.out.println(heap.peek());
    }
}
