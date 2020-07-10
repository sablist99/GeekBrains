import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleClient implements ClientsConstants {
    Socket socket;
    PrintWriter writer;


    SimpleClient() {
        try {
            socket = new Socket(SERVER_ADDR, SERVER_PORT);//Нужно для подключения к серверу
                                               //по адресу SERVER_ADDR с портом SERVER_PORT
            System.out.println(CONNECT_TO_SERVER);

            //Создаем райтер для отправки сообщений на сервер
            writer = new PrintWriter(socket.getOutputStream());
            writer.println(getLoginAndPassword()); //Отправка данных на сервер в формате: auth <login> <passwd>
            writer.flush();//Очистка буфера

            //Поток для чтения с сервера
            Thread t_reader = new Thread(new ClientHandlerReader(socket));

            //Поток для отправки сообщений на сервер
            Thread t_writer = new Thread(new ClientHandlerWriter(socket));

            t_reader.start();
            t_writer.start();

            t_reader.join();
            t_writer.join();
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(CONNECT_CLOSED);
    }

    class ClientHandlerReader implements Runnable {
        BufferedReader reader;
        Socket socket;
        String message;

        ClientHandlerReader(Socket clientSocket) {
            try {
                //Получаем объект сервера
                socket = clientSocket;

                //Создаем ридер для чтения из сервера
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println(message);
                    if (message.equals(AUTH_FAIL))
                        System.exit(-1); // terminate client
                }
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    String getLoginAndPassword() {
        Scanner scanner = new Scanner(System.in); //Ввод с клавиатуры
        System.out.print(LOGIN_PROMPT);
        String login = scanner.nextLine();
        System.out.print(PASSWD_PROMPT);
        String pass = scanner.nextLine();
        return AUTH_SIGN + " " + login + " " + pass;
    }

    class ClientHandlerWriter implements Runnable {
        Socket socket;
        Scanner scanner;

        ClientHandlerWriter(Socket clientSocket) {
            try {
                socket = clientSocket;
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        @Override
        public void run() {
            String message;
            scanner = new Scanner(System.in); //Ввод с клавиатуры
            try {
                do {
                    message = scanner.nextLine();
                    writer.println(message);
                    writer.flush();
                } while (!message.equalsIgnoreCase(EXIT_COMMAND));
                socket.close();
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
