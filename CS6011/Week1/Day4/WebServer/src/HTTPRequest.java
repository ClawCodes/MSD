import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class HTTPRequest {
    public static String parseRequest(Scanner scanner) {
        return scanner.nextLine().split(" ")[1];
    }

    public static String getResourceName(Socket socket) throws IOException {
        try {
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            return HTTPRequest.parseRequest(scanner);
        } catch (IOException e) {
            System.out.println("Error when attempting to handle request: ");
            e.printStackTrace();
            throw e;
        }
    }
}
