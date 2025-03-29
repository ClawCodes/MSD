import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int port_ = 8443;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port_)) {
            System.out.println("Server started on port " + port_);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                MessageHandler handler = new MessageHandler(false);
                Handshake.serverInit(clientSocket, handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
