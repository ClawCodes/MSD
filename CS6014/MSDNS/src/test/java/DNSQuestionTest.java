import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

class DNSQuestionTest {
    @Test
    void testDecodeQuestion() throws IOException {
        byte[] question = new byte[] {
                (byte)0x07, (byte)0x65, (byte)0x78, (byte)0x61, (byte)0x6D, (byte)0x70, (byte)0x6C, (byte)0x65, // 7example
                (byte)0x03, (byte)0x63, (byte)0x6F, (byte)0x6D, (byte)0x00, // 3com (with termination)
                (byte)0x00, (byte)0x01, // QTYPE = 1 -> A (IPv4 address)
                (byte)0x00, (byte)0x01, // QCLASS - 1 -> IN (internet)
                (byte)0x00, (byte)0x00 // excess bytes not in question
        };

        String[] expectedDomain = new String[] {"example", "com"};
        int expectedType = 1; // A
        int expectedClass = 1; // IN

        DNSMessage dnsMessage = new DNSMessage(); // empty message
        ByteArrayInputStream inStream = new ByteArrayInputStream(question);
        DNSQuestion actual = DNSQuestion.decodeQuestion(inStream, dnsMessage);

        assertArrayEquals(expectedDomain, actual.getDomain());
        assertEquals(expectedType, actual.getQType());
        assertEquals(expectedClass, actual.getQClass());
    }

    @Test
    void testWriteBytes() throws IOException {
        byte[] question = new byte[] {
                (byte)0x07, (byte)0x65, (byte)0x78, (byte)0x61, (byte)0x6D, (byte)0x70, (byte)0x6C, (byte)0x65, // 7example
                (byte)0x03, (byte)0x63, (byte)0x6F, (byte)0x6D, (byte)0x00, // 3com (with termination)
                (byte)0x00, (byte)0x01, // QTYPE = 1 -> A (IPv4 address)
                (byte)0x00, (byte)0x01, // QCLASS - 1 -> IN (internet)
                (byte)0x00, (byte)0x00 // excess bytes not in question
        };

        byte[] expected = Arrays.copyOfRange(question, 0, question.length - 2);

        DNSMessage dnsMessage = new DNSMessage(); // empty message
        ByteArrayInputStream inStream = new ByteArrayInputStream(question);
        DNSQuestion actual = DNSQuestion.decodeQuestion(inStream, dnsMessage);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        actual.writeBytes(outStream, dnsMessage.getDomainLocations());

        assertArrayEquals(expected, outStream.toByteArray());
    }

    @Test
    void testToString() throws IOException {
        byte[] question = new byte[] {
                (byte)0x07, (byte)0x65, (byte)0x78, (byte)0x61, (byte)0x6D, (byte)0x70, (byte)0x6C, (byte)0x65, // 7example
                (byte)0x03, (byte)0x63, (byte)0x6F, (byte)0x6D, (byte)0x00, // 3com (with termination)
                (byte)0x00, (byte)0x01, // QTYPE = 1 -> A (IPv4 address)
                (byte)0x00, (byte)0x01, // QCLASS - 1 -> IN (internet)
                (byte)0x00, (byte)0x00 // excess bytes not in question
        };

        String expected = "Domain: example.com\nType: 1\nClass: 1\n";

        DNSMessage dnsMessage = new DNSMessage(); // empty message
        ByteArrayInputStream inStream = new ByteArrayInputStream(question);
        DNSQuestion actual = DNSQuestion.decodeQuestion(inStream, dnsMessage);

        assertEquals(expected, actual.toString());
    }

    @Test
    void testHashCode() throws IOException {
        byte[] question = new byte[] {
                (byte)0x07, (byte)0x65, (byte)0x78, (byte)0x61, (byte)0x6D, (byte)0x70, (byte)0x6C, (byte)0x65, // 7example
                (byte)0x03, (byte)0x63, (byte)0x6F, (byte)0x6D, (byte)0x00, // 3com (with termination)
                (byte)0x00, (byte)0x01, // QTYPE = 1 -> A (IPv4 address)
                (byte)0x00, (byte)0x01, // QCLASS - 1 -> IN (internet)
                (byte)0x00, (byte)0x00 // excess bytes not in question
        };

        DNSMessage dnsMessage = new DNSMessage(); // empty message

        ByteArrayInputStream aStream = new ByteArrayInputStream(question);
        DNSQuestion a = DNSQuestion.decodeQuestion(aStream, dnsMessage);

        ByteArrayInputStream bStream = new ByteArrayInputStream(question);
        DNSQuestion b = DNSQuestion.decodeQuestion(bStream, dnsMessage);

        HashSet<DNSQuestion> qSet = new HashSet<>();
        qSet.add(a);

        assertTrue(qSet.contains(b));
    }
}