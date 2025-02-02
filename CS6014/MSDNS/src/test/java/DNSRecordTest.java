import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class DNSRecordTest {
    @Test
    public void testDecodeMessage() throws IOException {
        byte[] exampleDotCom = new byte[] {
                (byte) 0x07, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x6C, (byte) 0x65,  // "7example"
                (byte) 0x03, (byte) 0x63, (byte) 0x6F, (byte) 0x6D, (byte) 0x00,  // "3com."
        };

        byte[] record = new byte[] {
                (byte) 0x03, (byte) 0x6E, (byte) 0x73, (byte) 0x31, (byte) 0xC0, (byte) 0x00,  // "ns1.example.com" (compressed)
                (byte) 0x00, (byte) 0x02,  // TYPE = NS
                (byte) 0x00, (byte) 0x01,  // CLASS = IN
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3C,  // TTL = 60 seconds
                (byte) 0x00, (byte) 0x06,  // RDLENGTH
                (byte) 0x03, (byte) 0x6E, (byte) 0x73, (byte) 0x31, (byte) 0xC0, (byte) 0x00   // RDATA = "ns1.example.com" (compressed)
        };

        DNSMessage dnsMessage = new DNSMessage(exampleDotCom);
        ByteArrayInputStream inStream = new ByteArrayInputStream(record);
        DNSRecord actual = DNSRecord.decodeRecord(inStream, dnsMessage);

    }
}