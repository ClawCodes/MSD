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

                String response;
                try {
                    String fileName = HTTPRequest.getResourceName(socket);
                    response = HTTPResponse.fetchResource(fileName);
                } catch (IOException e) {
                    response = HTTPResponse.create404("plain", "Request could not be processed.");
                }
                HTTPResponse.sendResponse(socket, response);

            } catch (IOException e) {
                System.out.println("Unable to establish port connection.");
            }
        }
    }
}
