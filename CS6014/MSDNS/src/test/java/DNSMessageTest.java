import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class DNSMessageTest {
    @Test
    public void testReadDomainNameNoPointer() throws IOException {
        byte[] domainUnderTest = new byte[] {
                (byte)0x03, (byte)0x77, (byte)0x77, (byte)0x77, // "3www"
                (byte)0x04, (byte)0x74, (byte)0x65, (byte)0x73, (byte)0x74, // "4test"
                (byte)0x03, (byte)0x63, (byte)0x6F, (byte)0x6D, // "3com"
                (byte)0x00 // termination
        };

        String[] expected = new String[] { "www", "test", "com"};

        ByteArrayInputStream in = new ByteArrayInputStream(domainUnderTest);
        DNSMessage message = new DNSMessage(domainUnderTest);
        String[] actual = message.readDomainName(in);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadDomainNameWithPointer() throws IOException {
        byte[] domainUnderTest = new byte[] {
                (byte)0x03, (byte)0x77, (byte)0x77, (byte)0x77, // "3www"
                (byte)0x04, (byte)0x74, (byte)0x65, (byte)0x73, (byte)0x74, // "4test"
                (byte)0x03, (byte)0x63, (byte)0x6F, (byte)0x6D, // "3com"
                (byte)0x00 // termination
        };

        byte[] pointer = new byte[] {
                (byte)0xC0, (byte)0x00 // points to first byte in message
        };

        String[] expected = new String[] { "www", "test", "com"};

        ByteArrayInputStream in = new ByteArrayInputStream(pointer);
        DNSMessage message = new DNSMessage(domainUnderTest);
        String[] actual = message.readDomainName(in);
        assertArrayEquals(expected, actual);
    }
}