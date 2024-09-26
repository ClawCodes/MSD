import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private static String failureResponse = """
            HTTP/1.1 404 Not Found
            Connection: close\r\n
            """;

    public static String parseRequest(String scannerLine) {
        String fileName = scannerLine.split(" ")[1];
        return fileName;
    }

    public static String createSuccessResponse(String resource) {
        String reponse = """
                HTTP/1.1 200 OK
                Content-Type: text/html
                Connection: close\r\n
                """ + resource;
        return reponse;
    }

    public static String readFile(String fileName) throws FileNotFoundException {
        if (fileName.equals("/"))
            fileName = "index.html";

        File file = new File("resources/" + fileName);
        Scanner reader = new Scanner(file);

        String fileContent = reader.nextLine() + "\n";

        while (reader.hasNext()) {
            fileContent = fileContent + reader.nextLine() + "\n";
        }

        reader.close();
        return fileContent;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);

            String fileName = parseRequest(scanner.nextLine());

            try {
                String fileContent = readFile(fileName);
                String response = createSuccessResponse(fileContent);

                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(response.getBytes());
            } catch (IOException e) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(failureResponse.getBytes());
            }

            socket.getOutputStream().flush();
            socket.close();
        }

    }

}