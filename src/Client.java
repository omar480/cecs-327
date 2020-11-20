import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.net.DatagramSocket;

public class Client {
    public static void main(String[] args) {
        Scanner reader = null;
        DatagramSocket socket = null;

        try {
            reader = new Scanner(System.in);
            String port = null;
            String ip = null;

            if (args.length == 2) {
                port = args[0];
                ip = args[1];

                if (!validatePort(port) || !validateIp(ip)) {
                    throw new Exception("Pass valid arguments! Try: java Client <port> <ip address>");
                }
            } else {
                while (true) {
                    System.out.print("Enter the port number: ");
                    port = reader.nextLine();

                    if (!validatePort(port)) {
                        System.out.println("Enter a valid port!");
                        continue;
                    }
                    break;
                }

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
            socket = new DatagramSocket();

            System.out.print("Message to send to server: ");
            String msg = reader.nextLine();

            InetAddress host = InetAddress.getByName(ip);
            DatagramPacket request = new DatagramPacket(msg.getBytes(), msg.getBytes().length, host,
                    Integer.parseInt(port));

            socket.send(request);

            byte[] buffer = new byte[1000];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);

            displayResponseInfo(response);

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

    private static void displayResponseInfo(DatagramPacket res) {
        System.out.println("\n**********************");
        System.out.println("* Data from Server   *");
        System.out.println("**********************");
        System.out.println("Message: " + new String(res.getData()).trim()
            + "\nIP: " + res.getAddress()
            + "\nSize: " + res.getLength());
    }


    private static boolean validateIp(String ip) {
        String ipRegex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
                + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipRegex);
    }

    private static boolean validatePort(String port) {
        String portRegex = "(^[1-9]{4}$)";
        return port.matches(portRegex);
    }
}

// class InvalidPortException extends Exception {
// private static final long serialVersionUID = 1L;

// InvalidPortException(String msg) {
// super(msg);
// }
// }

// class InvalidIPException extends Exception {
// private static final long serialVersionUID = 1L;

// InvalidIPException(String msg) {
// super(msg);
// }
// }
