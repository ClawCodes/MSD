import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class KeyManager {
    Certificate CACert_;
    PrivateKey rsaPrivateKey_;
    Certificate rsaPublicKey_;
    BigInteger DHPrivateKey_;
    BigInteger DHPublicKey_;
    BigInteger DHShared_;
    DiffieHellman DHGenerator_;

    private static final String certPath_ = "certs/";

    private KeyManager(String privateRSA, String publicRSA) {
        try {
            CACert_ = readCertificate("CAcertificate.pem");
            rsaPrivateKey_ = readPrivateKey(privateRSA);
            rsaPublicKey_ = readCertificate(publicRSA);
            DHGenerator_ = new DiffieHellman();
            DHPrivateKey_ = DHGenerator_.generateSecret();
            DHPublicKey_ = DHGenerator_.generatePublicKey(DHPrivateKey_);
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

    public Certificate readCertificate(String file) throws FileNotFoundException, CertificateException {
        InputStream in = new FileInputStream(certPath_ + file);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        return certFactory.generateCertificate(in);
    }

    public void setDHSharedKey(byte[] key){
        DHShared_ = DHGenerator_.generateSharedSecret(DHPrivateKey_, new BigInteger(1, key));
    }

    public BigInteger getDHShared(){
        return DHShared_;
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
