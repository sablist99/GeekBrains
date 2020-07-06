import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Chat_window extends JFrame {
    final int WIDTH = 600;
    final int HEIGHT = 800;
    final String TITLE = "Сетевой чат";
    final String BTN_ENTER = "Отправить";

    JTextArea dialogue;
    JTextField message;
    JButton enter;
    Date date;
    SimpleDateFormat formatForDateNow;

    public Chat_window() {
        setTitle(TITLE);
        setResizable(false);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//Объявляем объект dimension, чтобы получить размеры экрана
        setBounds((dim.width - WIDTH) / 2, (dim.height - HEIGHT) / 2, WIDTH, HEIGHT);//Устанавливаем размеры экрана
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//Завершение работы приложения при закрытии формы

        //Оформление окна с диалогом
        dialogue = new JTextArea();
        dialogue.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(dialogue);

        //Оформление нижней панели
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
        message = new JTextField();
        enter = new JButton(BTN_ENTER);
        jp.add(message);
        jp.add(enter);
        message.addActionListener ( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                Send_message();
            }
        });
        enter.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                Send_message();
            }
        });

        //Добавление элементов в окно
        add(BorderLayout.CENTER, scrollPane);
        add(BorderLayout.SOUTH, jp);


        setVisible(true);
    }

    private void Send_message() {
        if ( !message.getText().equals("")) {
            date = new Date();
            formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
            dialogue.append(formatForDateNow.format(date) + ": \n"
                    + "        Вы: " + message.getText() + "\n");
            message.setText("");
        }
    }
}
