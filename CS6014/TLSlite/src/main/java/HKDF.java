import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HKDF {
    private SecretKey encryptionKey_;
    private SecretKey macKey_;
    private IvParameterSpec initVector_;
    Mac mac_;

    private HKDF(SecretKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        mac_ = Mac.getInstance("HmacSHA256");
        mac_.init(key);
    }

    private byte[] hkdfExpand(byte[] data, String tag){
        byte[] tagBytes = tag.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(data.length + tagBytes.length);
        buffer.put(data);
        buffer.put(tagBytes);
        byte[] macBytes = mac_.doFinal(buffer.array());
        return Arrays.copyOfRange(macBytes, 0, 16);
    }

    private void generateKeys(byte[] nonce, String tag){
        byte[] prk = mac_.doFinal(nonce);
        encryptionKey_ = new SecretKeySpec(hkdfExpand(prk, tag + " encrypt"), "RawBytes");
        macKey_ = new SecretKeySpec(hkdfExpand(encryptionKey_.getEncoded(), tag + " MAC"), "RawBytes");
        initVector_ = new IvParameterSpec(hkdfExpand(macKey_.getEncoded(), tag + " IV"));
    }

    private static HKDF create(byte[] nonce, BigInteger sharedDHSecret, String tag) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey key = new SecretKeySpec(sharedDHSecret.toByteArray(), "RawBytes");
        HKDF hkdf = new HKDF(key);
        hkdf.generateKeys(nonce, tag);

        return hkdf;
    }

    public static HKDF createFromClient(byte[] nonce, BigInteger sharedDHSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        return HKDF.create(nonce, sharedDHSecret, "client");
    }

    public static HKDF createFromServer(byte[] nonce, BigInteger sharedDHSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        return HKDF.create(nonce, sharedDHSecret, "server");
    }

    public SecretKey getEncryptionKey() {
        return encryptionKey_;
    }

    public SecretKey getMacKey() {
        return macKey_;
    }

    public IvParameterSpec getInitVector() {
        return initVector_;
    }
}
