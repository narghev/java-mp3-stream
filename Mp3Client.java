import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Mp3Client {
    private static final int BUFFER_SIZE = 1024;
    private static final int PORT = 10000;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Mp3Client fileName");
            return;
        }

        String fileName = args[0];
        InetAddress address = InetAddress.getLocalHost();
        DatagramSocket socket = new DatagramSocket();

        byte[] buffer = fileName.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
        socket.send(packet);

        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        while (true) {
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            int count = packet.getLength();
            if (count < 0) {
                break;
            }
						
						try {
							Mp3Player.play(buffer);	
						} catch(Exception e) {}
						
            bos.write(buffer, 0, count);
        }

        bos.close();
        socket.close();
    }
}
