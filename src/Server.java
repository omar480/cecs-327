import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
    static final int PORT = 6789;
    static final int BUFFER_SIZE = 1000;

    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(PORT);
            byte[] buffer = new byte[BUFFER_SIZE];

            System.out.println("Server executed. Running on port " + PORT);

            while (true) {
                System.out.println("\nWaiting for a packet...");
                DatagramPacket received = new DatagramPacket(buffer, BUFFER_SIZE);
                socket.receive(received);

                displayRequestInfo(received);

                DatagramPacket response = new DatagramPacket(
                    received.getData(),
                    received.getLength(),
                    received.getAddress(),
                    received.getPort()
                );

                socket.send(response);
                buffer = new byte[BUFFER_SIZE];
            }

        } catch (SocketException e) {
            System.out.println(Server.class.getName() + " : SOCKET : " + e.getMessage());
        } catch (IOException e) {
            System.out.println(Server.class.getName() + " : IO : " + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private static void displayRequestInfo(DatagramPacket req) {
        System.out.println("**********************");
        System.out.println("* Data from Client   *");
        System.out.println("**********************");
        System.out.println("Message: " + new String(req.getData()).trim()
            + "\nIP: " + req.getAddress()
            + "\nSize: " + req.getLength());
    }

    private static byte[] initBuffer() {
        return new byte[BUFFER_SIZE];
    }
}
