import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DNSQuestion {
    private String[] domain_;
    private String qType_;
    private String qClass_;

    private DNSQuestion(String[] domain, String qType, String qClass) {
        domain_ = domain;
        qType_ = qType;
        qClass_ = qClass;
    }

    /**
     *                                     1  1  1  1  1  1
     *       0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                                               |
     *     /                     QNAME                     /
     *     /                                               /
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                     QTYPE                     |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                     QCLASS                    |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * @param inStream Stream containing start of message section at the front of the stream
     * @param message DNSMessage to append question to
     * @return Instance of DNSQuestion containing decoded question
     * @throws IOException
     */
    public static DNSQuestion decodeQuestion(ByteArrayInputStream inStream, DNSMessage message) throws IOException {
        return new DNSQuestion(
                message.readDomainName(inStream),
                new String(inStream.readNBytes(2)),
                new String(inStream.readNBytes(2))
        );
    }
}
