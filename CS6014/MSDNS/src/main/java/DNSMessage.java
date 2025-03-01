import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        int numUniqueLables = 0;
        while (labelLen != 0){
            if (BitHelper.getBits(labelLen, 0, 1) == 3){ // firs two bits are 11 indicating a pointer follows
                offset = BitHelper.getBits(labelLen, 2, 8) << 8; // get first part of offset
                offset |= inStream.read(); //
                inStream = readDomainName(offset); // new stream containing non-compressed domain name
                labelLen = inStream.read();
                numUniqueLables = domain.size();
            }
            String label = new String(inStream.readNBytes(labelLen));
            domain.add(label);
            labelLen = inStream.read();
        }
        String[] domainParts = domain.toArray(new String[domain.size()]);
        // Add domain to locations when a pointer is encountered
        if (offset >= 0){
            domainLocations.put(joinDomainName(
                    Arrays.copyOfRange(domainParts, numUniqueLables, domainParts.length)
            ), offset);
        }
        return domainParts;
    }

    public static String joinDomainName(String[] pieces){
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

    static void writeDomainName(ByteArrayOutputStream outStream, HashMap<String,Integer> domainLocations, String[] domainPieces) throws IOException {
        for (int i = 0; i < domainPieces.length; i++){
            Integer offset = domainLocations.get(joinDomainName(Arrays.copyOfRange(domainPieces, i, domainPieces.length)));
            if (offset != null && offset < outStream.size()){
                int ptr = 0xC0;
                byte[] offsetParts = BitHelper.intToByteArray(offset, 2);
                offsetParts[0] |= (byte) ptr;
                outStream.write(offsetParts);
                return; // end as rest of domain is a pointer
            } else {
                String label = domainPieces[i];
                outStream.write((byte) label.length());
                outStream.write(domainPieces[i].getBytes());
            }
        }
        outStream.write((byte)0);
    }

    public static DNSMessage buildResponse(DNSMessage request, DNSRecord[] answers) throws IOException {
        DNSMessage response = new DNSMessage();
        for (DNSRecord answer : answers){
            response.addAnswer(answer);
        }

        DNSHeader header = DNSHeader.buildHeaderForResponse(request, response);
        response.setHeader(header);

        response.questions_.addAll(request.questions_);
        response.answers_.addAll(request.answers_);
        response.authorityRecords_.addAll(request.authorityRecords_);
        response.additionalRecords_.addAll(request.additionalRecords_);
        response.domainLocations.putAll(request.domainLocations);

        return response;
    }

    public byte[] toBytes() throws IOException {
     ByteArrayOutputStream outStream = new ByteArrayOutputStream();
     header_.writeBytes(outStream);
     for (DNSQuestion question : questions_){
         question.writeBytes(outStream, domainLocations);
     }
     for (DNSRecord answer : answers_){
         answer.writeBytes(outStream, domainLocations);
     }
     for (DNSRecord authorityRecord : authorityRecords_){
         authorityRecord.writeBytes(outStream, domainLocations);
     }
     for (DNSRecord additionalRecord : additionalRecords_){
         additionalRecord.writeBytes(outStream, domainLocations);
     }
     return outStream.toByteArray();
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

    public ArrayList<DNSRecord> getAnswers(){
        return answers_;
    }

    public HashMap<String, Integer> getDomainLocations() {
        return domainLocations;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(header_.toString());
        for (DNSQuestion question : questions_){
            str.append(question.toString());
        }
        for (DNSRecord answer : answers_){
            str.append(answer.toString());
        }
        for (DNSRecord authorityRecord : authorityRecords_){
            str.append(authorityRecord.toString());
        }
        for (DNSRecord additionalRecord : additionalRecords_){
            str.append(additionalRecord.toString());
        }
        return str.toString();
    }
}
