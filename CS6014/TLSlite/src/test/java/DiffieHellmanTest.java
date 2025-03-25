import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class DiffieHellmanTest {
    @Test
    void testSharedSecretGeneration() {
        DiffieHellman diffieHellman = new DiffieHellman();
        BigInteger secretA = diffieHellman.generateSecret();
        BigInteger secretB = diffieHellman.generateSecret();

        // Assert secrets are not equivalent
        assertNotEquals(secretA, secretB);

        BigInteger publicA = diffieHellman.generatePublicKey(secretA);
        BigInteger publicB = diffieHellman.generatePublicKey(secretB);

        BigInteger sharedSecretA = diffieHellman.generateSharedSecret(secretA, publicB);
        BigInteger sharedSecretB = diffieHellman.generateSharedSecret(secretB, publicA);

        assertEquals(sharedSecretA, sharedSecretB);
    }
}