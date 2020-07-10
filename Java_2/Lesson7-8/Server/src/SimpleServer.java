import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class SimpleServer implements ServersConstants{

    ServerSocket server;
    Socket socket;
    List<ClientHandler> clients;
    int counter_for_name;

    SimpleServer() {
        System.out.println(SERVER_START);
        new Thread(new CommandHandler()).start();
        clients = new ArrayList<ClientHandler>();//Список для хранения текущих клиентов
        counter_for_name = 1;
        try {
            //Запускаем сервер
            server = new ServerSocket(SERVER_PORT);
            while (true) {
                //Ожидаем подключение клиента
                socket = server.accept();
                System.out.println("#" + (clients.size() + 1) + CLIENT_JOINED);

                //Создаем нового клиента, добавляем его в список и запускаем для его обслуживания новый поток
                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(SERVER_STOP);


    }

    private boolean checkAuthentication(String login, String passwd) {
        Connection connect;
        boolean result = false;
        try {
            // connect db
            Class.forName(DRIVER_NAME);
            connect = DriverManager.getConnection(SQLITE_DB);
            // looking for login && passwd in db
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_SELECT.replace("?", login));
            while (rs.next())
                result = rs.getString(PASSWD_COL).equals(passwd);
            // close all
            rs.close();
            stmt.close();
            connect.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return result;
    }

    class CommandHandler implements Runnable {
        //Процесс для ввода команд серверу

        Scanner scanner = new Scanner(System.in);

        @Override
        public void run() {
            String command;
            do
                command = scanner.nextLine();
            while (!command.equals(EXIT_COMMAND));
            try {
                server.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    void broadcastMsg(String msg) {
        for (ClientHandler client : clients)
            client.sendMsg(msg);
    }

    class ClientHandler implements Runnable {
        BufferedReader reader;
        PrintWriter writer;
        Socket socket;
        String name;

        ClientHandler(Socket clientSocket) {
            try {
                socket = clientSocket;
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
                name = "Client #" + counter_for_name;
                counter_for_name++;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        void sendMsg(String msg) {
            try {
                writer.println(msg);
                writer.flush();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        @Override
        public void run() {
            String message;
            try {
                do {
                    message = reader.readLine();
                    if (message != null) {
                        System.out.println(name + ": " + message);
                        if (message.startsWith(AUTH_SIGN)) {
                            String[] wds = message.split(" ");
                            if (checkAuthentication(wds[1], wds[2])) {
                                name = wds[1];
                                sendMsg("Hello, " + name);
                                //sendMsg("\0");
                            } else {
                                System.out.println(name + ": " + AUTH_FAIL);
                                sendMsg(AUTH_FAIL);
                                message = EXIT_COMMAND;
                            }
                        } else if (!message.equalsIgnoreCase(EXIT_COMMAND)) {
                            broadcastMsg(name + ": " + message);
                            //broadcastMsg("\0");
                        }
                    }
                } while (!message.equalsIgnoreCase(EXIT_COMMAND));
                clients.remove(this); // delete client from list
                socket.close();
                System.out.println(name + CLIENT_DISCONNECTED);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
