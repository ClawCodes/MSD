import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class HKDFTest {
    @Test
    void testHKDF() throws NoSuchAlgorithmException, InvalidKeyException {
        // Diffie-hellman
        DiffieHellman diffieHellman = new DiffieHellman();
        BigInteger secretA = diffieHellman.generateSecret();
        BigInteger secretB = diffieHellman.generateSecret();

        assertNotEquals(secretA, secretB);

        BigInteger publicA = diffieHellman.generatePublicKey(secretA);
        BigInteger publicB = diffieHellman.generatePublicKey(secretB);

        BigInteger sharedSecretA = diffieHellman.generateSharedSecret(secretA, publicB);
        BigInteger sharedSecretB = diffieHellman.generateSharedSecret(secretB, publicA);

        assertEquals(sharedSecretA, sharedSecretB);

        // HKDF generation
        Nonce nonceA = new Nonce();
        Nonce nonceB = new Nonce();

        assertNotEquals(nonceA, nonceB);

        HKDF clientGen = HKDF.createFromClient(nonceA.generate(), sharedSecretA);
        HKDF serverGen = HKDF.createFromServer(nonceB.generate(), sharedSecretA);

        assertNotEquals(clientGen.getEncryptionKey_(), serverGen.getEncryptionKey_());
        assertNotEquals(clientGen.getMacKey_(), serverGen.getMacKey_());
        assertNotEquals(clientGen.getInitVector_(), serverGen.getInitVector_());
    }
}