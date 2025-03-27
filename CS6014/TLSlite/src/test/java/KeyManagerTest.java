import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class KeyManagerTest {
    @Test
    public void testKeyManager() {
        KeyManager clientManager = KeyManager.fromClient();
        KeyManager serverManager = KeyManager.fromServer();

        assertEquals(clientManager.getCACert(), serverManager.getCACert());
        assertNotEquals(clientManager.getrsaPrivateKey(), serverManager.getrsaPrivateKey());
        assertNotEquals(clientManager.getRsaPublicKey(), serverManager.getRsaPublicKey());
    }
}