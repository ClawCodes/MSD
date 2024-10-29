import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class to launch and manage chat server.
 * <p>
 * Example usage:
 * Server s = new Server(8080);
 * s.start() // Keep server alive
 *
 */
public class Server {
    ServerSocket serverSocket;

    /**
     * Create Server object with active ServerSocket
     * @param port: port to run server on
     * @throws IOException
     */
    Server(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Server failed to open port: " + port);
            throw e;
        }
    }

    /**
     * Start the server and keep it alive
     */
    void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ConnectionHandler(socket));
                thread.start();
            } catch (IOException e) {
                System.out.println("Unable to establish port connection.");
            }
        }
    }
}
