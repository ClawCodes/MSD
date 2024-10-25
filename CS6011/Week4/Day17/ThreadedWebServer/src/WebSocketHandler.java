import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Math.pow;

public class WebSocketHandler extends MessageHandler {
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
                // TODO: determine if this is right. The frame says unsigned 16 bit int, but shorts are signed.
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

    public static byte[] createFrame(String payload) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte FIN_Opcode = (byte)0x81;
        outStream.write(FIN_Opcode);

        // TODO: only returns int. Should I ever expected longer text as the frame allows?
        // Java strings can only hold the same amount of chars as Integer.MAX_VALUE
        int payLen = payload.length();

        if (payLen <= 125){
            outStream.write((byte)payLen);
        } else if (payLen <= pow(2, 16) - 1) {
            outStream.write((byte)126);
            outStream.write((short)payLen);
        } else {
            outStream.write((byte)127);
            outStream.write(0); // Write zero first to fill 64 bit extended length
            outStream.write(payLen); // remaining 32 bits
        }

        outStream.write(payload.getBytes());

        return outStream.toByteArray();
    }

    public void keepAlive() throws IOException {
        DataInputStream inStream = new DataInputStream(socket_.getInputStream());
        while (true) {
            if (inStream.available() > 0) {
                String message = readFrame(inStream);

                // TODO: REMOVE DEBUG STATEMENTS
                System.out.println(message);
                sendResponse(createFrame("{\"Hello\": \"from server\"}"));
            }

        }
    }
}
