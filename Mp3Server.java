import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Mp3Server extends DatagramSocket {
    private static final int BUFFER_SIZE = 1024;

    public Mp3Server(int port) throws IOException {
        super(port);
    }

    public void sendMp3(String fileName, InetAddress address, int port) throws IOException {
        System.out.println("Sending: " + fileName);
        FileInputStream fis = new FileInputStream("./files/" + fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);

        byte[] buffer = new byte[BUFFER_SIZE];
        int count;

        while ((count = bis.read(buffer)) > 0) {
            DatagramPacket packet = new DatagramPacket(buffer, count, address, port);
            send(packet);
        }

        bis.close();
    }

    public static void main(String[] args) throws IOException {
        Mp3Server server = new Mp3Server(10000);

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    server.receive(packet);

                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    String request = new String(packet.getData(), 0, packet.getLength());

                    server.sendMp3(request, address, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
