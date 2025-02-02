import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Headers are expected to follow the following diagram:
 * 1  1  1  1  1  1
 * 0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * |                      ID                       |
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * |QR|   OpCode  |AA|TC|RD|RA| Z|AD|CD|   RCODE   |
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * |                QDCOUNT/ZOCOUNT                |
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * |                ANCOUNT/PRCOUNT                |
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * |                NSCOUNT/UPCOUNT                |
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * |                    ARCOUNT                    |
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 */
public class DNSHeader {
    byte[] header_;

    DNSHeader(byte[] header) {
        header_ = header;
    }

    /**
     * Parse raw header from DataGramPacket input stream.
     *
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

    private int slicePair(int start, int end) {
        return BitHelper.bytePairToInt(Arrays.copyOfRange(header_, start, end));
    }

    int getQCount() {
        return slicePair(4, 6);
    }

    int getANCount() {
        return slicePair(6, 8);
    }

    int getNSCount() {
        return slicePair(8, 10);
    }

    int getARCount() {
        return slicePair(10, 12);
    }

    int getID() {
        return slicePair(0, 2);
    }

    /**
     * Use this method to build the header only for responses which use a cached answer
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    static DNSHeader buildHeaderForResponse(DNSMessage request, DNSMessage response) throws IOException {
        if (response.hasHeader()) {
            return response.getHeader();
        }
        int RCode = 0;
        if (!response.hasAnswers()) { // No answer provided by google or found in cache
            RCode = 2;
        }
        byte[] rawRequest = request.getHeader().getHeader();
        ByteArrayInputStream inStream = new ByteArrayInputStream(rawRequest); // create stream from request header
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        outStream.write(inStream.readNBytes(2)); // copy ID
        outStream.write(inStream.read() | 0x80); // flip QR bit
        int RAtoRCode = inStream.read();
        RAtoRCode &= 0xF0; // zero out lowest 4 bits
        RAtoRCode |= RCode;
        outStream.write(RAtoRCode);

        outStream.write(inStream.readNBytes(2)); // Keep same QDCOUNT
        byte[] ANCount = inStream.readNBytes(2);
        ANCount[1] = (byte) ((int) ANCount[1] + 1); // increment answer count by 1
        outStream.write(ANCount);
        outStream.write(inStream.readAllBytes()); // add the rest of the records to the message

        return new DNSHeader(outStream.toByteArray());
    }

    public void writeBytes(ByteArrayOutputStream outStream) {
        outStream.writeBytes(header_);
    }

    public String toString() {
        return String.format(
                "ID: %s\nQR: %s\nOpCode: %s\nAA: %s\nTC: %s\nRD: %s\n"
                        + "RA: %s\nZ: %s\nRCODE: %s\n"
                        + "QDCOUNT: %s\n"
                        + "ANCOUNT: %s\n"
                        + "NSCOUNT: %s\n"
                        + "ARCOUNT: %s\n",
                getID(),
                BitHelper.getBits(header_[2], 0, 0),
                BitHelper.getBits(header_[2], 1, 4),
                BitHelper.getBits(header_[2], 5, 5),
                BitHelper.getBits(header_[2], 6, 6),
                BitHelper.getBits(header_[2], 7, 7),
                BitHelper.getBits(header_[3], 0, 0),
                BitHelper.getBits(header_[3], 1, 3),
                BitHelper.getBits(header_[3], 4, 7),
                getQCount(),
                getANCount(),
                getNSCount(),
                getARCount()
        );
    }
}
