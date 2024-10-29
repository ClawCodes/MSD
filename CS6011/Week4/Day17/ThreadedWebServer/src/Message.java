import com.google.gson.Gson;

/**
 * Class representing a standard JSON message to send back to clients connected with the server
 */
public class Message {
    // NOTE: some IDEs might show these member variables are not used, however,
    // they are in the gson library in toString.
    private String type;
    private String user;
    private String room;
    private String message;

    /**
     * Constructor requiring a value of a message.
     * Use this constructor when sending a message to clients in a room
     * @param type type of message
     * @param user the user the message was sent from
     * @param room the room to send the message to
     * @param message the message content
     */
    Message(String type, String user, String room, String message) {
        setBaseVars(type, user, room);
        this.message = message;
    }

    /**
     * Constructor NOT requiring a value of a message.
     * Use this constructor when sending a message to a client that serves as a flag (e.g. join)
     * @param type type of message
     * @param user the user the message was sent from
     * @param room the room to send the message to
     */
    Message(String type, String user, String room) {
        setBaseVars(type, user, room);
        this.message = null; // Construct with omission of message
    }

    private void setBaseVars(String type, String user, String room) {
        this.type = type;
        this.user = user;
        this.room = room;
    }

    /**
     * Convert the message to a JSON string that is ready to be sent to a client
     * @return JSON string of the message
     */
    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
