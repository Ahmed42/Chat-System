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
import java.io.Serializable;
import java.net.*;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final JLabel onlineUsersLabel;
    
    private static final long PERIOD = 500;
    
    public TCPClient(String userName , Socket connection){
        
        setTitle(userName);
        this.connection = connection;
        this.userName = userName;    
        /*Init the compoents of the frame*/
        recepientName = new JTextField(25);
        message = new JTextField(25);
        Inbox = new JTextField(25);
        onlineUsersLabel = new JLabel();
        sendButton = new JButton(" Send Message ");
        setSize(400,200);
        setLayout(new FlowLayout());
        add(recepientName);
        add(message);
        add(Inbox);
        sendButton.addActionListener(new SendActionListener());
        add(sendButton);
        add(onlineUsersLabel);
        
        setVisible(true);
        /********************************/
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }
    
    public static void main(String[] args) {
        LoginForm form = new LoginForm();
        Socket connection;
        String name;
        System.out.println(form.socket);
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
        // Periodically executes the ConfirmLivenessTask
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(client.new ConfirmLivenessTask(),1000,PERIOD);
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

    class RecieveTask implements Runnable {

        @Override
        public void run() {

            try {
                while (true) {
                    ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                    Object newMessage = in.readObject();
                    if (newMessage instanceof UpdateOnlineUsersMessage) {
                        Object[] onlineUsers = ((UpdateOnlineUsersMessage) newMessage).getOnlineUsers();
                        String onlineUsersText = "";
                        for(Object user: onlineUsers) {
                            onlineUsersText += (String) user + " ";
                        }
                        onlineUsersLabel.setText(onlineUsersText);
                    } else {
                        System.out.println(((Message) newMessage).getContents());
                        Inbox.setText(((Message) newMessage).getContents());
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("At ReciveTask" + ex.getMessage());
            }
        }
    }
    
    // A task that sends "Alive" to the server so that the server won't go timeout
    class ConfirmLivenessTask extends TimerTask {

        @Override
        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                out.writeObject("Alive");
                
            } catch (IOException ex) {
                Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

}

class UpdateOnlineUsersMessage implements Serializable {
    private Object[] onlineUsers;
    
    UpdateOnlineUsersMessage(Object[] onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
    
    Object[] getOnlineUsers() {
        return onlineUsers;
    }
}


