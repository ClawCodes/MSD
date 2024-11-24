package assignment07;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BadHashFunctorTest {
    @Test
    void testHash(){
        BadHashFunctor badHashFunctor = new BadHashFunctor();
        assertEquals(97, badHashFunctor.hash("a"));
        assertEquals(97, badHashFunctor.hash("abc"));
        assertEquals(98, badHashFunctor.hash("b"));
    }
}