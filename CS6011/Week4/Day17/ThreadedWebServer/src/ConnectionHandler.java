import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class ConnectionHandler extends MessageHandler implements Runnable {
    ConnectionHandler(Socket socket){
        super(socket);
    }

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
