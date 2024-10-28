import java.io.IOException;
import java.util.*;

public class RoomManager {
    private static Map<String, ArrayList<WebSocketHandler>> rooms_ = new HashMap<>();

    public synchronized static void addRoom(String room, WebSocketHandler handler) {
        if (!rooms_.containsKey(room)) {
            ArrayList<WebSocketHandler> newRoom = new ArrayList<>();
            newRoom.add(handler);
            rooms_.put(room, newRoom);
        } else {
            rooms_.get(room).add(handler);
        }
    }

    public synchronized static void sendMessage(String room, String message) throws IOException {
        if (!rooms_.containsKey(room)) {
            throw new RuntimeException("Room not found.");
        } else {
            for (WebSocketHandler handler : rooms_.get(room)) {
                handler.sendText(message);
            }
        }
    }
}
