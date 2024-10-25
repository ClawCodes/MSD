import java.net.Socket;

public class WebSocketHandler extends MessageHandler {
    // TODO:
    // Parse WS message
    // Crete WS message
    // Keep socket alive
    WebSocketHandler(Socket socket){
        super(socket);
    }

    // TODO: implement pong response
    public void pong(){
    }

    public void keepAlive(){
        while(true){

        }
    }
}
