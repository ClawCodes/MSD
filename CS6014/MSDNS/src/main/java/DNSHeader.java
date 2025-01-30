import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class DNSHeader {
    byte[] header_;

    DNSHeader(byte[] header) {
        header_ = header;
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
     * @param inStream - input stream containing datagram packet
     * @return Instance of DNSHeader
     * @throws IOException
     */
    public static DNSHeader decodeHeader(ByteArrayInputStream inStream) throws IOException {
//        // Extract ID
//        byte[] line1 = in.readNBytes(2);
//        int QRtoRD = in.read();
//        int RAtoRCODE = in.read();
//        return new DNSHeader(
//                bytePairToInt(line1),
//                getBits(QRtoRD, 0, 0),
//                getBits(QRtoRD, 1, 4),
//                getBits(QRtoRD, 5, 5),
//                getBits(QRtoRD, 6, 6),
//                getBits(QRtoRD, 7, 7),
//                getBits(RAtoRCODE, 0, 0),
//                getBits(RAtoRCODE, 1, 1),
//                getBits(RAtoRCODE, 2, 2),
//                getBits(RAtoRCODE, 3, 3),
//                getBits(RAtoRCODE, 4, 7),
//                bytePairToInt(in.readNBytes(2)),
//                bytePairToInt(in.readNBytes(2)),
//                bytePairToInt(in.readNBytes(2)),
//                bytePairToInt(in.readNBytes(2))
//                );
        return new DNSHeader(inStream.readNBytes(12));
    }
    byte[] getHeader() {
        return header_;
    }

    int getQCount(){
        return BitHelper.bytePairToInt(Arrays.copyOfRange(header_, 4, 6));
    }
}
