import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

class DNSHeaderTest {

    @Test
    public void testDecodeHeader() throws IOException {
        byte[] inputHeader = new byte[] {
                (byte)0x12, (byte)0x34, // ID
                (byte)0xAD, // QR:1 OPCode: 0101 AA:1 TC: 0 RD: 1
                (byte)0x61, //  RA:1 Z:1 AD:0 CD:0 RCODE:0001
                (byte)0x12, (byte)0x34,
                (byte)0x56, (byte)0x78,
                (byte)0x11, (byte)0x22,
                (byte)0x33, (byte)0x44,
                // additional bytes beyond header (should not be reader into header)
                (byte)0x11, (byte)0x22,
                (byte)0x33, (byte)0x44,
        };

        ByteArrayInputStream in = new ByteArrayInputStream(inputHeader);
        DNSHeader actual = DNSHeader.decodeHeader(in);
        assertArrayEquals(Arrays.copyOfRange(inputHeader, 0, 12), actual.getHeader());
    }

    @Test
    public void testGetQCount() throws IOException {
        byte[] inputHeader = new byte[] {
                (byte)0x12, (byte)0x34,
                (byte)0xAD, (byte)0x61,
                (byte)0x12, (byte)0x34,
                (byte)0x56, (byte)0x78,
                (byte)0x11, (byte)0x22,
                (byte)0x33, (byte)0x44,
                // additional bytes beyond header (should not be reader into header)
                (byte)0x11, (byte)0x22,
                (byte)0x33, (byte)0x44,
        };

        int expectedQCount = 4660;

        ByteArrayInputStream in = new ByteArrayInputStream(inputHeader);
        DNSHeader actual = DNSHeader.decodeHeader(in);
        assertEquals(expectedQCount, actual.getQCount());
    }

    @Test
    public void testGetRCount() throws IOException {
        byte[] message = new byte[] {
                // header
                (byte)0xC9, (byte)0x39, // ID
                (byte)0x01, (byte)0x20, // QR = 0
                (byte)0x00, (byte)0x01, // one question
                (byte)0x00, (byte)0x00, // no answers
                (byte)0x00, (byte)0x00, // no auth records
                (byte)0x00, (byte)0x01, // one additional record
                // question
                (byte)0x07, (byte)0x65, (byte)0x78, (byte)0x61, (byte)0x6D, (byte)0x70, (byte)0x6C, (byte)0x65, // 7example
                (byte)0x03, (byte)0x63, (byte)0x6F, (byte)0x6D, (byte)0x00, // 3com (with termination)
                (byte)0x00, (byte)0x01, // QTYPE = 1 -> A (IPv4 address)
                (byte)0x00, (byte)0x01, // QCLASS - 1 -> IN (internet)
                (byte)0x00, (byte)0x00
        };
    }
}