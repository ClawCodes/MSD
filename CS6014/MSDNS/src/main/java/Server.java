import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class Server {
    static final int server_port = 5678;

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(server_port);
        byte[] buffer = new byte[512];
        DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);
        while (true) {
            socket.receive(pkt);
            ByteArrayInputStream inStream = new ByteArrayInputStream(pkt.getData());
            DNSHeader header = DNSHeader.decodeHeader(inStream);
        }
    }
}
