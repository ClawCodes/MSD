import java.io.IOException;
import java.net.Socket;

public class LocalClient {
    private static final String server_host_ = "localhost";
    private static final Integer server_port_ = 8443;

    public static void main(String[] args) {
        try (Socket socket = new Socket(LocalClient.server_host_, LocalClient.server_port_)) {
            System.out.println("Connected to TLS server at " + LocalClient.server_host_ + ":" + LocalClient.server_port_);

            // TODO: Implement TLS handshake
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
