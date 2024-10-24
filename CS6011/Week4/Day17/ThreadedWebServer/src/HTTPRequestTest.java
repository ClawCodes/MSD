import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class HTTPRequestTest {
    @Test
    public void testGetWithResource(){
        // Arrange
        String request = "GET /test.txt HTTP/1.1\r\nHost: test.com\r\nAccept: text/json\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());

        // Act
        HTTPRequest parsedRequest = new HTTPRequest(inputStream);

        // Assert
        Assertions.assertEquals("/test.txt", parsedRequest.getResource());
        Assertions.assertTrue(parsedRequest.hasHeader("Host"));
        Assertions.assertEquals("test.com", parsedRequest.getHeaderValue("Host"));
        Assertions.assertTrue(parsedRequest.hasHeader("Accept"));
        Assertions.assertEquals("text/json", parsedRequest.getHeaderValue("Accept"));
    }
    @Test
    public void testPutWithResourceWithBody(){
        // Arrange
        String request = "PUT /test.txt HTTP/1.1\r\nHost: test.com\r\nAccept: text/json\r\n\r\nsome text\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());

        // Act
        HTTPRequest parsedRequest = new HTTPRequest(inputStream);

        // Assert
        Assertions.assertEquals("/test.txt", parsedRequest.getResource());
        Assertions.assertTrue(parsedRequest.hasHeader("Host"));
        Assertions.assertEquals("test.com", parsedRequest.getHeaderValue("Host"));
        Assertions.assertTrue(parsedRequest.hasHeader("Accept"));
        Assertions.assertEquals("text/json", parsedRequest.getHeaderValue("Accept"));
    }
}