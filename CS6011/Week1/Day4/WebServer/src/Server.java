import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Server {
    ServerSocket serverSocket;

    private static String failureResponse = """
            HTTP/1.1 404 Not Found
            Connection: close\r\n
            """;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static String parseRequest(Scanner scanner) throws IOException {
        if (!scanner.hasNextLine()) {
            throw new IOException("No request provided");
        }
        return scanner.nextLine().split(" ")[1];
    }

    public static String createSuccessResponse(String resource) {
        String reponse = """
                HTTP/1.1 200 OK
                Content-Type: text/html
                Connection: close\r\n
                """ + resource;
        return reponse;
    }

    public static String readFile(String fileName) throws IOException {
        if (fileName.equals("/"))
            fileName = "/index.html";

        return Files.readString(Paths.get("resources" + fileName));
    }

    void start() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);

            String response;
            try {
                String fileName = parseRequest(scanner);
                String fileContent = readFile(fileName);
                response = createSuccessResponse(fileContent);

            } catch (IOException e) {
                response = failureResponse;
            }

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(response.getBytes());
            socket.getOutputStream().flush();
            socket.close();
        }
    }
}
