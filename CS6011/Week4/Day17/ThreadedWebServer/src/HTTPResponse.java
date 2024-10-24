import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTTPResponse {
    final String HTTPTemplate = """
            HTTP/1.1 %s %s
            Content-Type: %s
            Connection: close
            Content-Length: %d\r\n
            """;

    String create202(String contentType, int contentLength) {
        return String.format(this.HTTPTemplate, "200", "OK", contentType, contentLength);
    }

    String create404() {
        return String.format(this.HTTPTemplate, "404", "Not Found", "plain", "Request could not be processed.".length());
    }

    public static byte[] readFile(String fileName) throws IOException {
        if (fileName.equals("/"))
            fileName = "/index.html";

        return Files.readAllBytes(Paths.get("resources" + fileName));
    }

    public String determineContentType(String resourceName) {
        if (resourceName.endsWith(".html") || resourceName.equals("/"))
            return "text/html";
        if (resourceName.endsWith(".css"))
            return "text/css";
        if (resourceName.endsWith(".js"))
            return "application/javascript";
        if (resourceName.endsWith(".png"))
            return "image/png";
        if (resourceName.endsWith(".jpg") || resourceName.endsWith(".jpeg"))
            return "image/jpeg";
        // Default to plain text for unknown extensions
        return "text/plain";
    }

    public byte[] fetchResource(String resourceName) {
        try {
            byte[] contentBytes = readFile(resourceName);
            String contentType = determineContentType(resourceName);
            String responseHeader = create202(contentType, contentBytes.length);
            System.out.println(responseHeader);
            // Concat header and file contents into single byte array
            ByteArrayOutputStream response = new ByteArrayOutputStream();
            response.write(responseHeader.getBytes());
            response.write(contentBytes);
            return response.toByteArray();
        } catch (IOException e) {
            String response = create404();
            e.printStackTrace(); // Print so, local server logs the error and doesn't fail
            return response.getBytes();
        }
    }

    void sendResponse(Socket socket, byte[] response) {
        int retry = 0;
        while (retry < 3) {
            try {
                OutputStream outStream = socket.getOutputStream();
                outStream.write(response);
                outStream.flush();
                socket.close();
                break;
            } catch (IOException e) {
                retry++;
            }
        }
    }
}
