import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    ConnectionHandler(Socket socket){
        socket_ = socket;
    }

    @Override
    public void run() {
        byte[] response;
        HTTPRequest requestHandler = new HTTPRequest();
        HTTPResponse responseHandler = new HTTPResponse();
        try {
            String fileName = requestHandler.getResourceName(socket_);
            response = responseHandler.fetchResource(fileName);
        } catch (IOException e) {
            response = responseHandler.create404().getBytes();
        }
        responseHandler.sendResponse(socket_, response);
    }

    Socket socket_;
}
