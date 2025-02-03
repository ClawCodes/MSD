import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) throws IOException {
        InetAddress googleDNS = InetAddress.getByName("8.8.8.8");
        DatagramSocket socket = new DatagramSocket(5678);
        byte[] buffer = new byte[512];
        DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);
        DNSCache cache = new DNSCache();
        while (true) {
            socket.receive(pkt);
            DNSMessage message = DNSMessage.decodeMessage(pkt.getData());
            DNSQuestion question = message.getQuestions().get(0);
            DNSRecord answer = cache.get(question);

            if (answer == null) {
                // Fetch from google
                DatagramPacket forwardPkt = new DatagramPacket(pkt.getData(), pkt.getLength(), googleDNS, 53);

                DatagramSocket forwardSocket = new DatagramSocket(); // New socket for forwarding
                forwardSocket.send(forwardPkt);

                DatagramPacket responsePkt = new DatagramPacket(buffer, buffer.length);
                forwardSocket.receive(responsePkt); // await response from google

                DNSMessage googleResponse = DNSMessage.decodeMessage(responsePkt.getData());
                // Save response in cache
                DNSQuestion googleQuestion = googleResponse.getQuestions().get(0);
                DNSRecord googleAnswer = googleResponse.getAnswers().get(0);
                googleAnswer.increaseExpiration(3600); // Keep in cache for at least an hour
                cache.add(googleQuestion, googleAnswer);

                // Send response back to original client
                DatagramPacket clientResponse = new DatagramPacket(responsePkt.getData(), responsePkt.getLength(),
                        pkt.getAddress(), pkt.getPort());
                socket.send(clientResponse); // forward google's response to client
                forwardSocket.close();
            } else {
                DNSRecord[] ans = new DNSRecord[1];
                ans[0] = answer;
                DNSMessage responseMsg = DNSMessage.buildResponse(message, ans);
                byte[] response = responseMsg.toBytes();
                // Send message back to client
                DatagramPacket clientResponse = new DatagramPacket(response, response.length,
                        pkt.getAddress(), pkt.getPort());
                socket.send(clientResponse);
            }
        }
    }
}
