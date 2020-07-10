import java.io.*;
import java.net.*;
import java.util.*;

public class Main {

    final String SERVER_ADDR = "127.0.0.1"; // or "localhost"
    final int SERVER_PORT = 2048;
    final String CLIENT_PROMPT = "$ ";
    final String CONNECT_TO_SERVER = "Connection to server established.";
    final String CONNECT_CLOSED = "Connection closed.";
    final String EXIT_COMMAND = "exit"; // command for exit

    BufferedReader reader;
    Socket socket;
    Scanner scanner;
    PrintWriter writer;
    String message;
    String message_from_server;

    public static void main(String[] args) {
        new Main();
    }

    Main() {
        scanner = new Scanner(System.in); // for keyboard input
        try {
            socket = new Socket(SERVER_ADDR, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println(CONNECT_TO_SERVER);
            new Thread(new ServerWriter()).start();
            do {
                //System.out.print(CLIENT_PROMPT);
                message = scanner.nextLine();
                writer.println(message);
                writer.flush();
            } while (!message.equals(EXIT_COMMAND));
            writer.close();
            socket.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(CONNECT_CLOSED);
    }

    class ServerWriter implements Runnable {

        ServerWriter() {
            try {
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                do {
                    message_from_server = reader.readLine();
                    System.out.println("Server: " + message_from_server);
                } while (!message_from_server.equalsIgnoreCase(EXIT_COMMAND));
                writer.close();
                socket.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
