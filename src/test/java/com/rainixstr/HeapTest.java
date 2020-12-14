package com.rainixstr;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HeapTest {

    @Test
    public void add() {
        Heap heap = new Heap();
        heap.add(5);
        heap.add(6);
        heap.add(20);
        heap.add(7);
        assertEquals("Heap{[0, 20, 7, 6, 5]} 4", heap.toString());
        heap.add(2);
        assertEquals("Heap{[0, 20, 7, 6, 5, 2]} 5", heap.toString());
    }

    @Test
    public void remove() {
        Heap heap = new Heap();
        heap.add(5);
        heap.add(6);
        heap.add(20);
        heap.add(7);
        assertEquals(20, heap.remove());
        assertEquals(7, heap.remove());
    }
}