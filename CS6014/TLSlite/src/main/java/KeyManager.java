import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.Key;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyManager {
    Certificate CACert_;
    PrivateKey rsaPrivateKey_;
    Certificate rsaPublicKey_;
    Key DHPrivateKey_;
    Key DHPublicKey_;
    Key DHShared_;
    private static final String certPath_ = "certs/";

    private KeyManager(String privateRSA, String publicRSA) {
        try {
            CACert_ = readCertificate("CAcertificate.pem");
            rsaPrivateKey_ = readPrivateKey(privateRSA);
            rsaPublicKey_ = readCertificate(publicRSA);
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException("Error raised when reading key files");
        }
    }

    public static KeyManager fromClient(){
        return new KeyManager("clientPrivateKey.der", "CASignedClientCertificate.pem");
    }

    public static KeyManager fromServer(){
        return new KeyManager("serverPrivateKey.der", "CASignedServerCertificate.pem");
    }

    public PrivateKey readPrivateKey(String file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(certPath_ + file));

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    public PublicKey readPublicKey(String file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(certPath_ + file));

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    public Certificate readCertificate(String file) throws FileNotFoundException, CertificateException {
        InputStream in = new FileInputStream(certPath_ + file);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        return certFactory.generateCertificate(in);
    }

    Certificate getCACert() {
        return CACert_;
    }

    Certificate getRsaPublicKey() {
        return rsaPublicKey_;
    }

    PrivateKey getrsaPrivateKey() {
        return rsaPrivateKey_;
    }
}
