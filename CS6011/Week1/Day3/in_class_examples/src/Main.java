// Scratch project for in class examples

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // Writing to file
        File file = new File("file.txt");
        // true changes write mode to append
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write("Hello World");
        fileWriter.close();

        // Reading from file
        // Scanner reads an input byte stream - input file, stdin, etc.
        Scanner scanner = new Scanner(file);
        // Keep reading until the end of file
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
    }
}