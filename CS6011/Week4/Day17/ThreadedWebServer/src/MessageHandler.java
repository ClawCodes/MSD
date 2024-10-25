import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MessageHandler {
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
