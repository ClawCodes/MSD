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
    private SecretKey clientEncKey_;
    private SecretKey serverEncKey_;
    private SecretKey clientMACKey_;
    private SecretKey serverMACKey_;
    private IvParameterSpec clientInitVector_;
    private IvParameterSpec serverInitVector_;
    Mac mac_;



    private byte[] hkdfExpand(byte[] data, String tag){
        byte[] tagBytes = tag.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(data.length + tagBytes.length);
        buffer.put(data);
        buffer.put(tagBytes);
        byte[] macBytes = mac_.doFinal(buffer.array());
        return Arrays.copyOfRange(macBytes, 0, 16);
    }

    private void generateKeys(byte[] nonce){
        byte[] prk = mac_.doFinal(nonce);
        serverEncKey_ = new SecretKeySpec(hkdfExpand(prk, "server encrypt"), "AES");
        clientEncKey_ = new SecretKeySpec(hkdfExpand(serverEncKey_.getEncoded(), "client encrypt"), "AES");
        serverMACKey_ = new SecretKeySpec(hkdfExpand(clientEncKey_.getEncoded(), "server MAC"), "HmacSHA256");
        clientMACKey_ = new SecretKeySpec(hkdfExpand(serverMACKey_.getEncoded(), "client MAC"), "HmacSHA256");
        serverInitVector_ = new IvParameterSpec(hkdfExpand(clientMACKey_.getEncoded(), "server IV"));
        clientInitVector_ = new IvParameterSpec(hkdfExpand(clientMACKey_.getEncoded(), "client IV"));
    }

    public HKDF(byte[] nonce, BigInteger sharedDHSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey key = new SecretKeySpec(sharedDHSecret.toByteArray(), "RawBytes");
        mac_ = Mac.getInstance("HmacSHA256");
        mac_.init(key);
        generateKeys(nonce);
    }

    public SecretKey getClientEncKey() {
        return clientEncKey_;
    }
    public SecretKey getServerEncKey() {
        return serverEncKey_;
    }
    public SecretKey getClientMACKey() {
        return clientMACKey_;
    }
    public SecretKey getServerMACKey() {
        return serverMACKey_;
    }
    public IvParameterSpec getClientInitVector() {
        return clientInitVector_;
    }
    public IvParameterSpec getServerInitVector() {
        return serverInitVector_;
    }
}
