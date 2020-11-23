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
            // create a datagram socket on the specified part
            socket = new DatagramSocket(PORT);

            System.out.println("Server executed. Running on port " + PORT);

            while (true) {
                System.out.println("\nWaiting for a packet...");

                // request from client
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket received = new DatagramPacket(buffer, BUFFER_SIZE);
                socket.receive(received);

                displayClientInfo(received);

                // send the response to the client. In this case, we send the same
                // message back
                DatagramPacket response = new DatagramPacket(
                    received.getData(),
                    received.getLength(),
                    received.getAddress(),
                    received.getPort()
                );
                socket.send(response);
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

    /**
     * Display the client info (the request from the client).
     *
     * @param req is the request.
     */
    private static void displayClientInfo(DatagramPacket req) {
        System.out.println("**********************");
        System.out.println("* Data from Client   *");
        System.out.println("**********************");
        System.out.println("Message: " + new String(req.getData()).trim()
            + "\nIP: " + req.getAddress()
            + "\nSize: " + req.getLength());
    }
}
