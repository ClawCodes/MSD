import java.io.IOException;
import java.util.*;

/**
 * Class which manages the creation and communication of chat rooms along with the room's connected clients and message history.
 */
public class RoomManager {
    private static Map<String, AbstractMap.SimpleEntry<ArrayList<WebSocketHandler>, ArrayList<String>>> rooms_ = new HashMap<>();

    /**
     * Create a room.
     * If the room already exists then the client requesting the room creation, will be added to the room.
     * Otherwise, the room will be created with the requesting client included
     * @param room room name to create
     * @param handler the WebSocketHandler representing the client requesting the room creation
     */
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
                handler.sendText(message);
            }
        }
    }

    /**
     * Send a message to all clients in a room
     * @param room Room name to send the message to
     * @param message Message to send
     * @throws IOException
     */
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
