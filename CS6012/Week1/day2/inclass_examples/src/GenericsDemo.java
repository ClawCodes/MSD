import java.awt.*;

public class GenericsDemo {
    public static <T> T getElement(T[] arr, int i) {
        if (i<0 || i>=arr.length){
            throw new ArrayIndexOutOfBoundsException();
        }
        return arr[i];
    }

    public static void main(String[] args) {
//        int[] ints = new int[]{1,2,3,4}; // Cannot do this with integers, you need to use Integer class
        Integer[] ints = new Integer[]{1,2,3,4};
        Point[] points = new Point[]{new Point(0,0), new Point(1,1)};

        System.out.println(getElement(ints, 0));
        System.out.println(getElement(points, 0));
    }
}
