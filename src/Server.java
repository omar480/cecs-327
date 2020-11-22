import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


// creatting a class for server
public class Server {

    // Declaring static variables 
    static final int PORT = 6789;
    static final int BUFFER_SIZE = 1000;

    public static void main(String[] args) {
        DatagramSocket socket = null;

        /* 
        * Try statement will test for errors while executing 
        * catch statment will handle the errors 
        */
        try {
            // initalizing an object
            socket = new DatagramSocket(PORT);
            byte[] buffer = new byte[BUFFER_SIZE];

            System.out.println("Server executed. Running on port " + PORT);

            // 
            while (true) {
                System.out.println("\nWaiting for a packet...");
                DatagramPacket received = new DatagramPacket(buffer, BUFFER_SIZE);
                socket.receive(received);

                displayRequestInfo(received);

                // Getters for datagram packet
                DatagramPacket response = new DatagramPacket(
                    received.getData(),
                    received.getLength(),
                    received.getAddress(),
                    received.getPort()
                );

                socket.send(response);
                buffer = new byte[BUFFER_SIZE];
            }
          // error handling 
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
    // displaying data from client: IP and Size
    private static void displayRequestInfo(DatagramPacket req) {
        System.out.println("**********************");
        System.out.println("* Data from Client   *");
        System.out.println("**********************");
        System.out.println("Message: " + new String(req.getData()).trim()
            + "\nIP: " + req.getAddress()
            + "\nSize: " + req.getLength());
    }
}
