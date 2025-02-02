import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DNSMessage {
    private byte[] message_;
    private DNSHeader header_;
    private ArrayList<DNSQuestion> questions_ = new ArrayList<>();
    private ArrayList<DNSRecord> answers_ = new ArrayList<>();
    private ArrayList<DNSRecord> authorityRecords_ = new ArrayList<>();
    private ArrayList<DNSRecord> additionalRecords_ = new ArrayList<>();
    private HashMap<String, Integer> domainLocations = new HashMap<>();

    // empty message constructor
    DNSMessage(){}

    DNSMessage(byte[] message) {
        message_ = message;
    }

    private ByteArrayInputStream readDomainName(int firstByte){
        return new ByteArrayInputStream(Arrays.copyOfRange(message_, firstByte, message_.length));
    }

    public String[] readDomainName(ByteArrayInputStream inStream) throws IOException {
        ArrayList<String> domain = new ArrayList<>();
        int labelLen = inStream.read();
        // first two bits are set indicates pointer
        int offset = -1;
        while (labelLen != 0){
            if (BitHelper.getBits(labelLen, 0, 1) == 3){ // firs two bits are 11 indicating a pointer follows
                offset = BitHelper.getBits(labelLen, 2, 8) << 8; // get first part of offset
                offset |= inStream.read(); //
                inStream = readDomainName(offset); // new stream containing non-compressed domain name
                labelLen = inStream.read();
            }
            String label = new String(inStream.readNBytes(labelLen));
            domain.add(label);
            labelLen = inStream.read();
        }
        String[] domainParts = domain.toArray(new String[domain.size()]);
        // Add domain to locations when a pointer is encountered
        if (offset >= 0){
            domainLocations.put(joinDomainName(domainParts), offset);
        }
        return domainParts;
    }

    String joinDomainName(String[] pieces){
        return String.join(".", pieces);
    }

    public static DNSMessage decodeMessage(byte[] bytes) throws IOException {
        DNSMessage message = new DNSMessage(bytes);
        ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);

        DNSHeader header = DNSHeader.decodeHeader(inStream);
        message.setHeader(header);

        // decode questions
        for (int i = 0; i < header.getQCount(); i++) {
            message.questions_.add(DNSQuestion.decodeQuestion(inStream, message));
        }

        // decode answers
        for (int i = 0; i < header.getANCount(); i++) {
            message.answers_.add(DNSRecord.decodeRecord(inStream, message));
        }

        // decode authority records
        for (int i = 0; i < header.getNSCount(); i++) {
            message.authorityRecords_.add(DNSRecord.decodeRecord(inStream, message));
        }

        // decode additional records
        for (int i = 0; i < header.getARCount(); i++) {
            message.additionalRecords_.add(DNSRecord.decodeRecord(inStream, message));
        }

        return message;
    }

    public void setHeader(DNSHeader header){
        header_ = header;
    }

    public DNSHeader getHeader(){
        return header_;
    }

    public boolean hasHeader(){
        return header_ != null;
    }

    public void addAnswer(DNSRecord record){
        answers_.add(record);
    }

    public int numAnswers(){
        return answers_.size();
    }

    public boolean hasAnswers(){
        return !answers_.isEmpty();
    }

    public ArrayList<DNSQuestion> getQuestions(){
        return questions_;
    }

    public HashMap<String, Integer> getDomainLocations() {
        return domainLocations;
    }
}
