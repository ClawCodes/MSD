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
    public void testBuildHeaderForResponse() throws IOException {
        byte[] request = {
                (byte) 0x12, (byte) 0x34, (byte) 0x81, (byte) 0x80,  // Transaction ID + Flags
                (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,  // QDCOUNT = 1, ANCOUNT = 0
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,  // NSCOUNT = 0, ARCOUNT = 1

                // Question Section (www.example.com)
                (byte) 0x03, (byte) 0x77, (byte) 0x77, (byte) 0x77,  // "3www"
                (byte) 0x07, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x6C, (byte) 0x65,  // "7example"
                (byte) 0x03, (byte) 0x63, (byte) 0x6F, (byte) 0x6D, (byte) 0x00,  // "3com."

                (byte) 0x00, (byte) 0x01,  // QTYPE = A
                (byte) 0x00, (byte) 0x01,  // QCLASS = IN

                // Additional Section (NS Record: ns1.example.com)
                (byte) 0x03, (byte) 0x6E, (byte) 0x73, (byte) 0x31, (byte) 0xC0, (byte) 0x10,  // "ns1.example.com" (pointer to example.com)
                (byte) 0x00, (byte) 0x02,  // TYPE = NS (0x0002)
                (byte) 0x00, (byte) 0x01,  // CLASS = IN (0x0001)
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3C,  // TTL = 60 seconds (0x3C)
                (byte) 0x00, (byte) 0x06,  // RDLENGTH = 6 bytes
                (byte) 0x03, (byte) 0x6E, (byte) 0x73, (byte) 0x31, (byte) 0xC0, (byte) 0x10   // RDATA = "ns1.example.com" (pointer to example.com)
        };

        DNSMessage requestMsg = DNSMessage.decodeMessage(request);

        byte[] answer = { // Answer Section (A Record for www.example.com -> 93.184.216.34)
                (byte) 0xC0, (byte) 0x0C,  // Pointer to "www.example.com"
                (byte) 0x00, (byte) 0x01,  // TYPE = A
                (byte) 0x00, (byte) 0x01,  // CLASS = IN
                (byte) 0x00, (byte) 0x3C,  // TTL = 60 seconds
                (byte) 0x00, (byte) 0x04,  // RDLENGTH = 4 bytes
                (byte) 0x5D, (byte) 0xB8, (byte) 0xD8, (byte) 0x22,  // RDATA = 93.184.216.34}
        };

        ByteArrayInputStream in = new ByteArrayInputStream(answer);
        DNSRecord answerMsg = DNSRecord.decodeRecord(in, requestMsg);
        DNSMessage responseMsg = new DNSMessage();
        responseMsg.addAnswer(answerMsg);

        DNSHeader responseHeader = DNSHeader.buildHeaderForResponse(requestMsg, responseMsg);
        assertEquals(1, responseHeader.getQCount());
        assertEquals(1, responseHeader.getANCount());
        assertEquals(0, responseHeader.getNSCount());
        assertEquals(1, responseHeader.getARCount());

        byte[] respHeader = responseHeader.getHeader();
        assertEquals(1, BitHelper.getBits(respHeader[2], 0, 0)); // QR set to 1 for response
        assertEquals(BitHelper.getBits(respHeader[3], 4, 7), 0); // RCODE == 0
    }

    @Test
    public void testToString() throws IOException {
        byte[] request = {
                (byte) 0x12, (byte) 0x34, (byte) 0x01, (byte) 0x80,  // Transaction ID + Flags
                (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,  // QDCOUNT = 1, ANCOUNT = 0
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,  // NSCOUNT = 0, ARCOUNT = 1

                // Question Section (www.example.com)
                (byte) 0x03, (byte) 0x77, (byte) 0x77, (byte) 0x77,  // "3www"
                (byte) 0x07, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x6C, (byte) 0x65,  // "7example"
                (byte) 0x03, (byte) 0x63, (byte) 0x6F, (byte) 0x6D, (byte) 0x00,  // "3com."

                (byte) 0x00, (byte) 0x01,  // QTYPE = A
                (byte) 0x00, (byte) 0x01,  // QCLASS = IN

                // Additional Section (NS Record: ns1.example.com)
                (byte) 0x03, (byte) 0x6E, (byte) 0x73, (byte) 0x31, (byte) 0xC0, (byte) 0x10,  // "ns1.example.com" (pointer to example.com)
                (byte) 0x00, (byte) 0x02,  // TYPE = NS (0x0002)
                (byte) 0x00, (byte) 0x01,  // CLASS = IN (0x0001)
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3C,  // TTL = 60 seconds (0x3C)
                (byte) 0x00, (byte) 0x06,  // RDLENGTH = 6 bytes
                (byte) 0x03, (byte) 0x6E, (byte) 0x73, (byte) 0x31, (byte) 0xC0, (byte) 0x10   // RDATA = "ns1.example.com" (pointer to example.com)
        };
        String expected = "ID: 4660\nQR: 0\nOpCode: 0\nAA: 0\nTC: 0\nRD: 1\nRA: 1\nZ: 0\nRCODE: 0\n" +
                "QDCOUNT: 1\nANCOUNT: 0\nNSCOUNT: 0\nARCOUNT: 1\n";
        DNSMessage msg = DNSMessage.decodeMessage(request);
        DNSHeader header = msg.getHeader();
        String actual = header.toString();
        assertEquals(expected, actual);
    }
}