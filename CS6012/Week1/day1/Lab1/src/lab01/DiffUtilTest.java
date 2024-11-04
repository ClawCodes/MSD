package lab01;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class TestFindSmallestDiff {
    private int[] arr1;
    private int[] arr2;
    private int[] arr3;
    private int[] arr4;
    private int[] arr5;

    @BeforeEach
    protected void setUp() throws Exception {
        arr1 = new int[0];
        arr2 = new int[] { 3, 3, 3 };
        arr3 = new int[] { 52, 4, -8, 0, -17 };
        arr4 = new int[] {0, 1, 4, 10, 20}; // smallest diff at the start
        arr5 = new int[] {4, 10, 20, 0, 1}; // smallest diff at the end
    }

    @AfterEach
    protected void tearDown() throws Exception {
        arr1 = null;
        arr2 = null;
        arr3 = null;
        arr4 = null;
        arr5 = null;
    }

    @Test
    public void emptyArray() {
        assertEquals(-1, DiffUtil.findSmallestDiff(arr1));
    }

    @Test
    public void allArrayElementsEqual() {
        assertEquals(0, DiffUtil.findSmallestDiff(arr2));
    }

    @Test
    public void smallRandomArrayElements() {
        assertEquals(4, DiffUtil.findSmallestDiff(arr3));
    }

    @Test
    public void smallestDiffWthStartAndEndElements() {}
}