import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        int[] integers = new int[10];

        for (int i=0; i<integers.length; i++) {
            integers[i] = i;
            System.out.println(integers[i]);
        }

        System.out.println(Arrays.stream(integers).sum());

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your name and age: ");
        String name = sc.nextLine();
        int age = sc.nextInt();
//        iGen, Millennial, Gen X, Boomer, Greatest Generation
        if (age >= 18){
            System.out.println("You can vote!");
        }
        if (age <= 122 && age >= 96){
            System.out.println("You are part of the Greatest generation.");
        } else if (age > 60) {
            System.out.println("You are part of the Baby Boomers.");
        } else if (age > 40){
            System.out.println("You are part of generation X.");
        } else if (age > 28){
            System.out.println("You are a Millenial.");
        } else if (age > 12){
            System.out.println("You are part of iGen.");
        }
    }
}