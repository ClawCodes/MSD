import java.nio.ByteBuffer;
import java.util.Arrays;

public class BitHelper {
    public static int bytePairToInt(byte[] bytes) {
        if (bytes.length != 2){
            throw new IllegalArgumentException("Byte array must only contain two elements.");
        }
        int mask = 0;
        mask |= (bytes[0] << 8);
        mask |= bytes[1];
        return mask;
    }

    public static int getBits(int b, int start, int end ){
        int mask = 1;
        for (int i = start; i < end; i++){
            mask = (mask << 1) | 1;
        }
        mask = mask << (7 - end);
        return (b & mask) >> (7 - end);
    }

    public static byte[] intToByteArray(int value, int keep){
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(value);
        return Arrays.copyOfRange(buffer.array(), 4 - keep, 4);
    }
}
