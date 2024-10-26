import java.io.IOException;
import java.util.*;

public class RoomManager {
    private static Map<String, List<WebSocketHandler>> rooms_ = new HashMap<>();

    public synchronized static void addRoom(String room, WebSocketHandler handler) {
        if (!rooms_.containsKey(room)) {
            rooms_.put(room, Arrays.asList(handler));
        }
    }

    public synchronized static void sendMessage(String room, String message, String fromUser) throws IOException {
        if (!rooms_.containsKey(room)) {
            throw new RuntimeException("Room not found.");
        } else {
            for (WebSocketHandler handler : rooms_.get(room)) {
                if (!handler.getUser().equals(fromUser)) {
                    handler.sendText(message);
                }
            }
        }
    }
}
