import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyWindow extends JFrame {
    private JTextField jtf ;
    private JTextArea jta ;
    private final String SERVER_ADDR = "83.246.166.56" ;
    private final int SERVER_PORT = 8189 ;
    private Socket sock ;
    private Scanner in ;
    private PrintWriter out ;
    public MyWindow () {
        try {
            sock = new Socket ( SERVER_ADDR , SERVER_PORT );
            in = new Scanner ( sock . getInputStream ());
            out = new PrintWriter ( sock . getOutputStream ());
        } catch ( IOException e ) {
            e . printStackTrace ();
        }
        setBounds ( 600 , 300 , 500 , 500 );
        setTitle ( "Client" );
        setDefaultCloseOperation ( WindowConstants . EXIT_ON_CLOSE );
        jta = new JTextArea ();
        jta . setEditable ( false );
        jta . setLineWrap ( true );
        JScrollPane jsp = new JScrollPane ( jta );
        add ( jsp , BorderLayout. CENTER );
        JPanel bottomPanel = new JPanel ( new BorderLayout ());
        add ( bottomPanel , BorderLayout . SOUTH );
        JButton jbSend = new JButton ( "SEND" );
        bottomPanel . add ( jbSend , BorderLayout . EAST );
        jtf = new JTextField ();
        bottomPanel . add ( jtf , BorderLayout . CENTER );
        jbSend . addActionListener ( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                if (! jtf . getText (). trim (). isEmpty ()) {
                    sendMsg ();
                    jtf . grabFocus ();
                }
            }
        });
        jtf . addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                sendMsg ();
            }
        });
        new Thread ( new Runnable () {
            @Override
            public void run () {
                try {
                    while ( true ) {
                        if ( in . hasNext ()) {
                            String w = in . nextLine ();
                            if ( w . equalsIgnoreCase ( "end session" )) break ;
                            jta . append ( w );
                            jta . append ( "\n" );
                        }
                    }
                } catch ( Exception e ) {
                }
            }
        }). start ();
        addWindowListener ( new WindowAdapter() {
            @Override
            public void windowClosing ( WindowEvent e ) {
                super . windowClosing ( e );
                try {
                    out . println ( "end" );
                    out . flush ();
                    sock . close ();
                    out . close ();
                    in . close ();
                } catch ( IOException exc ) {
                }
            }
        });
        setVisible ( true );
    }
    public void sendMsg () {
        out . println ( jtf . getText ());
        out . flush ();
        jtf . setText ( "" );
    }
}