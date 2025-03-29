import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Arrays;

public class Handshake {

    public static class ClientHello implements Serializable {
        private final byte[] nonce;

        ClientHello(byte[] nonce) {
            this.nonce = nonce;
        }

        @Override
        public String toString() {
            return Arrays.toString(nonce);
        }
    }

    public static class CertHello implements Serializable {
        private Certificate publicCert;
        private BigInteger DHPublicKey;
        private byte[] signature;

        CertHello(Certificate publicCert, BigInteger DHPublicKey, byte[] signature) {
            this.publicCert = publicCert;
            this.DHPublicKey = DHPublicKey;
            this.signature = signature;
        }

        public void verify(PublicKey publicKey) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
            publicCert.verify(publicKey);
        }

        public Certificate getPublicCert() {
            return publicCert;
        }
        public BigInteger getDHPublicKey() {
            return DHPublicKey;
        }

        @Override
        public String toString() {
            return publicCert.toString() + DHPublicKey.toString() + Arrays.toString(signature);
        }
    }

    public static HKDF clientInit(Socket socket, MessageHandler handler) throws IOException, ClassNotFoundException, CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException, NoSuchPaddingException {
        Nonce nonce = new Nonce();
        ClientHello hello = new ClientHello(nonce.generate());
        handler.send(socket, hello);

        KeyManager manager = KeyManager.fromClient();

        CertHello servHello = (CertHello) handler.receive(socket);
        servHello.verify(manager.getCACert().getPublicKey());

        System.out.println("Verified server identity");

        DiffieHellman dh = new DiffieHellman();
        BigInteger dhSecret = dh.generateSecret();
        BigInteger dhPublic = dh.generatePublicKey(dhSecret);
        CertHello clientCertHello = new CertHello(manager.getCACert(), dhPublic, MessageHandler.sign(dhPublic.toByteArray(), manager.getrsaPrivateKey()));
        handler.send(socket, clientCertHello);

        BigInteger masterSecret = dh.generateSharedSecret(dhSecret, servHello.getDHPublicKey());
        HKDF keyGen = new HKDF(hello.nonce, masterSecret);
        handler.initCipher(keyGen.getClientMACKey());
        handler.setOtherMAC(keyGen.getServerMACKey());

        byte[] serverHmacExpected = handler.getOtherHmac();
        ByteMessage servHmac = (ByteMessage) handler.receive(socket);
        // Assert the HMAC sent from server is equivalent to history computed on client side
        assert (Arrays.equals(servHmac.getData(), serverHmacExpected));

        handler.send(socket, new ByteMessage(handler.getHmac()));
        System.out.println("Client-side handshake complete!");

        return keyGen;
    }

    public static HKDF serverInit(Socket socket, MessageHandler handler) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateException, NoSuchProviderException, NoSuchPaddingException {
        ClientHello clientHello = (ClientHello) handler.receive(socket);

        KeyManager manager = KeyManager.fromServer();
        DiffieHellman dh = new DiffieHellman();
        BigInteger dhSecret = dh.generateSecret();
        BigInteger dhPublic = dh.generatePublicKey(dhSecret);
        CertHello servHello = new CertHello(manager.getCACert(), dhPublic, MessageHandler.sign(dhPublic.toByteArray(), manager.getrsaPrivateKey()));
        handler.send(socket, servHello);

        CertHello clientResp = (CertHello) handler.receive(socket);
        clientResp.verify(manager.getCACert().getPublicKey());

        System.out.println("Verified Client Identity.");

        BigInteger masterSecret = dh.generateSharedSecret(dhSecret, clientResp.getDHPublicKey());
        HKDF keyGen = new HKDF(clientHello.nonce, masterSecret);

        handler.initCipher(keyGen.getServerMACKey());
        handler.send(socket, new ByteMessage(handler.getHmac()));
        handler.setOtherMAC(keyGen.getClientMACKey());

        byte[] clientHmacExpected = handler.getOtherHmac();
        ByteMessage clientHmac = (ByteMessage) handler.receive(socket);

        // Assert HMACs are equivalent between server and client
        assert (Arrays.equals(clientHmac.getData(), clientHmacExpected));
        System.out.println("Server-side handshake complete!");
        return keyGen;
    }
}
