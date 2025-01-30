import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DNSMessage {
    private DNSHeader header_;
    private DNSQuestion question_;

    DNSMessage(DNSHeader header) {
        header_ = header;
    }

    public String[] readDomainName(ByteArrayInputStream inStream) throws IOException {
        ArrayList<String> domain = new ArrayList<>();
        int labelLen = inStream.read();
        while (labelLen != 0){
            String label = new String(inStream.readNBytes(labelLen));
            domain.add(label);
            labelLen = inStream.read();
        }
        return domain.toArray(new String[domain.size()]);
    }

    public void setQuestion(DNSQuestion question) {
        question_ = question;
    }

}
