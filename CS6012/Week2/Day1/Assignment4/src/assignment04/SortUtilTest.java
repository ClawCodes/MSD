package assignment04;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

class SortUtilTest {

    private static Stream<Arguments> thresholds() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3),
                Arguments.of(4),
                Arguments.of(5),
                Arguments.of(6),
                Arguments.of(7),
                Arguments.of(8));
    }

    @ParameterizedTest
    @MethodSource("thresholds")
    void testSortInts(int threshold) {
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(4, 3, 2, 1));
        SortUtil.setMsThreshold(threshold);
        ArrayList<Integer> actual = SortUtil.msDriver(intList, Comparator.naturalOrder());

        assertEquals(1, actual.get(0));
        assertEquals(2, actual.get(1));
        assertEquals(3, actual.get(2));
        assertEquals(4, actual.get(3));
    }

    @ParameterizedTest
    @MethodSource("thresholds")
    void testSortStrings(int threshold) {
        ArrayList<String> stringList = new ArrayList<>(Arrays.asList("banana", "kiwi", "apple", "pear", "cherry"));
        SortUtil.setMsThreshold(threshold);
        ArrayList<String> actual = SortUtil.msDriver(stringList, Comparator.naturalOrder());

        assertEquals("apple", actual.get(0));
        assertEquals("banana", actual.get(1));
        assertEquals("cherry", actual.get(2));
        assertEquals("kiwi", actual.get(3));
        assertEquals("pear", actual.get(4));
    }

    @ParameterizedTest
    @MethodSource("thresholds")
    void testSort2(int threshold) {
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(55, -12, 47, 1001, 0, 89, 2));
        SortUtil.setMsThreshold(threshold);
        ArrayList<Integer> actual = SortUtil.msDriver(intList, Comparator.naturalOrder());

        assertEquals(-12, actual.get(0));
        assertEquals(0, actual.get(1));
        assertEquals(2, actual.get(2));
        assertEquals(47, actual.get(3));
        assertEquals(55, actual.get(4));
        assertEquals(89, actual.get(5));
        assertEquals(1001, actual.get(6));
    }

    @Test
    void testEmptySet() {
        ArrayList<Integer> intList = new ArrayList<>();
        ArrayList<Integer> actual = SortUtil.msDriver(intList, Comparator.naturalOrder());

        assertTrue(actual.isEmpty());
    }

    @Test
    void testDuplicates() {
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(4, 3, 2, 1, 3, 4));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 3, 4, 4));
        ArrayList<Integer> actual = SortUtil.msDriver(intList, Comparator.naturalOrder());

        assertEquals(expected, actual);
    }

    @Test
    void testReverseOrder() {
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(33, 2, -19, 507, 41));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(507, 41, 33, 2, -19));
        ArrayList<Integer> actual = SortUtil.msDriver(intList, Comparator.reverseOrder());

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> pivotIndexes() {
        return Stream.of(
                Arguments.of(0, 2),
                Arguments.of(1, 4),
                Arguments.of(2, 0),
                Arguments.of(3, 3),
                Arguments.of(4, 5),
                Arguments.of(5, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("pivotIndexes")
    void testPartition(int pivotIndex, int expectedPivotIndex) {
        // Arrange
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(21, 101, -7, 49, 324, 9));
        int pivotValue = intList.get(pivotIndex);

        // Act
        int actualPivotIndex = SortUtil.partition(intList, pivotIndex, 0,intList.size() - 1, Comparator.naturalOrder());

        // Assert
        assertEquals(expectedPivotIndex, actualPivotIndex);
        for (int i = 0; i < expectedPivotIndex; i++) {
            assertTrue(intList.get(i) < pivotValue);
        }
        for (int i = expectedPivotIndex + 1; i < intList.size(); i++) {
            assertTrue(intList.get(i) > pivotValue);
        }
    }

    @Test
    void testPartitionSubset() {
        // Arrange
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(21, 101, -7, 49, 324, 9));
        int pivotIndex = 3;
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(21, 101, -7, 9, 49, 324));

        // Act
        int actualPivotIndex = SortUtil.partition(intList, pivotIndex, 2, intList.size() - 1, Comparator.naturalOrder());

        // Assert
        assertEquals(expected, intList);
        assertEquals(4, actualPivotIndex);
    }

    @Test
    void testPartitionWithDuplicates() {
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1));
        int pivotIndex = 3;
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1));

        int actualPivotIndex = SortUtil.partition(intList, pivotIndex, 0, intList.size() - 1, Comparator.naturalOrder());

        // Assert
        assertEquals(expected, intList);
        assertEquals(pivotIndex, actualPivotIndex);
    }

    private static Stream<Arguments> pivotMethods() {
        return Stream.of(
                Arguments.of("randomPivot"),
                Arguments.of("threeMedianPivot"),
                Arguments.of("middlePivot")
        );
    }

    @ParameterizedTest
    @MethodSource("pivotMethods")
    void quickSort(String pivotMethod) {
        SortUtil.setPivotMethod(pivotMethod);
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(21, 101, -7, 49, 324, 9));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(-7, 9, 21, 49, 101, 324));

        SortUtil.quicksort(intList, Comparator.naturalOrder());
        assertEquals(expected, intList);
    }


    @Test
    void quickSortIrregularList() {
        //Empty list
        ArrayList<Integer> intList = new ArrayList<>();
        SortUtil.quicksort(intList, Comparator.naturalOrder());
        assertEquals(intList, intList);

        //List of length 1
        intList.add(4);
        SortUtil.quicksort(intList, Comparator.naturalOrder());
        assertEquals(intList, intList);

        //List of length 2
        intList.add(1);
        SortUtil.quicksort(intList, Comparator.naturalOrder());
        assertEquals(intList, intList);
    }

    @Test
    void quickSortDuplicates(){
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(34, 1, 89, 1, -4));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(-4, 1, 1, 34, 89));
        SortUtil.quicksort(intList, Comparator.naturalOrder());
        assertEquals(intList, expected);
    }

    @Test
    void quickSortMinMax(){
        // Min first element, max center
        ArrayList<Integer> minFirstMaxCenter = new ArrayList<>(Arrays.asList(-4, 1, 89, 2, 34));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(-4, 1, 2, 34, 89));
        SortUtil.quicksort(minFirstMaxCenter, Comparator.naturalOrder());
        assertEquals(expected, minFirstMaxCenter);

        // Max first element, min center
        ArrayList<Integer> maxFirstMinCenter = new ArrayList<>(Arrays.asList(89, 1, -4, 2, 34));
        SortUtil.quicksort(maxFirstMinCenter, Comparator.naturalOrder());
        assertEquals(expected, maxFirstMinCenter);

        // Min last element, max center
        ArrayList<Integer> minLastMaxCenter = new ArrayList<>(Arrays.asList(1, 34, 89, 2, -4));
        SortUtil.quicksort(minLastMaxCenter, Comparator.naturalOrder());
        assertEquals(expected, minLastMaxCenter);

        // Max last element, min center
        ArrayList<Integer> maxLastMinCenter = new ArrayList<>(Arrays.asList(1, 34, -4, 2, 89));
        SortUtil.quicksort(maxLastMinCenter, Comparator.naturalOrder());
        assertEquals(expected, maxLastMinCenter);

        // Min first, max last
        ArrayList<Integer> minFirstMaxLast = new ArrayList<>(Arrays.asList(-4, 1, 34, 2, 89));
        SortUtil.quicksort(minFirstMaxLast, Comparator.naturalOrder());
        assertEquals(expected, minFirstMaxLast);

        // Max first, min last
        ArrayList<Integer> maxFirstMinLast = new ArrayList<>(Arrays.asList(89, 1, 34, 2, -4));
        SortUtil.quicksort(maxFirstMinLast, Comparator.naturalOrder());
        assertEquals(expected, maxFirstMinLast);
    }

    private static Stream<Arguments> medianSlices() {
        return Stream.of(
                Arguments.of(1, 4, 3),
                Arguments.of(0, 4, 3),
                Arguments.of(0, 3, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("medianSlices")
    void testThreeMedianPivot(int start, int end, int expected) {
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(-4, 1, 89, 2, 34));
        int actual = SortUtil.threeMedianPivot(intList, start, end, Comparator.naturalOrder());
        assertEquals(expected, actual);
    }


    private static Stream<Arguments> middleSlices() {
        return Stream.of(
                Arguments.of(1, 4, 2),
                Arguments.of(0, 4, 2),
                Arguments.of(0, 1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("middleSlices")
    void testMiddlePivot(int start, int end, int expected) {
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(-4, 1, 89, 2, 34));
        int actual = SortUtil.middlePivot(start, end);
        assertEquals(expected, actual);
    }

    @Test
    void qsDuplicate(){
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1));
        SortUtil.quicksort(intList, Comparator.naturalOrder());
        assertEquals(intList, intList);
    }
}