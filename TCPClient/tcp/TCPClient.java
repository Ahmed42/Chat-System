/**
 * The client is simple, has 2 threaded tasks , one is implemented
 * 1 - RecieveTask , implements runnable : Waits for messages to be recieved by the server
 * 2- UpdateListTask , implements Runnable : Waits for user list updates from the serveer    
 */

/**
 * Work left :
 * 1 - Add the logic for the UpdateList task
 * 2 - Complete the gui
*/

package tcp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

/**
 *
 * @author Motasim
 */
public class TCPClient extends JFrame {
    
    private final JTextField recepientName;
    private final JTextField message;
    private final JTextField Inbox;
    private final JButton sendButton;
    private final Socket connection;
    private final String userName;
    
    public TCPClient(String userName , Socket connection){
        
        setTitle(userName);
        this.connection = connection;
        this.userName = userName;    
        /*Init the compoents of the frame*/
        recepientName = new JTextField(25);
        message = new JTextField(25);
        Inbox = new JTextField(25);
        sendButton = new JButton(" Send Message ");
        setSize(400,200);
        setLayout(new FlowLayout());
        add(recepientName);
        add(message);
        add(Inbox);
        sendButton.addActionListener(new SendActionListener());
        add(sendButton);
        
        setVisible(true);
        /********************************/
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }
    
    public static void main(String[] args) {
        
        LoginForm form = new LoginForm();
        Socket connection;
        String name;
        
        while(form.socket == null)
        {
        //wait    
        }
        
        connection = form.socket;
        name = form.getUserName();
        form.setVisible(false);
        ExecutorService executor = Executors.newCachedThreadPool();
        
        TCPClient client = new TCPClient(name, connection);
        executor.execute(client.new RecieveTask());
    }
    
    class SendActionListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            out.writeObject(new Message(userName, recepientName.getText(), message.getText()));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    }
    
    class RecieveTask implements Runnable{

        @Override
        public void run() {
            
            try { 
                while(true){
                    ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                    Message newMessage = (Message)in.readObject();
                    System.out.println(newMessage.getContents());
                    Inbox.setText(newMessage.getContents());
                }
            }
            catch (IOException | ClassNotFoundException ex) {
                System.out.println("At ReciveTask" + ex.getMessage());
            }
        }
    }
        
    
    
}


