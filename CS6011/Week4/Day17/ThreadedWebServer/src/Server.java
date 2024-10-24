import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;

    Server(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Server failed to open port: " + port);
            throw e;
        }
    }

    void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ConnectionHandler(socket));
                thread.start();
//                thread.join(); // TODO: determine if this is needed after the websocket is closed
            } catch (IOException e) {
                System.out.println("Unable to establish port connection.");
            }
        }
    }
}
