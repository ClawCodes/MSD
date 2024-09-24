//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.util.ArrayList;
import java.util.Scanner;

class Student {
    int gpa;
    String name;

    /**
     * Primary constructor
     *
     * @param gpa  - GPA of student
     * @param name - Name of student
     */
    public Student(int gpa, String name) {
        this.gpa = gpa;
        this.name = name;
    }

    /**
     * Default constructor
     */
    public Student() {
        gpa = 0;
        name = "";
    }

    public int getGpa() {
        return gpa;
    }

    public void setGpa(int gpa) {
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class Main {
    public static void addNumbers(int[] numbers){
        numbers[0] = 3;
    }
    public static void changeNumber(int number){
        number = 3;
    }
    public static void main(String[] args) {

        int originalNumber = 10;

        changeNumber(originalNumber);

        System.out.println("The original number is: " + originalNumber);

        // Create new array. This is different from ArrayList as it is a fixed size (same as vector vs. array in c++)
        int [] numbers = new int[4]; // numbers is on the stack and the array is on the heap
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
            System.out.println(numbers[i]);
        }

        System.out.println("--------------");

        addNumbers(numbers);
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }

        // ArrayList for wrapped primitive
        // This will create a shallow copy -> Create a new heap of pointers which point to the same heap array containing the primitives
        // To do a deep copy, you must use a loop to reset the values
//        ArrayList<Integer> numbersCopy = new ArrayList<Integer>(numbers);


        Student student = new Student();
        student.setName("Chris");

        System.out.println(student.getName());

        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student(1, "John"));
        students.add(new Student(2, "Jane"));
        students.add(new Student(3, "Bob"));
        students.add(new Student(4, "Mary"));

        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i).getName());
        }

        for (Student s : students) {
            System.out.println(s.getGpa());
        }

        // Captures stream of bytes from keyboard and pass to program
        // The scanner does the processing
        Scanner sc = new Scanner(System.in);
        int number = sc.nextInt();
        System.out.println("The user entered number is: " + number);
    }
}