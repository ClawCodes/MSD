import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class DNSCacheTest {
    @Test
    public void testGet() throws IOException {
        byte[] dnsResponse = {
                (byte) 0x12, (byte) 0x34, (byte) 0x81, (byte) 0x80,  // Transaction ID + Flags
                (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01,  // QDCOUNT = 1, ANCOUNT = 1
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,  // NSCOUNT = 0, ARCOUNT = 1

                // Question Section (www.example.com)
                (byte) 0x03, (byte) 0x77, (byte) 0x77, (byte) 0x77,  // "3www"
                (byte) 0x07, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x6C, (byte) 0x65,  // "7example"
                (byte) 0x03, (byte) 0x63, (byte) 0x6F, (byte) 0x6D, (byte) 0x00,  // "3com."
                (byte) 0x00, (byte) 0x01,  // QTYPE = A
                (byte) 0x00, (byte) 0x01,  // QCLASS = IN

                // Answer Section (A Record for www.example.com -> 93.184.216.34)
                (byte) 0xC0, (byte) 0x0C,  // Pointer to "www.example.com"
                (byte) 0x00, (byte) 0x01,  // TYPE = A
                (byte) 0x00, (byte) 0x01,  // CLASS = IN
                (byte) 0x00, (byte) 0x3C,  // TTL = 60 seconds
                (byte) 0x00, (byte) 0x04,  // RDLENGTH = 4 bytes
                (byte) 0x5D, (byte) 0xB8, (byte) 0xD8, (byte) 0x22,  // RDATA = 93.184.216.34

                // Additional Section (NS Record: ns1.example.com)
                (byte) 0x03, (byte) 0x6E, (byte) 0x73, (byte) 0x31, (byte) 0xC0, (byte) 0x10,  // "ns1.example.com" (pointer to example.com)
                (byte) 0x00, (byte) 0x02,  // TYPE = NS (0x0002)
                (byte) 0x00, (byte) 0x01,  // CLASS = IN (0x0001)
                (byte) 0x00, (byte) 0x3C,  // TTL = 60 seconds (0x3C)
                (byte) 0x00, (byte) 0x06,  // RDLENGTH = 6 bytes
                (byte) 0x03, (byte) 0x6E, (byte) 0x73, (byte) 0x31, (byte) 0xC0, (byte) 0x10   // RDATA = "ns1.example.com" (pointer to example.com)
        };

        DNSMessage message = DNSMessage.decodeMessage(dnsResponse);
        DNSRecord answer = message.getAnswers().get(0);
        DNSQuestion question = message.getQuestions().get(0);
        DNSCache cache = new DNSCache();
        cache.add(question, answer);

        assertEquals(answer, cache.get(question));
    }
}