// Scratch file for in class examples


class A{
    // Declaring counter as static essentially makes counter global across all objects
    // Each object incrementing counter increments the same counter
    static int counter = 0;

    public void increment(){
        counter++;
    }

    static void printCount(){
        System.out.println(counter);
    }
}

public class Main {
    public static void main(String[] args)
    {
        A a = new A();
        a.increment();

        A b = new A();
        b.increment();

        A c = new A();
        c.increment();

        A.printCount();
    }
}