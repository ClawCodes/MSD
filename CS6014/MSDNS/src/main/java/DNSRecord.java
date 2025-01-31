import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *                                     1  1  1  1  1  1
 *       0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                                               |
 *     /                                               /
 *     /                      NAME                     /
 *     |                                               |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                      TYPE                     |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                     CLASS                     |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                      TTL                      |
 *     |                                               |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *     |                   RDLENGTH                    |
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--|
 *     /                     RDATA                     /
 *     /                                               /
 *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 */
public class DNSRecord {
    String[] domain_;
    byte[] type_;
    byte[] class_;
    int ttl_;
    int rdLength_;
    byte[] rdata_;

    private DNSRecord(String[] domain, byte[] type, byte[] class_, int ttl, int rdLength, byte[] rdata) {
        domain_ = domain;
        type_ = type;
        this.class_ = class_;
        ttl_ = ttl;
        rdLength_ = rdLength;
        rdata_ = rdata;
    }

    static DNSRecord decodeRecord(ByteArrayInputStream inStream, DNSMessage message) throws IOException {
        String[] domain = message.readDomainName(inStream);
        byte[] type_ = inStream.readNBytes(2);
        byte[] class_ = inStream.readNBytes(2);
        int ttl = BitHelper.bytePairToInt(inStream.readNBytes(2));
        int rdLength = BitHelper.bytePairToInt(inStream.readNBytes(2));

        return new DNSRecord(
                domain,
                type_,
                class_,
                ttl,
                rdLength,
                inStream.readNBytes(rdLength)
        );
    }
}
