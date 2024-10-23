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

    public void connectionHandler(Socket socket) {
        byte[] response;
        HTTPRequest requestHandler = new HTTPRequest();
        HTTPResponse responseHandler = new HTTPResponse();
        try {
            String fileName = requestHandler.getResourceName(socket);
            response = responseHandler.fetchResource(fileName);
        } catch (IOException e) {
            response = responseHandler.create404().getBytes();
        }
        responseHandler.sendResponse(socket, response);
    }

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
