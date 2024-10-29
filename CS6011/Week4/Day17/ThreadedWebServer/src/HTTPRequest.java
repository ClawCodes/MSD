import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Class representing a client HTTP request
 * This class contains a given requests' headers and their respective value along with the
 * requests' method and requested resource
 *
 */
public class HTTPRequest {
    /**
     * Constructor:
     * Reads and parses an HTTP request from a socket input stream.
     * @param inputStream socket InputStream to read the request from
     */
    HTTPRequest(InputStream inputStream) {
        try {
            Scanner scanner = new Scanner(inputStream);

            parseFirstLine(scanner.nextLine());

            while(true) {
                String requestLine = scanner.nextLine();

                // Break when we hit the end of the request
                if (requestLine.matches("^\\s*$")) {
                    break;
                }

                String[] splitLine = requestLine.split(": ");
                if (splitLine.length != 2) // Assumes not header
                    continue;
                headers_.put(splitLine[0], splitLine[1]);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error when attempting to handle request: ");
            e.printStackTrace();
        }
    }

    /**
     * Parse the first line of an HTTP request and store the HTTP method and the requested resource name
     * @param line first line of HTTP response (e.g. GET /index.html HTTP/1.1)
     * @throws IllegalArgumentException
     */
    private void parseFirstLine(String line) throws IllegalArgumentException{
        String[] splitLine = line.split(" ");

        if (splitLine.length != 3) {
            throw new IllegalArgumentException("Invalid HTTP Request");
        }

        operation_ = splitLine[0];
        resource_ = splitLine[1];
    }

    public String getResource() {
        return resource_;
    }

    /**
     * Check if a given header is contained in the parsed HTTP request
     * @param header header string to check for
     * @return true if header exists, otherwise false.
     */
    public boolean hasHeader(String header){
        return headers_.containsKey(header);
    }

    /**
     * Get the value of a header contained in the HTTP request
     * @param header header to fetch the value of
     * @return the value of the header
     */
    public String getHeaderValue(String header){
        return headers_.get(header);
    }

    Map<String, String> headers_ = new HashMap<>();
    String operation_;
    String resource_;
}
