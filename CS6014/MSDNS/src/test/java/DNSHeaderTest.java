import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class DNSHeaderTest {

    @Test
    public void testDecodeHeader() throws IOException {
        byte[] inputHeader = new byte[] {
                (byte)0x12, (byte)0x34, // ID
                (byte)0xAD, // QR:1 OPCode: 0101 AA:1 TC: 0 RD: 1
                (byte)0x61 //  RA:1 Z:1 AD:0 CD:0 RCODE:0001
                // TODO: START HERE - finish test
        };

        int expectedID = 4660;
        int expectedQR = 1;
        int expectedOPCode = 5;
        int expectedAA = 1;
        int expectedTC = 0;
        int expectedRD = 1;

        ByteArrayInputStream in = new ByteArrayInputStream(inputHeader);
        DNSHeader actual = DNSHeader.decodeHeader(in);
        assertEquals(expectedID, actual.getId());
        assertEquals(expectedQR, actual.getQR());
        assertEquals(expectedOPCode, actual.getOpCode());
        assertEquals(expectedAA, actual.getAa());
        assertEquals(expectedTC, actual.getTc());
        assertEquals(expectedRD, actual.getRd());
    }
}