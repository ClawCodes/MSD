import java.io.IOException;
import java.io.InputStream;

public class DNSHeader {
    private int id_;
    private int qr_;
    private int opCode_;
    private int AA_;
    private int TC_;
    private int RD_;
    private int RA_;
    private int Z_;
    private int AD_;
    private int CD_;
    private int RCode_;
    private int QDCount_;
    private int ANCount_;
    private int NSCount_;
    private int ARCount_;

    private DNSHeader(int id, int qr, int opCode, int aa, int tc, int rd,
                      int ra, int z, int ad, int cd, int rcode,
                      int qdCount, int anCount, int nsCount, int arCount) {
        id_ = id;
        qr_ = qr;
        opCode_ = opCode;
        AA_ = aa;
        TC_ = tc;
        RD_ = rd;
        RA_ = ra;
        Z_ = z;
        AD_ = ad;
        CD_ = cd;
        RCode_ = rcode;
        QDCount_ = qdCount;
        ANCount_ = anCount;
        NSCount_ = nsCount;
        ARCount_ = arCount;
    }

    private static int bytePairToInt(byte[] bytes) {
        if (bytes.length != 2){
            throw new IllegalArgumentException("Byte array must only contain two elements.");
        }
        int mask = 0;
        mask |= (bytes[0] << 8);
        mask |= bytes[1];
        return mask;
    }

    private static int getBits(int b, int start, int end ){
        int mask = 1;
        for (int i = start; i < end; i++){
            mask = (mask << 1) | 1;
        }
        mask = mask << (7 - end);
        return (b & mask) >> (7 - end);
    }

    /**
     * Parse raw header from DataGramPacket input stream.
     * Headers are expected to follow the following diagram:
     *                                  1  1  1  1  1  1
     *    0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
     *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *   |                      ID                       |
     *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *   |QR|   OpCode  |AA|TC|RD|RA| Z|AD|CD|   RCODE   |
     *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *   |                QDCOUNT/ZOCOUNT                |
     *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *   |                ANCOUNT/PRCOUNT                |
     *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *   |                NSCOUNT/UPCOUNT                |
     *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *   |                    ARCOUNT                    |
     *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * @param in - input stream containing datagram packet
     * @return Instance of DNSHeader
     * @throws IOException
     */
    public static DNSHeader decodeHeader(InputStream in) throws IOException {
        // Extract ID
        byte[] line1 = in.readNBytes(2);
        int QRtoRD = in.read();
        int RAtoRCODE = in.read();

        return new DNSHeader(
                bytePairToInt(line1),
                getBits(QRtoRD, 0, 0),
                getBits(QRtoRD, 1, 4),
                getBits(QRtoRD, 5, 5),
                getBits(QRtoRD, 6, 6),
                getBits(QRtoRD, 7, 7),
                getBits(RAtoRCODE, 0, 0),
                getBits(RAtoRCODE, 1, 1),
                getBits(RAtoRCODE, 2, 2),
                getBits(RAtoRCODE, 3, 3),
                getBits(RAtoRCODE, 4, 7),
                bytePairToInt(in.readNBytes(2)),
                bytePairToInt(in.readNBytes(2)),
                bytePairToInt(in.readNBytes(2)),
                bytePairToInt(in.readNBytes(2))
                );
    }

    public int getId() {
        return id_;
    }

    public int getQR() {
        return qr_;
    }
    public int getOpCode() {
        return opCode_;
    }
    public int getAa() {
        return AA_;
    }
    public int getTc() {
        return TC_;
    }
    public int getRd() {
        return RD_;
    }
}
