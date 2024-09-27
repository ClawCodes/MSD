import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTTPResponse {
    static String HTTPTemplate = "HTTP/1.1 {} {}\r\n{}Connection: close\r\n";

    static String create202(String contentType, String content){
        return String.format(HTTPTemplate, "Content-Type: " + "text/" + contentType, content);
    }

    /**
     * Create bare 404 response
     *
     * @return 404 response as string
     */
    static String create404(){
        return String.format(HTTPTemplate, "404", "Not Found", "");
    }

    /**
     * Create 404 with additional content
     *
     * @param contentType
     * @param content
     * @return
     */
    static String create404(String contentType, String content){
        return String.format(HTTPTemplate, "404", "Not Found", "Content-Type: " + "text/" + contentType, content);
    }

    private static String failureResponse = """
            HTTP/1.1 404 Not Found
            Connection: close\r\n
            """;
    

    public static String readFile(String fileName) throws IOException {
        if (fileName.equals("/"))
            fileName = "/index.html";

        return Files.readString(Paths.get("resources" + fileName));
    }

    static public String fetchResource(String resourceName) throws IOException {
        String response = failureResponse;
        try {
            String content = HTTPResponse.readFile(resourceName);
            String contentType = "plain";
            if (resourceName.endsWith(".html"))
                contentType = "html";
                response = HTTPResponse.create202(contentType, content);
        }
        catch (IOException e) {
                response = HTTPResponse.create404();
        }

        return response;
    }
}
