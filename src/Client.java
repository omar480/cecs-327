import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

// creating a class for client
public class Client {
    // declaring static variable for buffer_size
    static final int BUFFER_SIZE = 1000;

    public static void main(String[] args) {
        Scanner reader = null;
        DatagramSocket socket = null;

        /* 
        * Try statement will test for errors while executing 
        * catch statment will handle the errors 
        */
        try {
            reader = new Scanner(System.in);
            String port = null;
            String ip = null;
            // checking for error handing if invalid input
            if (args.length == 2) {
                port = args[0];
                ip = args[1];
              
                if (!validatePort(port) || !validateIp(ip)) {
                    throw new Exception("Pass valid arguments! Try: java Client <port> <ip address>");
                }
            } else {
                while (true) {
                    // if condition is met, proceed for port number
                    System.out.print("Enter the port number: ");
                    port = reader.nextLine();
                    // checking if port number is valid
                    if (!validatePort(port)) {
                        System.out.println("Enter a valid port!");
                        continue;
                    }
                    break;
                }
                // prompt for ip address
                while (true) {
                    System.out.print("Enter the ip address: ");
                    ip = reader.nextLine();
                    // error handling message
                    if (!validateIp(ip)) {
                        System.out.println("Enter a valid ip!");
                        continue;
                    }
                    break;
                }
            }

            System.out.println("Starting the client...");

            while (true) {
                socket = new DatagramSocket();

                System.out.print("\nMessage to send to server: ");
                String msg = reader.nextLine();

                InetAddress host = InetAddress.getByName(ip);
                // get msg in bytes and length
                DatagramPacket request = new DatagramPacket(
                    msg.getBytes(),
                    msg.getBytes().length,
                    host,
                    Integer.parseInt(port));

                socket.send(request);

                // buffer for incoming data
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                // receiving the datagram packet
                socket.receive(response);
                displayResponseInfo(response);
            }
        } catch (SocketException e) {
            System.out.println(Client.class.getName() + " : " + e.getMessage());
        } catch (Exception e) {
            System.out.println(Client.class.getName() + " : " + e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }
    // display response information for client: IP and Size
    private static void displayResponseInfo(DatagramPacket res) {
        System.out.println("\n**********************");
        System.out.println("* Data from Server   *");
        System.out.println("**********************");
        System.out.println("Message: " + new String(res.getData()).trim()
            + "\nIP: " + res.getAddress()
            + "\nSize: " + res.getLength());
    }
    // check if ip meeting conditional requirements
    private static boolean validateIp(String ip) {
        String ipRegex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
            + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
            + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
            + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipRegex) || ip.equals("localhost");
    }

    // check if port meets conditional requirements
    private static boolean validatePort(String port) {
        String portRegex = "(^[1-9]{4}$)";
        return port.matches(portRegex);
    }
}
