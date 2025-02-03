import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 *                                   1  1  1  1  1  1
 *     0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *   |                                               |
 *   /                     QNAME                     /
 *   /                                               /
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *   |                     QTYPE                     |
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *   |                     QCLASS                    |
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 */
public class DNSQuestion {
    private String[] domain_;
    private byte[] qType_;
    private byte[] qClass_;

    private DNSQuestion(String[] domain, byte[] qType, byte[] qClass) {
        domain_ = domain;
        qType_ = qType;
        qClass_ = qClass;
    }

    /**
     * @param inStream Stream containing start of message section at the front of the stream
     * @param message DNSMessage to append question to
     * @return Instance of DNSQuestion containing decoded question
     * @throws IOException
     */
    public static DNSQuestion decodeQuestion(ByteArrayInputStream inStream, DNSMessage message) throws IOException {
        return new DNSQuestion(
                message.readDomainName(inStream),
                inStream.readNBytes(2),
                inStream.readNBytes(2)
        );
    }

    public void writeBytes(ByteArrayOutputStream outStream, HashMap<String, Integer> domainLocations) throws IOException {
        DNSMessage.writeDomainName(outStream, domainLocations, domain_);
        outStream.writeBytes(qType_);
        outStream.writeBytes(qClass_);
    }

    public String[] getDomain() {
        return domain_;
    }

    public int getQType() {
        return BitHelper.bytePairToInt(qType_);
    }
    public int getQClass() {
        return BitHelper.bytePairToInt(qClass_);
    }

    public String toString() {
        return String.format(
                "Domain: %s\nType: %s\nClass: %s\n",
                DNSMessage.joinDomainName(domain_),
                getQType(),
                getQClass()
        );
    }

    @Override
    public boolean equals(Object obj) {
        DNSQuestion question;
        try {
            question = (DNSQuestion) obj;
        } catch (ClassCastException e) {
            return false;
        }
        return Arrays.equals(domain_, question.domain_) && Arrays.equals(qType_, question.qType_) && Arrays.equals(qClass_, question.qClass_);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }
}
