import java.util.HashMap;

public class DNSCache {
    public HashMap<DNSQuestion, DNSRecord> cache_ = new HashMap<>();
    void add(DNSQuestion question, DNSRecord record){
        cache_.put(question, record);
    }
    DNSRecord get(DNSQuestion question){
        DNSRecord record = cache_.get(question);
        if (record == null){
            cache_.remove(question);
            return null;
        }
        return record.isExpired() ? null : record;
    }

    public HashMap<DNSQuestion, DNSRecord> getCache(){
        return cache_;
    }
}
