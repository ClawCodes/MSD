// Scratch file for in class examples

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create a server

        // Accept requests from clients
        ServerSocket serverSocket = new ServerSocket(8080); // Specifies the port where the server is being run

        // Wrap with while true to keep the server up
        while(true){
            Socket socket = serverSocket.accept(); // Listens to requests on port 8080

            // View client information which is communication with this server
            System.out.println(socket.getInetAddress());
            System.out.println(socket.getPort());

            // Write a message to the client
//            OutputStream outputStream = socket.getOutputStream();
            socket.getOutputStream().write("Welcome Client!".getBytes());

            // Recieve message
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            System.out.println(scanner.nextLine());
        }
    }
}