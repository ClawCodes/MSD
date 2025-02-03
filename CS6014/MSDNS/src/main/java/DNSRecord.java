import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

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
    private String[] domain_;
    private int type_;
    private byte[] class_;
    private int ttl_;
    private int rdLength_;
    private byte[] rdata_;
    private LocalDateTime expireDtm;


    private DNSRecord(String[] domain, int type, byte[] class_, int ttl, int rdLength, byte[] rdata) {
        domain_ = domain;
        type_ = type;
        this.class_ = class_;
        ttl_ = ttl;
        rdLength_ = rdLength;
        rdata_ = rdata;
        expireDtm = LocalDateTime.now().plusSeconds(ttl);
    }

    static DNSRecord decodeRecord(ByteArrayInputStream inStream, DNSMessage message) throws IOException {
        String[] domain = message.readDomainName(inStream);
        int type_ = BitHelper.bytePairToInt(inStream.readNBytes(2));

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

    public void writeBytes(ByteArrayOutputStream outStream, HashMap<String, Integer> domainLocations) throws IOException {
        DNSMessage.writeDomainName(outStream, domainLocations, domain_);
        outStream.writeBytes(BitHelper.intToByteArray(type_, 2));
        outStream.writeBytes(class_);
        outStream.writeBytes(BitHelper.intToByteArray(ttl_, 2));
        outStream.writeBytes(BitHelper.intToByteArray(rdLength_, 2));
        outStream.writeBytes(rdata_);
    }

    public String[] getDomain() {
        return domain_;
    }

    public void increaseExpiration(int seconds){
        expireDtm = expireDtm.plusSeconds(seconds);
    }

    boolean isExpired() {
        return LocalDateTime.now().isAfter(expireDtm);
    }

    public String toString() {
        return String.format(
                "Domain: %s\nType: %s\nClass: %s\nTTL: %d\nRD Length: %d\nExpires: %s",
                DNSMessage.joinDomainName(domain_),
                type_,
                BitHelper.bytePairToInt(class_),
                ttl_,
                rdLength_,
                expireDtm
        );
    }
}
