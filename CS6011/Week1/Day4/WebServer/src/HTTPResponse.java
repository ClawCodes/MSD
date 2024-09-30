import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTTPResponse {
    static final String HTTPTemplate = """
            HTTP/1.1 %s %s
            Content-Type: text/%s
            Connection: close\r\n
            %s
            """;

    static String create202(String contentType, String content) {
        return String.format(HTTPTemplate, "200", "OK", contentType, content);
    }

    static String create404(String contentType, String content) {
        return String.format(HTTPTemplate, "404", "Not Found", contentType, content);
    }

    public static String readFile(String fileName) throws IOException {
        if (fileName.equals("/"))
            fileName = "/index.html";

        return Files.readString(Paths.get("resources" + fileName));
    }

    static public String fetchResource(String resourceName) {
        String response;
        try {
            String content = HTTPResponse.readFile(resourceName);
            String contentType = "plain";
            if (resourceName.equals("/"))
                contentType = "html";
            response = HTTPResponse.create202(contentType, content);
        } catch (IOException e) {
            response = HTTPResponse.create404("plain", "Requested content does not exist.");
            e.printStackTrace();
        }

        return response;
    }

    static void sendResponse(Socket socket, String response) {
        int retry = 0;
        while (retry < 3) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(response.getBytes());
                socket.getOutputStream().flush();
                socket.close();
                return;
            } catch (IOException e) {
                retry++;
            }
        }
        System.out.println("ERROR: Failed to send response to " + socket.getRemoteSocketAddress());
    }
}
