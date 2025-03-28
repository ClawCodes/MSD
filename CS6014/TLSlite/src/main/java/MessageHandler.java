import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.ArrayList;

public class MessageHandler {
    MessageDigest md_;
    Mac hmac_;
    Cipher RSAcipher_;
    Cipher TLScipher_;
    int historySize_ = 0;
    ArrayList<byte[]> sent_ = new ArrayList<>();
    ArrayList<byte[]> received_ = new ArrayList<>();

    MessageHandler(SecretKey macKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        md_ = MessageDigest.getInstance("SHA-256");
        hmac_ = Mac.getInstance("HmacSHA256");
        hmac_.init(macKey);
        RSAcipher_ = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        TLScipher_ = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    byte[] hashMessage(byte[] message){
        return md_.digest(message);
    }

    byte[] getHMAC(byte[] message){
        return hmac_.doFinal(message);
    }

    private byte[] getHistory(boolean fromClient){
        ByteBuffer buffer = ByteBuffer.allocate(historySize_);
        ArrayList<byte[]> sent = sent_;
        ArrayList<byte[]> rec = received_;
        if (!fromClient){
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

    byte[] getClientHistory(){
        return hashMessage(getHistory(true));
    }

    byte[] getServerHistory(){
        return hashMessage(getHistory(false));
    }

    byte[] rsaEncrypt(byte[] plaintext, PrivateKey key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        sent_.add(plaintext);
        historySize_ += plaintext.length;
        RSAcipher_.init(Cipher.ENCRYPT_MODE, key);
      return RSAcipher_.doFinal(plaintext);
    }

    byte[] rsaDecrypt(byte[] ciphertext, PublicKey key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        RSAcipher_.init(Cipher.DECRYPT_MODE, key);
        byte[] plaintext = RSAcipher_.doFinal(ciphertext);
        received_.add(plaintext);
        historySize_ += plaintext.length;
        return plaintext;
    }

    byte[] tlsEncrypt(byte[] plaintext, SecretKey key, IvParameterSpec iv) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        sent_.add(plaintext);
        historySize_ += plaintext.length;
        TLScipher_.init(Cipher.ENCRYPT_MODE, key, iv);
        return TLScipher_.doFinal(plaintext);
    }

    byte[] tlsDecrypt(byte[] ciphertext, SecretKey key, IvParameterSpec iv) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        TLScipher_.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plaintext = TLScipher_.doFinal(ciphertext);
        received_.add(plaintext);
        historySize_ += plaintext.length;
        return plaintext;
    }
}
