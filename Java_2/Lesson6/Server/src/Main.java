import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {

    final int SERVER_PORT = 2048;
    final String SERVER_START = "Server is started...";
    final String SERVER_STOP = "Server stopped.";
    final String CLIENT_JOINED = "Client joined.";
    final String CLIENT_DISCONNECTED = "Disconnected";
    final String EXIT_COMMAND = "exit"; // command for exit

    ServerSocket server;
    Socket socket;

    public static void main(String[] args) {
        new Main();
    }

    Main() {
        System.out.println(SERVER_START);
        try {
            server = new ServerSocket(SERVER_PORT);
            socket = server.accept();
            System.out.println(CLIENT_JOINED);
            Thread treader = new Thread(new ClientHandlerReader(socket));
            Thread twriter = new Thread(new ClientHandlerWriter(socket));
            treader.start();
            twriter.start();
            treader.join();
            //twriter.join();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(SERVER_STOP);
    }

    class ClientHandlerReader implements Runnable {
        BufferedReader reader;
        Socket socket;

        ClientHandlerReader(Socket clientSocket) {
            try {
                socket = clientSocket;
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        @Override
        public void run() {
            String message;
            try {
                do {
                    message = reader.readLine();
                    System.out.println("Client: " + message);
                } while (!message.equalsIgnoreCase(EXIT_COMMAND));
                socket.close();
                System.out.println(CLIENT_DISCONNECTED);
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    class ClientHandlerWriter implements Runnable {
        PrintWriter writer;
        Socket socket;
        Scanner scanner;

        ClientHandlerWriter(Socket clientSocket) {
            try {
                socket = clientSocket;
                writer = new PrintWriter(socket.getOutputStream());
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        @Override
        public void run() {
            String message;
            scanner = new Scanner(System.in); // for keyboard input
            try {
                do {
                    message = scanner.nextLine();
                    writer.println(message);
                    writer.flush();
                } while (!message.equalsIgnoreCase(EXIT_COMMAND));
                socket.close();
                System.out.println(CLIENT_DISCONNECTED);
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
