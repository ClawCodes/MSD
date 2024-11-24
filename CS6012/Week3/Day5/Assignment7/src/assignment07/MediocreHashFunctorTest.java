package assignment07;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MediocreHashFunctorTest {
    @Test
    void testHash(){
        MediocreHashFunctor mediocreHashFunctor = new MediocreHashFunctor();
        assertEquals(979899, mediocreHashFunctor.hash("abc"));
        assertEquals(98, mediocreHashFunctor.hash("b"));
    }
}