import java.awt.*;

public class MyContainer<T> {
//    private T[] storage = new T[10]; // Cannot create array of type T
    // Instead you must cast an ojbect array to T[]
    private T[] storage = (T[]) new Object[10];
    int size = 0;

    public T getElement(int i) throws IndexOutOfBoundsException{
        if (i<0 || i>=storage.length){
            throw new IndexOutOfBoundsException();
        }
        return storage[i];
    }

    public void addElement(T element) {
        storage[size]=element;
        size++;
    }

    public static void main(String[] args) {
        MyContainer<Point> points = new MyContainer<>();
        points.addElement(new Point(0,0));
        points.addElement(new Point(1,1));
//        points.addElement("hello"); // This is wrong because line 22 declares T to be Point
    }
}
