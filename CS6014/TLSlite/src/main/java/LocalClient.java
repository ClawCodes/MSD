import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class LocalClient {
    private static final String server_host_ = "localhost";
    private static final Integer server_port_ = 8443;

    public static void sendEncryptedMsg(String msg, MessageHandler handler, HKDF keys, Socket socket) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] cipherText = handler.tlsEncrypt(msg.getBytes(), keys.getClientEncKey(), keys.getClientInitVector());
        handler.send(socket, cipherText);
    }

    public static String receiveEnctryptedMsg(MessageHandler handler, HKDF keys, Socket socket) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, ClassNotFoundException {
        ByteMessage obj = (ByteMessage) handler.receive(socket);
        byte[] dectyped = handler.tlsDecrypt(obj.getData(), keys.getServerEncKey(), keys.getServerInitVector());
        return new String(dectyped);
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket(LocalClient.server_host_, LocalClient.server_port_)) {
            System.out.println("Connected to TLS server at " + LocalClient.server_host_ + ":" + LocalClient.server_port_);
            MessageHandler handler = new MessageHandler(true);
            HKDF keys = Handshake.clientInit(socket, handler);

            String msgOne = receiveEnctryptedMsg(handler, keys, socket);
            System.out.println("FROM SERVER: " + msgOne);

            String hello = "Hello Server";
            sendEncryptedMsg(hello, handler, keys, socket);

            String msgtwo = receiveEnctryptedMsg(handler, keys, socket);
            System.out.println("FROM SERVER: " + msgtwo);

            String goodbye = "Goodbye Server";
            sendEncryptedMsg(goodbye, handler, keys, socket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
