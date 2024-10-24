import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    ConnectionHandler(Socket socket){
        socket_ = socket;
    }

    @Override
    public void run() {
        byte[] response;
        HTTPResponse responseHandler = new HTTPResponse();
        try {
            HTTPRequest requestHandler = new HTTPRequest(socket_.getInputStream());
            String fileName = requestHandler.getResource();
            System.out.println(fileName);
            response = responseHandler.fetchResource(fileName);
            socket_.getOutputStream().write(response);
        } catch (IOException e) {
            response = responseHandler.create404().getBytes();
        }
        responseHandler.sendResponse(socket_, response);
    }

    Socket socket_;
}
