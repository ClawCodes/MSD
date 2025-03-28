import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class MessageHandlerTest {
    @Test
    public void testClientAuthentication() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        KeyManager clientManager = KeyManager.fromClient();
        DiffieHellman diffieHellman = new DiffieHellman();
        BigInteger secretA = diffieHellman.generateSecret();
        BigInteger secretB = diffieHellman.generateSecret();

        assertNotEquals(secretA, secretB);

        BigInteger publicA = diffieHellman.generatePublicKey(secretA);
        BigInteger publicB = diffieHellman.generatePublicKey(secretB);

        BigInteger sharedSecretA = diffieHellman.generateSharedSecret(secretA, publicB);
        BigInteger sharedSecretB = diffieHellman.generateSharedSecret(secretB, publicA);

        assertEquals(sharedSecretA, sharedSecretB);
        Nonce nonce = new Nonce();
        HKDF clientGen = HKDF.createFromClient(nonce.generate(), publicB);

        MessageHandler handler = new MessageHandler(clientGen.getMacKey());

        // Build up message history for hash
        String m1 = "msg1";
        String m2 = "msg2";
        handler.rsaEncrypt(m1.getBytes(), clientManager.getrsaPrivateKey());
        handler.rsaEncrypt(m2.getBytes(), clientManager.getrsaPrivateKey());
        byte[] history = handler.getClientHistory();
        byte[] expectedHash = handler.getServerHistory();

        byte[] cipherText = handler.rsaEncrypt(history, clientManager.getrsaPrivateKey());


        byte[] decryptedText = handler.rsaDecrypt(cipherText, clientManager.getRsaPublicKey().getPublicKey());

        assertArrayEquals(expectedHash, decryptedText);
    }
}