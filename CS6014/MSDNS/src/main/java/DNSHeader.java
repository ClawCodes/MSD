import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
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
 */
public class DNSHeader {
    byte[] header_;

    DNSHeader(byte[] header) {
        header_ = header;
    }


    /**
     * Parse raw header from DataGramPacket input stream.
     * @param inStream - input stream containing datagram packet
     * @return Instance of DNSHeader
     * @throws IOException
     */
    public static DNSHeader decodeHeader(ByteArrayInputStream inStream) throws IOException {
        return new DNSHeader(inStream.readNBytes(12));
    }

    byte[] getHeader() {
        return header_;
    }
    
    private int slicePair(int start, int end){
        return BitHelper.bytePairToInt(Arrays.copyOfRange(header_, start, end));
    }

    int getQCount(){
        return slicePair(4, 5);
    }
    
    int getANCount(){
        return slicePair(6, 7);
    }
    
    int getNSCount(){
        return slicePair(8, 9);
    }
    
    int getARCount(){
        return slicePair(10, 11);
    }
}
