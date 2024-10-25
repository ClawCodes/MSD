import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class WebSocketHandler extends MessageHandler {
    // TODO:
    // Parse WS message
    // Crete WS message
    // Keep socket alive
    WebSocketHandler(Socket socket) {
        super(socket);
    }

    // TODO: implement pong response
    public void pong() {
    }

    public static String readFrame(DataInputStream inputStream) {
        try {
            byte[] header = inputStream.readNBytes(2);

            boolean fin = (header[0] & 0x80) > 0; // This should always be zero for us
            int opcode = header[0] & 0x7F;
            if (opcode == 0x8) {
                throw new Exception("connection closed");
            }

            boolean masked = (header[1] & 0x80) != 0;

            long len = header[1] & 0x7F;

            if (len == 126) {
                len = inputStream.readShort();
            }

            if (len == 127) {
                len = inputStream.readInt();
            }

            byte[] mask = new byte[4];
            if (masked){
                mask = inputStream.readNBytes(4);
            }
            byte[] message = inputStream.readNBytes((int) len);

            if (masked) {
                for (int i = 0; i < len; i++) {
                    message[i] ^= mask[i % 4];
                }
            }

            return new String(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void keepAlive() throws IOException {
        DataInputStream inStream = new DataInputStream(socket_.getInputStream());
        while (true) {
            if (inStream.available() > 0) {
                String message = readFrame(inStream);

            }

        }
    }
}
