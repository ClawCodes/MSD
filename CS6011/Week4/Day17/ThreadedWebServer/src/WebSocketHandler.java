import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.spec.InvalidParameterSpecException;

import static java.lang.Math.pow;


/**
 * Class which manages the creation, maintenance, and communication of a web socket
 */
public class WebSocketHandler extends MessageHandler {
    /**
     * Constructor
     * @param socket the socket used to communicate to a given client
     */
    WebSocketHandler(Socket socket) {
        super(socket);
    }

    /**
     * Read a web socket message that is received from a client
     *
     * @param inputStream The inputStream to read the message from
     * @return The web socket message as a string
     * @throws Exception
     */
    public static String readFrame(DataInputStream inputStream) throws Exception {
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
            if (masked) {
                mask = inputStream.readNBytes(4);
            }
            byte[] message = inputStream.readNBytes((int) len);

            if (masked) {
                for (int i = 0; i < len; i++) {
                    message[i] ^= mask[i % 4];
                }
            }

            return new String(message);
    }

    /**
     * Create a web socket message to send to a client
     * @param payload The message content
     * @param opcode The opcode to include in the constructed frame
     * @return The web socket from to send to the client as an array of bytes
     * @throws IOException
     */
    public static byte[] createFrame(String payload, OpCode opcode) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte FIN_Opcode = (byte) (0x80 | opcode.getValue());
        outStream.write(FIN_Opcode);

        // NOTE: Java strings can only hold the same amount of chars as Integer.MAX_VALUE (i.e. 32 bits)
        int payLen = payload.length();

        if (payLen <= 125) {
            outStream.write((byte) payLen);
        } else if (payLen <= pow(2, 16) - 1) {
            outStream.write((byte) 126);
            outStream.write((short) payLen);
        } else {
            outStream.write((byte) 127);
            outStream.write(0); // Write zero first to fill 64 bit extended length
            outStream.write(payLen); // remaining 32 bits
        }

        outStream.write(payload.getBytes());

        return outStream.toByteArray();
    }


    /**
     * Send a message to a client via a web socket message
     * This method converts the payload into a web socket frame and sends the message to the client.
     * @param payload The message to send
     * @param opcode The opcode to add to the frame
     */
    public void sendPayload(String payload, OpCode opcode) {
        try {
            sendResponse(createFrame(payload, opcode));
        } catch (IOException e) {
            System.out.println("Failed to send payload.");
            e.printStackTrace();
        }
    }

    /**
     * Send a websocket message containing text to a client
     * @param text the message to send to the client
     */
    public void sendText(String text) {
        sendPayload(text, OpCode.TEXT);
    }

    /**
     * Close the web socket with the client by sending a frame with the close opcode
     * @param message message to include in the close request
     */
    public void closeConnection(String message) {
        sendPayload(message, OpCode.CLOSE);
    }

    /**
     * Keep the Web socket connection a live and handle client-server communication.
     * This method handles room creation, message sending between clients, and client connections.
     * @throws IOException
     */
    public void keepAlive() throws IOException {
        DataInputStream inStream = new DataInputStream(socket_.getInputStream());
        boolean connected = true;
        while (connected) {
            if (inStream.available() > 0) {
                String message;
                try {
                    message = readFrame(inStream);
                } catch (Exception e){
                    System.out.println("Failed to read frame.");
                    continue;
                }
                String[] splitMsg = message.split(" ", 2);
                switch (splitMsg[0]) { // splitMsg[0] is the type of message
                    case "join":
                        String[] userAndRoom = splitMsg[1].split(" ");
                        setRoom(userAndRoom[1]);
                        try {
                            setUser(userAndRoom[0]);
                            Message jsonMessage = new Message("join", userName_, room_);
                            sendText(jsonMessage.toString());
                        } catch (InvalidParameterSpecException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "leave":
                        closeConnection("Closing connection from client request.");
                        connected = false;
                        break;
                    case "message":
                        Message jsonMessage = new Message("message", userName_, room_, splitMsg[1]);
                        RoomManager.sendMessage(room_, jsonMessage.toString());
                        break;
                }
            }

        }
    }

    public void setUser(String name) throws InvalidParameterSpecException {
        if (userName_ == null) {
            userName_ = name;
        } else {
            throw new InvalidParameterSpecException("The connection's user name has already been set!");
        }
    }

    public void setRoom(String room) {
        room_ = room;
        RoomManager.addRoom(room, this);
    }

    String room_;
    String userName_ = null;
}
