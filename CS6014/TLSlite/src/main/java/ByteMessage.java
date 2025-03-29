import java.io.Serializable;
import java.util.Arrays;

public class ByteMessage implements Serializable {
    private final byte[] data;

    public ByteMessage(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
