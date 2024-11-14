package assignment04;

import javax.crypto.spec.PSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class SortUtil {
    private static int msThreshold = 4;
    private static String pivotMethod = "randomPivot";


    public static <T> void merge(ArrayList<T> outputArray, ArrayList<T> left, ArrayList<T> right, Comparator<? super T> comparator) {
        int n1 = left.size();
        int n2 = right.size();

        int i = 0, j = 0, k = 0;
        while (i < n1 && j < n2) {
            if (comparator.compare(left.get(i), right.get(j)) < 0) {
                outputArray.set(k, left.get(i));
                i++;
            } else {
                outputArray.set(k, right.get(j));
                j++;
            }
            k++;
        }
        while (i < n1) {
            outputArray.set(k, left.get(i));
            i++;
            k++;
        }
        while (j < n2) {
            outputArray.set(k, right.get(j));
            j++;
            k++;
        }
    }

    public static <T> ArrayList<T> msDriver(ArrayList<T> list, Comparator<? super T> comparator) {
        ArrayList<T> newList = new ArrayList<>();
        for (T item : list) {
            newList.add(item);
        }
        mergesort(newList, comparator);
        return newList;
    }

    public static <T> void insertionSort(ArrayList<T> list, Comparator<? super T> comparator) {
        int n = list.size();
        for (int i = 1; i < n; ++i) {
            T key = list.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
    }

    public static void setMsThreshold(int msThreshold) {
        if (msThreshold < 1) {
            throw new IllegalArgumentException("Threshold must be greater than 1");
        }
        SortUtil.msThreshold = msThreshold;
    }

    public static <T> void mergesort(ArrayList<T> list, Comparator<? super T> comparator) {
        if (list.size() <= msThreshold) {
            insertionSort(list, comparator);
        } else {
            int mid = list.size() / 2;
            ArrayList<T> left = new ArrayList<>();
            ArrayList<T> right = new ArrayList<>();
            for (int i = 0; i < mid; i++) {
                left.add(list.get(i));
            }
            for (int i = mid; i < list.size(); i++) {
                right.add(list.get(i));
            }
            mergesort(left, comparator);
            mergesort(right, comparator);
            merge(list, left, right, comparator);
        }
    }

    public static int randomPivot(int start, int end) {
        Random random = new Random();
        return random.nextInt(end - start) + start;
    }

    public static <T> int threeMedianPivot(ArrayList<T> list, int start, int end, Comparator<? super T> comparator) {
        if (end - start <= 2) {
            return randomPivot(start, end);
        }
        int pivotOne = (int) ((end - start) * 0.25 + start);
        T one = list.get(pivotOne);
        int pivotTwo = (int) ((end - start) * 0.5 + start);
        T two = list.get(pivotTwo);
        int pivotThree = (int) ((end - start) * 0.75 + start);
        T three = list.get(pivotThree);

        boolean oneLTwo = comparator.compare(one, two) < 0;
        boolean oneLThree = comparator.compare(one, three) < 0;
        boolean twoLThree = comparator.compare(two, three) < 0;

        if ((oneLTwo && !oneLThree) || (!oneLTwo && oneLThree)) {
            return pivotOne;
        } else if ((oneLTwo && twoLThree) || (!twoLThree && !oneLTwo)) {
            return pivotTwo;
        } else {
            return pivotThree;
        }
    }

    public static int middlePivot(int start, int end) {
        if (end - start <= 2) {
            return randomPivot(start, end);
        }
        return ((end -start) / 2) + start;
    }

    public static <T> void swap(ArrayList<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static <T> int partition(ArrayList<T> list, int pivotIndex, int start, int end, Comparator<? super T> comparator){
        int leftIndex = start;
        int rightIndex = end - 1;
        T pivotValue = list.get(pivotIndex);

        swap(list, pivotIndex, end);

        while (leftIndex < rightIndex){
            while (comparator.compare(list.get(leftIndex), pivotValue) <= 0 && leftIndex < rightIndex) {
                leftIndex++;
            }
            while (comparator.compare(list.get(rightIndex), pivotValue) >= 0 && rightIndex > leftIndex) {
                rightIndex--;
            }
            swap(list, leftIndex, rightIndex);
        }
        if (comparator.compare(list.get(end), list.get(leftIndex)) <= 0) {
            swap(list, leftIndex, end);
            return leftIndex;
        }
        return end;
    }

    public static void setPivotMethod(String pivotMethod) {
        ArrayList<String> methods = new ArrayList<>(Arrays.asList("randomPivot", "threeMedianPivot", "middlePivot"));
        if (methods.contains(pivotMethod)) {
            SortUtil.pivotMethod = pivotMethod;
        } else
            throw new IllegalArgumentException("Pivot method '" + pivotMethod + "' does not exist");
    }

    private static <T> void quicksort(ArrayList<T> list, Comparator<? super T> comparator, int start, int end) {
        int pivotIndex;
        if (end - start < 1) {
            return;
        }
        if (pivotMethod.equals("randomPivot")) {
            pivotIndex = randomPivot(start, end);
        } else if (pivotMethod.equals("threeMedianPivot")) {
            pivotIndex = threeMedianPivot(list, start, end, comparator);
        } else {
            pivotIndex = middlePivot(start, end);
        }
        int newPivotIndex = partition(list, pivotIndex, start, end, comparator);
        quicksort(list, comparator, start, newPivotIndex - 1);
        quicksort(list, comparator, newPivotIndex + 1, end);
    }


    public static <T> void quicksort(ArrayList<T> list, Comparator<? super T> comparator) {
        quicksort(list, comparator, 0, list.size() - 1);
    }

    public static ArrayList<Integer> generateBestCase(int size) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            list.add(i);
        }
        return list;
    }

    public static ArrayList<Integer> generateAverageCase(int size) {
        Random random = new Random();
        ArrayList<Integer> list = generateBestCase(size);

        for (int i = list.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            // Swap array[i] and array[j]
            int temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);

        }

        return list;
    }

    public static ArrayList<Integer> generateWorstCase(int size) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = size; i > 0; i--) {
            list.add(i);
        }
        return list;
    }
}
