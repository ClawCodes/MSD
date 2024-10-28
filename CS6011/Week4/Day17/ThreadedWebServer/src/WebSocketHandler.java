import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.spec.InvalidParameterSpecException;

import static java.lang.Math.pow;


public class WebSocketHandler extends MessageHandler {
    WebSocketHandler(Socket socket) {
        super(socket);
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] createFrame(String payload, OpCode opcode) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte FIN_Opcode = (byte) (0x80 | opcode.getValue());
//        System.out.println(String.format("%02X ", FIN_Opcode)); // TODO: ensure opcodes manipulated correctly
        outStream.write(FIN_Opcode);

        // TODO: only returns int. Should I ever expected longer text as the frame allows?
        // Java strings can only hold the same amount of chars as Integer.MAX_VALUE
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

    public void sendPayload(String payload, OpCode opcode) {
        try {
            sendResponse(createFrame(payload, opcode));
        } catch (IOException e) {
            System.out.println("Failed to send payload.");
            e.printStackTrace();
        }
    }

    public void sendText(String text) {
        sendPayload(text, OpCode.TEXT);
    }

    public void closeConnection(String message) {
        sendPayload(message, OpCode.CLOSE);
    }

    public void keepAlive() throws IOException {
        DataInputStream inStream = new DataInputStream(socket_.getInputStream());
        boolean connected = true;
        while (connected) {
            if (inStream.available() > 0) {
                String message = readFrame(inStream);
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
                            e.printStackTrace(); // TODO: send message to client that userName is already set? user alert and update on client side
                        }
                        break;
                    case "leave":
                        closeConnection("Closing connection from client request.");
                        connected = false;
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

    public String getUser() {
        return userName_;
    }

    String room_;
    String userName_ = null;
}
