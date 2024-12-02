package assignment08;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SolverTest {
    @Test
    public void testGetNeighborsTopLeftCorner(){
        Solver solver = new Solver();
        solver.setWidth(9);
        solver.setHeight(7);
        ArrayList<Integer> actual = solver.getNeighbors(0, 0);

        assertEquals(2, actual.size());
        assertEquals(1, actual.get(0));
        assertEquals(9, actual.get(1));
    }

    @Test
    public void testGetNeighborsTopRightCorner(){
        Solver solver = new Solver();
        solver.setWidth(9);
        solver.setHeight(7);
        ArrayList<Integer> actual = solver.getNeighbors(0, 8);
        assertEquals(2, actual.size());
        assertEquals(7, actual.get(0));
        assertEquals(17, actual.get(1));
    }

    @Test
    public void testGetNeighborsBottomLeftCorner(){
        Solver solver = new Solver();
        solver.setWidth(9);
        solver.setHeight(7);
        ArrayList<Integer> actual = solver.getNeighbors(6, 0);
        assertEquals(2, actual.size());
        assertEquals(45, actual.get(0));
        assertEquals(55, actual.get(1));
    }

    @Test
    public void testGetNeighborsBottomRightCorner(){
        Solver solver = new Solver();
        solver.setWidth(9);
        solver.setHeight(7);
        ArrayList<Integer> actual = solver.getNeighbors(6, 8);
        assertEquals(2, actual.size());
        assertEquals(53, actual.get(0));
        assertEquals(61, actual.get(1));
    }

    @Test
    public void testGetNeighborsCentralNode(){
        Solver solver = new Solver();
        solver.setWidth(9);
        solver.setHeight(7);
        ArrayList<Integer> actual = solver.getNeighbors(3, 4);
        assertEquals(4, actual.size());
        assertEquals(22, actual.get(0));
        assertEquals(30, actual.get(1));
        assertEquals(32, actual.get(2));
        assertEquals(40, actual.get(3));
    }

    @Test
    public void testGetNeighborsCentralRightMost(){
        Solver solver = new Solver();
        solver.setWidth(9);
        solver.setHeight(7);
        ArrayList<Integer> actual = solver.getNeighbors(3, 8);
        assertEquals(3, actual.size());
        assertEquals(26, actual.get(0));
        assertEquals(34, actual.get(1));
        assertEquals(44, actual.get(2));
    }
}