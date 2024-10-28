import java.io.IOException;
import java.util.*;

public class RoomManager {
//    private static Map<String, ArrayList<WebSocketHandler>> rooms_ = new HashMap<>();
    private static Map<String, AbstractMap.SimpleEntry<ArrayList<WebSocketHandler>, ArrayList<String>>> rooms_ = new HashMap<>();

    public synchronized static void addRoom(String room, WebSocketHandler handler) {
        if (!rooms_.containsKey(room)) {
            ArrayList<WebSocketHandler> newRoom = new ArrayList<>();
            ArrayList<String> messages = new ArrayList<>(); // Add empty message container
            AbstractMap.SimpleEntry<ArrayList<WebSocketHandler>, ArrayList<String>> roomEntry = new AbstractMap.SimpleEntry<>(newRoom, messages);
            newRoom.add(handler);
            rooms_.put(room, roomEntry);
        } else {
            rooms_.get(room).getKey().add(handler);
            // When a room exists and a new client joins, the client must get the message history
            for (String message : rooms_.get(room).getValue()){
                System.out.println(message);
                handler.sendText(message);
            }
        }
    }

    public synchronized static void sendMessage(String room, String message) throws IOException {
        if (!rooms_.containsKey(room)) {
            throw new RuntimeException("Room not found.");
        } else {
            for (WebSocketHandler handler : rooms_.get(room).getKey()) {
                handler.sendText(message);
            }
            rooms_.get(room).getValue().add(message); // Preserve message history
        }
    }
}
