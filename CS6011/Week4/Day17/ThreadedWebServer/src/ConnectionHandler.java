import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

/**
 * Runnable which is 1:1 with each connected client.
 * Each client will get its own thread, thus this class should be called in the creation of a new thread upon client
 * connection
 * <p>
 * Example:
 * Thread thread = new Thread(new ConnectionHandler(socket));
 * thread.start()
 */
public class ConnectionHandler extends MessageHandler implements Runnable {
    ConnectionHandler(Socket socket){
        super(socket);
    }

    /**
     * Runnable.run implementation which handled HTTP requests, responses, and web socket connections.
     */
    @Override
    public void run() {
        byte[] response;
        try {
            HTTPRequest requestHandler = new HTTPRequest(socket_.getInputStream());
            if (requestHandler.hasHeader("Sec-WebSocket-Key")) {
                String clientKey = requestHandler.getHeaderValue("Sec-WebSocket-Key");
                sendResponse(HTTPResponse.createWebSocketResponse(clientKey).getBytes());
                WebSocketHandler wsHandler = new WebSocketHandler(socket_);
                wsHandler.keepAlive();
            } else {
                String fileName = requestHandler.getResource();
                response = HTTPResponse.fetchResource(fileName);
                sendResponse(response);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            sendResponse(HTTPResponse.create404().getBytes());
        }
        closeSocket();
    }
}
