package com.rainixstr;

public class Main {
    public static void main(String[] arg) {
        Heap heap = new Heap();
        heap.add(5);
        heap.add(6);
        heap.add(20);
        heap.add(7);
        System.out.println(heap);
        System.out.println(heap.remove());
        heap.add(9);
        System.out.println(heap);
        System.out.println(heap.remove());
        heap.add(1);
        System.out.println(heap);
        System.out.println(heap.remove());
        heap.add(2);
        System.out.println(heap);
        System.out.println(heap.remove());
    }
}
