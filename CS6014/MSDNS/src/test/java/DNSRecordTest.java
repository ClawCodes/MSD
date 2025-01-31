import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DNSRecordTest {
    @Test
    public void testDecodeMessage(){
        byte[] domainUnderTest = new byte[] {
                (byte)0x03, (byte)0x77, (byte)0x77, (byte)0x77, // "3www"
                (byte)0x04, (byte)0x74, (byte)0x65, (byte)0x73, (byte)0x74, // "4test"
                (byte)0x03, (byte)0x63, (byte)0x6F, (byte)0x6D, // "3com"
                (byte)0x00, // termination
                (byte)0x03, (byte)0x77, // type
        };
    }
}