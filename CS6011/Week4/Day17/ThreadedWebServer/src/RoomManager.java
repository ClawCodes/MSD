import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    private static Map<String, ArrayList<WebSocketHandler>> rooms_ = new HashMap<>();

    public synchronized static void addRoom(String room, WebSocketHandler handler) {
        if (!rooms_.containsKey(room)) {
            rooms_.get(room).add(handler);
        }
    }

    public synchronized static void sendMessage(String room, String message, String fromUser) {
        if (!rooms_.containsKey(room)) {
            throw new RuntimeException("Room not found.");
        } else {
            for (WebSocketHandler handler : rooms_.get(room)) {
                if (!handler.getUser().equals(fromUser)) {
                    handler.sendPayload(message);
                }
            }
        }
    }
}
