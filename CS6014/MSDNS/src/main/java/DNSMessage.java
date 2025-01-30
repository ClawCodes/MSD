import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DNSMessage {
    private byte[] message_;
    private DNSHeader header_;
    private DNSQuestion question_;

    DNSMessage(byte[] message) {
        message_ = message;
    }

    private ByteArrayInputStream readDomainName(int firstByte){
        if (firstByte < 12){
            throw new IllegalArgumentException("A domain pointer is not expected in a header section.");
        }
        return new ByteArrayInputStream(Arrays.copyOfRange(message_, firstByte, message_.length));
    }

    // TODO: TEST!
    public String[] readDomainName(ByteArrayInputStream inStream) throws IOException {
        ArrayList<String> domain = new ArrayList<>();
        int labelLen = inStream.read();
        // first two bits are set indicates pointer
        if (BitHelper.getBits(labelLen, 0, 2) == 3){
            int offset = BitHelper.getBits(labelLen, 2, 8);
            inStream = readDomainName(offset); // new stream containing non-compressed domain name
            labelLen = inStream.read();
        }
        while (labelLen != 0){
            String label = new String(inStream.readNBytes(labelLen));
            domain.add(label);
            labelLen = inStream.read();
        }
        return domain.toArray(new String[domain.size()]);
    }

    public void setHeader(DNSHeader header){
        header_ = header;
    }

    public void setQuestion(DNSQuestion question) {
        question_ = question;
    }

    public ByteArrayInputStream getInputStream(){
        return new ByteArrayInputStream(message_);
    }

}
