import com.google.gson.Gson;

public class Message {
    private String type;
    private String user;
    private String room;
    private String message;

    Message(String type, String user, String room, String message) {
        this.type = type;
        this.user = user;
        this.room = room;
        this.message = message;
    }

    Message(String type, String user, String room) {
        this.type = type;
        this.user = user;
        this.room = room;
        this.message = null; // Construct with omission of message
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
