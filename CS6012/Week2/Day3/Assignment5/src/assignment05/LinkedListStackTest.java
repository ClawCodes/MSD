package assignment05;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListStackTest {
    @Test
    void testPopulatedStack(){
        LinkedListStack<String> stack = new LinkedListStack<>();
        assertTrue(stack.isEmpty());
        stack.push("a");
        stack.push("b");
        stack.push("c");
        assertFalse(stack.isEmpty());

        assertEquals(3, stack.size());
        assertEquals("c", stack.peek());
        assertEquals("c", stack.pop());
        assertEquals(2, stack.size());

        assertEquals("b", stack.peek());
        assertEquals("b", stack.pop());
        assertEquals(1, stack.size());
        assertEquals("a", stack.peek());
        assertEquals(1, stack.size());
        assertEquals("a", stack.pop());
        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());

        assertThrows(NoSuchElementException.class, stack::pop);
        assertThrows(NoSuchElementException.class, stack::peek);

        stack.push("a");
        stack.push("b");
        stack.push("c");
        assertEquals(3, stack.size());
        stack.clear();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());

        assertThrows(NoSuchElementException.class, stack::pop);
        assertThrows(NoSuchElementException.class, stack::peek);
    }
}