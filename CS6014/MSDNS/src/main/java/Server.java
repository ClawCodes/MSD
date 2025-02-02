import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Server {
    public static void writeBytes(String name, byte[] bytes) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(name)) {
            fos.write(bytes);
        }
    }

    public static void main(String[] args) throws IOException {
        InetAddress googleDNS = InetAddress.getByName("8.8.8.8");
        DatagramSocket socket = new DatagramSocket(5678);
        byte[] buffer = new byte[512];
        DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);
        while (true) {
            socket.receive(pkt);
            Server.writeBytes("dig_request", pkt.getData());
            DNSMessage message = DNSMessage.decodeMessage(pkt.getData());
            // if message.QR == 0 then process as query
            // Check cache for the answer
            // if answer does not exist then ask google for answer (jump to handle answer message)
            // if message.QR == 1 then process as answer
            // Save question and answer to cache
            // Forward answer to client
            // TODO: check cache - if not in cache run the following
            // Forward to 8.8.8.8
            DatagramPacket forwardPkt = new DatagramPacket(pkt.getData(), pkt.getLength(), googleDNS, 53);

            DatagramSocket forwardSocket = new DatagramSocket(); // New socket for forwarding
            forwardSocket.send(forwardPkt);

            DatagramPacket responsePkt = new DatagramPacket(buffer, buffer.length);
            forwardSocket.receive(responsePkt); // await response from google

            // Send response back to original client
            DatagramPacket clientResponse = new DatagramPacket(responsePkt.getData(), responsePkt.getLength(),
                    pkt.getAddress(), pkt.getPort());
            socket.send(clientResponse); // forward google's response to client

            forwardSocket.close();
        }
    }
}
