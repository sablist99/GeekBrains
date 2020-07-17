import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



class Chat_window extends JFrame implements ChatConstants {


    private JTextArea dialogue;
    private JTextField message;
    private JRadioButton pvt_msg_btn;
    private JComboBox list_of_client;
    private PrintWriter writer;
    private BufferedReader reader;
    private String LaP;
    private boolean isRun;

    void Set_lap(String l) {
        LaP = l;
    }


    Chat_window() {
        setTitle(TITLE);
        setResizable(false);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//Объявляем объект dimension, чтобы получить размеры экрана
        setBounds((dim.width - WINDOW_WIDTH) / 2, (dim.height - WINDOW_HEIGHT) / 2, WINDOW_WIDTH, WINDOW_HEIGHT);//Устанавливаем размеры экрана
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//Завершение работы приложения при закрытии формы

        //Оформление окна с диалогом
        dialogue = new JTextArea();
        dialogue.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(dialogue);

        //Оформление нижней панели
        JPanel jp = new JPanel();
        JPanel jp_top = new JPanel();
        JPanel jp_bottom = new JPanel();

        jp_top.setLayout(new BoxLayout(jp_top, BoxLayout.X_AXIS));
        jp_bottom.setLayout(new BoxLayout(jp_bottom, BoxLayout.X_AXIS));
        jp.setLayout(new BorderLayout());

        message = new JTextField();
        JButton enter = new JButton(BTN_ENTER);
        pvt_msg_btn = new JRadioButton(PVT_MSG);
        list_of_client = new JComboBox();
        final JButton update_list = new JButton(UPDATE_LIST);

        jp_top.add(pvt_msg_btn);
        jp_top.add(list_of_client);
        jp_top.add(update_list);
        jp_bottom.add(message);
        jp_bottom.add(enter);

        list_of_client.setEnabled(false);
        update_list.setEnabled(false);

        jp.add(BorderLayout.NORTH, jp_top);
        jp.add(BorderLayout.SOUTH, jp_bottom);

        //Оформление верхней панели
        JMenuBar menuBar = new JMenuBar();

        JMenu options = new  JMenu("Опции");

        JMenuItem pvt_msg_menuBar = new JMenuItem("Приватное сообщение");
        options.add(pvt_msg_menuBar);

        JMenuItem client_list = new JMenuItem("Обновить список собеседников");
        options.add(client_list);

        JMenu close_connection = new JMenu("Разорвать соединение и выйти");
        JMenuItem close_connection_item = new JMenuItem("Разорвать соединение и выйти");
        close_connection.add(close_connection_item);

        menuBar.add(options);
        menuBar.add(close_connection);


        //Добавление элементов в окно
        add(BorderLayout.CENTER, scrollPane);
        add(BorderLayout.SOUTH, jp);
        add(BorderLayout.NORTH, menuBar);

        message.requestFocusInWindow();

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (writer != null){
                    //Это сообщение служит командой серверу для отключения от чата
                    writer.print("\n");
                }
            }
        });

        close_connection_item.addActionListener ( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                System.exit(0);
            }
        });

        message.addActionListener ( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                if(isRun)
                    Send_message();
            }
        });

        enter.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                if(isRun)
                    Send_message();
            }
        });

        pvt_msg_btn.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                if(pvt_msg_btn.isSelected()) {
                    list_of_client.setEnabled(true);
                    update_list.setEnabled(true);
                } else {
                    list_of_client.setEnabled(false);
                    update_list.setEnabled(false);
                }
                message.requestFocusInWindow();

            }
        });

        pvt_msg_menuBar.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                pvt_msg_btn.setSelected(true);
                list_of_client.setEnabled(true);
                update_list.setEnabled(true);
                message.requestFocusInWindow();

            }
        });

        update_list.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                writer.println(UPDATE_LIST_MSG);
                writer.flush();
            }
        });

        client_list.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                writer.println(UPDATE_LIST_MSG);
                writer.flush();
            }
        });

        new_client();
    }

    private void Connect(String login, String password) {
        try {
            //Нужно для подключения к серверу по адресу SERVER_ADDR с портом SERVER_PORT
            Socket socket = new Socket(SERVER_ADDR, SERVER_PORT);

            //Создаем райтер для отправки сообщений на сервер
            writer = new PrintWriter(socket.getOutputStream());

            //Первое сообщение серверу гарантированно пара логин-пароль
            writer.println(login + " " + password); //Отправка данных на сервер в формате: <login> <passwd>
            writer.flush();//Очистка буфера

            //Создаем ридер для чтения из сервера
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "Windows-1251"));

            //Получаем ответ на авторизацию
            String answer = reader.readLine();

            if (answer.equals("1")) {
                dialogue.append(CONNECT_TO_SERVER + "\n");
                System.out.println(CONNECT_TO_SERVER);

                //Этот флаг позволяет отправлять сообщения
                isRun = true;

                //Поток для чтения с сервера
                Thread t_reader = new Thread(new ServerListener());
                t_reader.start();
                t_reader.join();

                dialogue.append(CONNECT_CLOSED + "\n");
                System.out.println(CONNECT_CLOSED);
            } else {
                socket.close();
                writer = null;
                reader = null;
                dialogue.append(NOT_CONNECT_TO_SERVER + "\n");
                isRun = false;
                new_client();
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void new_client() {
        //Окно ввода логина и пароля
        new Autorization_window(this);
        LaP = "";
        while (true)
        {
            //Ожидаем получения логина и пароля
            if(!LaP.equals("")) {
                String[] arr = LaP.split(" ");
                Connect(arr[0], arr[1]);
                break;
            }
            else
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
        }
    }

    class ServerListener implements Runnable {
        String message_string;

        @Override
        public void run() {
            try {
                while ((message_string = reader.readLine()) != null) {
                    if (!message_string.equals("\0")) {
                        if (message_string.equals(BEGIN_OF_NAME)) {
                            list_of_client.removeAllItems();
                            while (!(message_string = reader.readLine()).equals(END_OF_NAME)) {
                                list_of_client.addItem(message_string);
                            }
                        }
                        else
                            dialogue.append(message_string + "\n");
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void Send_message() {

            if (!message.getText().trim().equals("") && !message.getText().trim().equals("\n")) {
                try {
                    if (pvt_msg_btn.isSelected() && list_of_client.getSelectedItem() != null)
                        writer.println("/w " + list_of_client.getSelectedItem().toString() + " " + message.getText());
                    else
                        writer.println(message.getText());
                    writer.flush();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            message.setText("");
            message.requestFocusInWindow();

    }

}
