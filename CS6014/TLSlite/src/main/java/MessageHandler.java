import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.ArrayList;

public class MessageHandler {
    MessageDigest md_;
    Mac hmac_;
    Cipher RSAcipher_;
    Cipher TLScipher_;
    Mac otherHmac_;
    int historySize_;
    ArrayList<byte[]> sent_;
    ArrayList<byte[]> received_;
    boolean isClient_;

    MessageHandler(boolean isClient){
        historySize_ = 0;
        sent_ = new ArrayList<>();
        received_ = new ArrayList<>();
        isClient_ = isClient;
    }

    public void initCipher(SecretKey macKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        md_ = MessageDigest.getInstance("SHA-256");
        hmac_ = Mac.getInstance("HmacSHA256");
        hmac_.init(macKey);
        RSAcipher_ = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        TLScipher_ = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    public void setOtherMAC(SecretKey macKey) throws NoSuchAlgorithmException, InvalidKeyException {
        otherHmac_ = Mac.getInstance("HmacSHA256");
        otherHmac_.init(macKey);
    }

    byte[] getHMAC(byte[] message){
        return hmac_.doFinal(message);
    }

    public byte[] getHistory(){
        ByteBuffer buffer = ByteBuffer.allocate(historySize_);
        ArrayList<byte[]> sent = sent_;
        ArrayList<byte[]> rec = received_;
        if (!isClient_){
            sent = received_;
            rec = sent_;
        }
        for (byte[] arr : sent){
            buffer.put(arr);
        }
        for (byte[] arr : rec){
            buffer.put(arr);
        }
        return buffer.array();
    }

    public byte[] getHmac(){
        return getHMAC(getHistory());
    }

    public byte[] getOtherHmac(){
        return otherHmac_.doFinal(getHistory());
    }

    public byte[] tlsEncrypt(byte[] plaintext, SecretKey key, IvParameterSpec iv) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        TLScipher_.init(Cipher.ENCRYPT_MODE, key, iv);
        return TLScipher_.doFinal(plaintext);
    }

    public byte[] tlsDecrypt(byte[] ciphertext, SecretKey key, IvParameterSpec iv) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        TLScipher_.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plaintext = TLScipher_.doFinal(ciphertext);
        return plaintext;
    }

    public void send(Socket socket, Object message) throws IOException {
        byte[] messageBytes = message.toString().getBytes();
        sent_.add(messageBytes);
        historySize_ += messageBytes.length;
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message);
        oos.flush();
    }

    public Object receive(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        Object obj = in.readObject();
        byte[] objBytes = obj.toString().getBytes();
        received_.add(objBytes);
        historySize_ += objBytes.length;
        return obj;
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }
}
