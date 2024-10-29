import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Class providing functionality to send a message to a connected client with an open socket
 */
public class MessageHandler {
    /**
     * Constructor
     * @param socket The respective client's socket
     */
    MessageHandler(Socket socket) {
        socket_ = socket;
    }

    void closeSocket() {
        try {
            socket_.close();
        } catch (IOException e) {
            System.out.println("Failed to close socket.");
        }
    }

    /**
     * Send a message to a client
     * @param response message to send to client using the provided socket
     */
    void sendResponse(byte[] response) {
        int retry = 0;
        while (retry < 3) {
            try {
                OutputStream outStream = socket_.getOutputStream();
                outStream.write(response);
                outStream.flush();
                break;
            } catch (IOException e) {
                retry++;
            }
        }
    }

    Socket socket_;
}
