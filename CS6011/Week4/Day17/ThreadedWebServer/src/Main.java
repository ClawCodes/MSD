import com.google.gson.Gson;

import java.io.*;

public class Main {
    /**
     * Start server and access on port 8080/index.html
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server(8080);
        server.start();
    }
}