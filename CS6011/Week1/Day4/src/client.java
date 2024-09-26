import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws IOException {
        // Client needs a socket with the IP address we want to connect to and the port number the server lives on
        Socket socket = new Socket("localhost", 8080);

        InputStream inputStream = socket.getInputStream();
        Scanner serverIn = new Scanner(inputStream);
        System.out.println(serverIn.nextLine());

        Scanner scannerIn = new Scanner(System.in);

        String message = scannerIn.nextLine();

        // Need to convert to bytes for sending data
        socket.getOutputStream().write((message+"\n").getBytes());
    }
}
