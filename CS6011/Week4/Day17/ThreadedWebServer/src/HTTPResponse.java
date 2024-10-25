import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HTTPResponse {
    static final String HTTPTemplate = """
            HTTP/1.1 %s %s
            Content-Type: %s
            Connection: close
            Content-Length: %d\r\n
            """;

    static final String WSTemplate = """
            HTTP/1.1 101 Switching Protocols
            Upgrade: websocket
            Connection: Upgrade
            Sec-WebSocket-Accept: %s\r\n
            """;

    static String create202(String contentType, int contentLength) {
        return String.format(HTTPTemplate, "200", "OK", contentType, contentLength);
    }

    static String create404() {
        String body = "Request could not be processed.";
        String base = String.format(HTTPTemplate, "404", "Not Found", "plain", body.length());
        return (base + body);
    }

    public static String createWebSocketResponse(String clientKey) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        String secret = Base64.getEncoder().encodeToString(md.digest((clientKey +
                "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()));

        return String.format(WSTemplate, secret);
    }

    public static byte[] readFile(String fileName) throws IOException {
        if (fileName.equals("/"))
            fileName = "/index.html";

        return Files.readAllBytes(Paths.get("resources" + fileName));
    }

    public static String determineContentType(String resourceName) {
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

    public static byte[] fetchResource(String resourceName) throws IOException {
            byte[] contentBytes = readFile(resourceName);
            String contentType = determineContentType(resourceName);
            String responseHeader = create202(contentType, contentBytes.length);
            // Concat header and file contents into single byte array
            ByteArrayOutputStream response = new ByteArrayOutputStream();
            response.write(responseHeader.getBytes());
            response.write(contentBytes);
            return response.toByteArray();
    }
}
