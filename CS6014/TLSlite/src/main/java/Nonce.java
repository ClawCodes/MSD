import java.security.SecureRandom;

public class Nonce {
    SecureRandom rand;

    public Nonce(){
        rand = new SecureRandom();
    }

    public byte[] generate(){
        byte[] nonce = new byte[32];
        rand.nextBytes(nonce);

        return nonce;
    }
}
