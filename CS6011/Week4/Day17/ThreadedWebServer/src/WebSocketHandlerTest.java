import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Arrays;

class WebSocketHandlerTest {
    @Test
    public void testreadFrameBaseLengthMasked() throws Exception {
        // ASCII Hello: 0x48 0x65 0x6C 0x6C 0x6F
        // Mask key: 0x37FA213D
        byte[] inputFrame = new byte[] {
                (byte)0x81, // FIN: 1, Opcode 0x1
                (byte)0x85, // Masked with payload of length 5 (i.e. "Hello")
                (byte)0x37, (byte)0xFA, (byte)0x21, (byte)0x3D, // Random masking key
                (byte)0x7F, (byte)0x9F, (byte)0x4D, (byte)0x51, (byte)0x58 // Masked payload
        };

        DataInputStream inStream =  new DataInputStream(new ByteArrayInputStream(inputFrame));

        String actual = WebSocketHandler.readFrame(inStream);

        Assertions.assertEquals("Hello", actual);
    }

    @Test
    public void testreadFrameBaseLengthUnMasked() throws Exception {
        // ASCII Hello: 0x48 0x65 0x6C 0x6C 0x6F
        byte[] inputFrame = new byte[] {
                (byte)0x81, // FIN: 1, Opcode 0x1
                (byte)0x05, // Unasked with payload of length 5 (i.e. "Hello")
                (byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F // Unmasked payload
        };

        DataInputStream inStream =  new DataInputStream(new ByteArrayInputStream(inputFrame));

        String actual = WebSocketHandler.readFrame(inStream);

        Assertions.assertEquals("Hello", actual);
    }

    @Test
    public void testreadFrameSentence() throws Exception {
        byte[] inputFrame = new byte[] {
                (byte)0x81, // FIN: 1, Opcode 0x1
                (byte)0x8F, // Masked with payload of length 15 (i.e. "This is a test!")
                (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78, // Random mask
                // Masked sentence ("This is a test!")
                (byte)0x46, (byte)0x5C, (byte)0x3F, (byte)0x0B, (byte)0x32,
                (byte)0x5D, (byte)0x25, (byte)0x58, (byte)0x73, (byte)0x14,
                (byte)0x22, (byte)0x1D, (byte)0x61, (byte)0x40, (byte)0x77
        };

        DataInputStream inStream =  new DataInputStream(new ByteArrayInputStream(inputFrame));

        String actual = WebSocketHandler.readFrame(inStream);

        Assertions.assertEquals("This is a test!", actual);

    }
    @Test
    public void testreadFrameSentenceUnMasked() throws Exception {
        byte[] inputFrame = new byte[] {
                (byte)0x81, // FIN: 1, Opcode 0x1
                (byte)0x0F, // Unasked with payload of length 15 (i.e. "This is a test!")
                (byte)0x54, (byte)0x68, (byte)0x69, (byte)0x73, (byte)0x20,
                (byte)0x69, (byte)0x73, (byte)0x20, (byte)0x61, (byte)0x20,
                (byte)0x74, (byte)0x65, (byte)0x73, (byte)0x74, (byte)0x21
        };

        DataInputStream inStream =  new DataInputStream(new ByteArrayInputStream(inputFrame));

        String actual = WebSocketHandler.readFrame(inStream);

        Assertions.assertEquals("This is a test!", actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 220}) // FIXME: 220 not parsing
    public void testCreateFrame(int payloadLength) throws Exception {
        char[] chars = new char[payloadLength];
        Arrays.fill(chars, (char) 'a');
        String payload = new String(chars);

        byte[] inputFrame = WebSocketHandler.createFrame(payload, OpCode.TEXT);
        DataInputStream s = new DataInputStream(new ByteArrayInputStream(inputFrame));
        String actual = WebSocketHandler.readFrame(s);

        Assertions.assertEquals(payload, actual);
    }
}