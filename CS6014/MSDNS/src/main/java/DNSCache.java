import java.util.HashMap;

public class DNSCache {
    static HashMap<DNSQuestion, DNSRecord> cache_ = new HashMap<>();
    static void add(DNSQuestion question, DNSRecord record){
        // TODO: Add TTL checks/updates
        if (!cache_.containsKey(question)){
            cache_.put(question, record);
        }
    }
    static DNSRecord get(DNSQuestion question){
        return cache_.get(question);
    }
}
