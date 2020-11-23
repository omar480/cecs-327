import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    static final int BUFFER_SIZE = 1000;

    public static void main(String[] args) {
        Scanner reader = null;
        DatagramSocket socket = null;

        try {
            reader = new Scanner(System.in);
            String port = null;
            String ip = null;

            // only for when port and ip are passed as arguments
            if (args.length == 2) {
                port = args[0];
                ip = args[1];

                if (!validatePort(port) || !validateIp(ip)) {
                    throw new Exception("Pass valid arguments! Try: java Client <port> <ip address>");
                }
            } else {
                // input for port
                while (true) {
                    System.out.print("Enter the port number: ");
                    port = reader.nextLine();

                    if (!validatePort(port)) {
                        System.out.println("Enter a valid port!");
                        continue;
                    }
                    break;
                }

                // input for ip
                while (true) {
                    System.out.print("Enter the ip address: ");
                    ip = reader.nextLine();

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

                // if ip was entered as a name, conver it to a
                InetAddress host = InetAddress.getByName(ip);

                // request to server, send message
                DatagramPacket request = new DatagramPacket(
                    msg.getBytes(),
                    msg.getBytes().length,
                    host,
                    Integer.parseInt(port)
                );
                socket.send(request);

                // will hold the data from the server
                byte[] buffer = new byte[BUFFER_SIZE];

                // response from server, getting message
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);

                // display the response from the server
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

    /**
     * Displays the response from the server.
     *
     * @param res is the response.
     */
    private static void displayResponseInfo(DatagramPacket res) {
        System.out.println("\n**********************");
        System.out.println("* Data from Server   *");
        System.out.println("**********************");
        System.out.println("Message: " + new String(res.getData()).trim() + "\nSize: " + res.getLength());
    }

    /**
     * Validate ipv4 addresses.
     *
     * @param ip the ipv4 address.
     * @return true if the ip is a valid ipv4 address.
     */
    private static boolean validateIp(String ip) {
        String ipRegex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
                + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipRegex) || ip.equals("localhost");
    }

    /**
     * Validate port numbers. The port range is [1-65535].
     *
     * @param port is the port number.
     * @return true if the port is valid.
     */
    private static boolean validatePort(String port) {
        String portRegex = "^([1-9]|[1-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9][0-9][0-9]|[1-6][0-5][0-6][0-3][0-5])$";
        return port.matches((portRegex));
    }
}
