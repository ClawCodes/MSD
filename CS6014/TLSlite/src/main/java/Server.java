import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class Server {
    private static final int port_ = 8443;

    public static void sendEncryptedMsg(String msg, MessageHandler handler, HKDF keys, Socket socket) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] cipherText = handler.tlsEncrypt(msg.getBytes(), keys.getServerEncKey(), keys.getServerInitVector());
        handler.send(socket, new ByteMessage(cipherText));
    }

    public static String receiveEnctryptedMsg(MessageHandler handler, HKDF keys, Socket socket) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, ClassNotFoundException {
        byte[] obj = (byte[]) handler.receive(socket);
        byte[] dectyped = handler.tlsDecrypt(obj, keys.getClientEncKey(), keys.getClientInitVector());
        return new String(dectyped);
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port_)) {
            System.out.println("Server started on port " + port_);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                MessageHandler handler = new MessageHandler(false);
                HKDF keys = Handshake.serverInit(clientSocket, handler);

                String hello = "Hello Client";
                sendEncryptedMsg(hello, handler, keys, clientSocket);

                String msgOne = receiveEnctryptedMsg(handler, keys, clientSocket);
                System.out.println("FROM CLIENT: " + msgOne);

                String goodbye = "Goodbye Client";
                sendEncryptedMsg(goodbye, handler, keys, clientSocket);

                String msgtwo = receiveEnctryptedMsg(handler, keys, clientSocket);
                System.out.println("FROM CLIENT: " + msgtwo);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
