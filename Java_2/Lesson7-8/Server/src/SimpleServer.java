
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

class SimpleServer extends JFrame implements ServersConstants{

    private ServerSocket server;
    private List<ClientHandler> clients;
    private JTextArea dialogue;


    private SimpleDateFormat formatForDateNow;

    SimpleServer() {

        //Оформление окна с диалогом
        setTitle(TITLE);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//Объявляем объект dimension, чтобы получить размеры экрана
        setBounds((dim.width - WINDOW_WIDTH) / 2, (dim.height - WINDOW_HEIGHT) / 2, WINDOW_WIDTH, WINDOW_HEIGHT);//Устанавливаем размеры экрана
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//Завершение работы приложения при закрытии формы
        dialogue = new JTextArea();
        dialogue.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(dialogue);
        add(BorderLayout.CENTER, scrollPane);
        setVisible(true);


        dialogue.append(SERVER_START + "\n");
        System.out.println(SERVER_START);

        //Поток для системных команд
        new Thread(new CommandHandler()).start();

        //Список для хранения текущих клиентов
        clients = new ArrayList<ClientHandler>();

        //Переменная для вывода даты
        formatForDateNow = new SimpleDateFormat(DATE_FORMAT);


        try {
            //Запускаем сервер
            server = new ServerSocket(SERVER_PORT);
            while (true) {
                //Ожидаем подключение клиента
                Socket socket = server.accept();

                //Создаем нового клиента, добавляем его в список и запускаем для его обслуживания новый поток
                ClientHandler client = new ClientHandler(socket);
                new Thread(client).start();
            }
        } catch (Exception ex) {
            dialogue.append(ex.getMessage() + "\n");
            System.out.println(ex.getMessage());
        }
        dialogue.append(SERVER_STOP + "\n");
        System.out.println(SERVER_STOP);


    }

    private boolean checkAuthentication(String l_and_p) {
        try {
            File login_and_passwoed = new File(LAPS_FILE_NAME);
            FileReader fileReader = new FileReader(login_and_passwoed);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line + " " + l_and_p);
                if (line.equals(l_and_p))
                    return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return false;
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

    private void broadcastMsg(String msg) {
        for (ClientHandler client : clients)
            client.sendMsg(msg);
    }

    private boolean Check_name(String name) {
        if (name.equals(ANONIM)) return false;

        boolean result = false;
        for (ClientHandler client : clients)
            if (name.equals(client.getName())) {
                result = true;
                break;
            }
        return result;
    }

    private ClientHandler Check_name_and_return(String name) {
        for (ClientHandler client : clients)
            if (name.equals(client.getName())) {
                return client;
            }
        return null;
    }

    class ClientHandler implements Runnable {
        BufferedReader reader;
        PrintWriter writer;
        Socket socket;
        String name;

        ClientHandler(Socket clientSocket) {
            try {
                name = ANONIM;
                socket = clientSocket;
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), "Windows-1251"));
                writer = new PrintWriter(socket.getOutputStream());

                String L_and_P = reader.readLine();
                System.out.println(L_and_P);
                String[] L_and_P_split = L_and_P.split(" ");

                //Если этот пользователь авторизирован, то прекращаем работу этого потока
                if(Check_name(L_and_P_split[0])) {
                    dialogue.append(NAME_IS_ALREADY_AUTORIZED + "\n");
                    System.out.println(NAME_IS_ALREADY_AUTORIZED);
                    socket.close();
                    return;
                }

                //Если пользователь не авторизован


                //Передаем данные для проверки логина и пароля
                if (checkAuthentication(L_and_P)) {
                    //Успешная авторизация
                    clients.add(this);
                    name = L_and_P_split[0];
                    dialogue.append("Пользователь " + name + " авторизовался\n");
                    sendMsg(1);
                } else {
                    //Провал
                    sendMsg(0);
                    dialogue.append(AUTH_FAIL + "\n");
                    socket.close();
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        String getName() {
            return name;
        }

        void sendMsg(String msg) {
            try {
                Date date = new Date();
                writer.println(formatForDateNow.format(date) + ": \n      " + msg);
                writer.flush();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        //Этот метод используется для отправки сообщений "от сервера"
        void sendMsg(int a) {
            try {
                writer.println(a);
                writer.flush();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }



        @Override
        public void run() {
            String message;
            try {
                message = reader.readLine();
                //System.out.println("'" + message + "'");
                while (!message.equals("")) {
                    dialogue.append(name + ": " + message + "\n");
                    System.out.println(name + ": " + message);

                    //Если сообщение адресовано одному пользователю
                    if (message.startsWith(PVT_MSG)) {
                        String[] buf_array = message.split(" ");
                        String for_whom = buf_array[1];
                        ClientHandler buf_client;
                        if((buf_client = Check_name_and_return(for_whom)) != null) {
                            String personal_msg = "";

                            for (int i = 2; i < buf_array.length; i++)
                                personal_msg += buf_array[i] + " ";
                            buf_client.sendMsg(name + ": " + personal_msg);
                        } else
                            sendMsg(EMPTY_NAME);
                    } else if (message.startsWith(COMMAND)) {
                        String[] buf_array = message.split(" ");
                        String command = buf_array[1];
                        if (command.equals(UPDATE_LIST_COMMAND)) {
                            writer.println(BEGIN_OF_NAME);
                            for (ClientHandler clientHandler : clients) {
                                writer.println(clientHandler.name);
                                writer.flush();
                            }
                            writer.println(END_OF_NAME);
                            writer.flush();
                            sendMsg("Обновление выполнено");
                        }
                        else sendMsg(UNKNOWN_COMMAND);
                    } else
                        broadcastMsg(name + ": " + message);
                    message = reader.readLine();
                }
                clients.remove(this); // delete client from list
                socket.close();
                dialogue.append(name + CLIENT_DISCONNECTED + "\n");
                System.out.println(name + CLIENT_DISCONNECTED);
            } catch (Exception ex) {
                //ex.printStackTrace();
                clients.remove(this); // delete client from list
                try {
                    socket.close();
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                dialogue.append(name + CLIENT_DISCONNECTED + "\n");
                System.out.println(name + CLIENT_DISCONNECTED);
            }
        }
    }
}
