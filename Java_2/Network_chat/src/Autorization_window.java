import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Autorization_window extends JFrame implements ChatConstants{

    JTextField login;
    JTextField password;
    JButton enter;
    JButton exit;




    Autorization_window (final Chat_window chat_window) {
        setTitle(AUTH_TITLE);
        setResizable(false);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//Объявляем объект dimension, чтобы получить размеры экрана
        setBounds((dim.width - WINDOW_AUTH_WIDTH) / 2, (dim.height - WINDOW_AUTH_HEIGHT) / 2, WINDOW_AUTH_WIDTH, WINDOW_AUTH_HEIGHT);//Устанавливаем размеры экрана

        JPanel panel = new JPanel();
        panel.setLayout(null);


        login = new JTextField("Логин");
        password = new JTextField("Пароль");
        enter = new JButton("Авторизоваться");
        exit = new JButton("Выход");

        login.setBounds(MARGIN, MARGIN, ELEMENT_WIDTH, ELEMENT_HEIGHT);
        password.setBounds(MARGIN, 2 * MARGIN + ELEMENT_HEIGHT,  ELEMENT_WIDTH, ELEMENT_HEIGHT);
        exit.setBounds(MARGIN, 3 * MARGIN + 2 * ELEMENT_HEIGHT, ELEMENT_WIDTH / 2, ELEMENT_HEIGHT);
        enter.setBounds((WINDOW_AUTH_WIDTH - MARGIN) / 2, 3 * MARGIN + 2 * ELEMENT_HEIGHT, ELEMENT_WIDTH / 2 - MARGIN, ELEMENT_HEIGHT);

        panel.add(login);
        panel.add(password);
        panel.add(exit);
        panel.add(enter);

        add(panel);

        enter.addActionListener ( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                chat_window.Set_lap(login.getText().trim() + " " + password.getText().trim());
                setVisible(false);
                dispose();
            }
        });

        exit.addActionListener ( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                System.exit(0);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

}
