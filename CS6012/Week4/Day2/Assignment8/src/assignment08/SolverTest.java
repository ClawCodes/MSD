package assignment08;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

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

    @Test
    public void testGetReadTinyMaze() throws FileNotFoundException {
        Solver solver = new Solver("tinyMaze.txt");
        Graph maze = solver.getMaze_();

        assertEquals(63, maze.size());
        assertEquals(51, solver.startVertex_);
        assertEquals("S", maze.getVertex(51).getValue());
        assertEquals(46, solver.endVertex_);
        assertEquals("G", maze.getVertex(46).getValue());
        assertEquals(7, solver.height_);
        assertEquals(9, solver.width_);

        Node topLeft = maze.getVertex(0);
        assertEquals("X", topLeft.getValue());
        assertEquals(new ArrayList(Arrays.asList(1, 9)), topLeft.neighbors());

        Node topRight = maze.getVertex(8);
        assertEquals("X", topRight.getValue());
        assertEquals(new ArrayList(Arrays.asList(7, 17)), topRight.neighbors());

        Node bottomLeft = maze.getVertex(54);
        assertEquals("X", bottomLeft.getValue());
        assertEquals(new ArrayList(Arrays.asList(45, 55)), bottomLeft.neighbors());

        Node bottomRight = maze.getVertex(62);
        assertEquals("X", bottomRight.getValue());
        assertEquals(new ArrayList(Arrays.asList(53, 61)), bottomRight.neighbors());
    }
}