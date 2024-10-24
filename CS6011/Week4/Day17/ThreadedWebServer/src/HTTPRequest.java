import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HTTPRequest {
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
                System.out.println(requestLine);
                if (splitLine.length != 2) // Assumes not header
                    continue;
                headers_.put(splitLine[0], splitLine[1]);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error when attempting to handle request: ");
            e.printStackTrace();
        }
    }

    private void parseFirstLine(String line) throws IllegalArgumentException{
        String[] splitLine = line.split(" ");

        if (splitLine.length != 3) {
            throw new IllegalArgumentException("Invalid HTTP Request");
        }

        operation_ = splitLine[0];
        resource_ = splitLine[1];
    }

    String getResource() {
        return resource_;
    }

    boolean hasHeader(String header){
        return headers_.containsKey(header);
    }

    String getHeaderValue(String header){
        return headers_.get(header);
    }

    Map<String, String> headers_ = new HashMap<>();
    String operation_;
    String resource_;
}
