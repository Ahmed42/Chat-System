/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.util.Date;
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
    
    private javax.swing.JTextArea InboxTextArea;
    private javax.swing.JScrollPane inboxScrollPane;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton logOutButton;
    private javax.swing.JPanel masterPanel;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel onlineLabel;
    private javax.swing.JList onlineList;
    private javax.swing.JScrollPane onlineScrollPane;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel inboxLabel;
    // End of variables declaration  
    private final Socket connection;
    private final String userName;
    private static final long PERIOD = 500;
    private JButton cleaConversationButton;
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        masterPanel = new javax.swing.JPanel();
        onlineScrollPane = new javax.swing.JScrollPane();
        onlineList = new javax.swing.JList();
        sendButton = new javax.swing.JButton();
        inboxScrollPane = new javax.swing.JScrollPane();
        InboxTextArea = new javax.swing.JTextArea();
        onlineLabel = new javax.swing.JLabel();
        messageScrollPane = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        messageLabel = new javax.swing.JLabel();
        logOutButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        inboxLabel = new javax.swing.JLabel();
        cleaConversationButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        onlineList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        onlineScrollPane.setViewportView(onlineList);

        sendButton.setText("Send");
        sendButton.addActionListener(new SendActionListener());

        InboxTextArea.setEditable(false);
        InboxTextArea.setColumns(20);
        InboxTextArea.setRows(5);
        inboxScrollPane.setViewportView(InboxTextArea);

        onlineLabel.setText("Online users:");

        messageTextArea.setColumns(20);
        messageTextArea.setRows(5);
        messageScrollPane.setViewportView(messageTextArea);

        messageLabel.setText("Message to send");

        logOutButton.setText("Log out");
        logOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutButtonActionPerformed(evt);
            }
        });

        inboxLabel.setText("Inbox :");

        cleaConversationButton.setText("Clear Chat");
        cleaConversationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleaConversationButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout masterPanelLayout = new javax.swing.GroupLayout(masterPanel);
        masterPanel.setLayout(masterPanelLayout);
        masterPanelLayout.setHorizontalGroup(
            masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(masterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addComponent(logOutButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cleaConversationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(onlineScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(onlineLabel)
                    .addComponent(jSeparator2))
                .addGap(19, 19, 19)
                .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addComponent(inboxLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(inboxScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageLabel)
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        masterPanelLayout.setVerticalGroup(
            masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(masterPanelLayout.createSequentialGroup()
                .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(onlineLabel)
                    .addComponent(messageLabel)
                    .addComponent(inboxLabel))
                .addGap(6, 6, 6)
                .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE)
                        .addGap(207, 207, 207))
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addComponent(onlineScrollPane)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(logOutButton)
                            .addComponent(cleaConversationButton)))
                    .addComponent(inboxScrollPane))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(masterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(masterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold> 
    
    public TCPClient(String userName , Socket connection){
        
        setTitle(userName);
        this.connection = connection;
        this.userName = userName;    
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setStyle();
        LoadPastConversation();
        setVisible(true);
        /********************************/
        
    }
    
    public void setStyle(){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TCPClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
    }
    
    private void logOutButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // Cancels the "Alive" periodic message
        timer.cancel();
        // Shutdown the Receive Task
        executor.shutdown();
        SaveConversation();
        // Show the login form again
        new LoginForm();
        
        // Close this window
        this.dispose();
    }                                            

    static java.util.Timer timer;
    static ExecutorService executor;
    
    public static void initializeTCPClient(String username, Socket connection) {
        try {
            connection.setSoTimeout(0);
        } catch (SocketException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        executor = Executors.newCachedThreadPool();
        
        TCPClient client = new TCPClient(username, connection);
        client.setStyle();
        executor.execute(client.new RecieveTask());
        
        // Periodically executes the ConfirmLivenessTask
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(client.new ConfirmLivenessTask(),1000,PERIOD);
        
 
    }
    
    class SendActionListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(onlineList.getSelectedValue() != null){
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                out.writeObject(new Message(userName, (String)onlineList.getSelectedValue(), messageTextArea.getText()));
                String message = messageTextArea.getText();
                String old = InboxTextArea.getText();
                InboxTextArea.setText(old + "\n"+"Me to " + onlineList.getSelectedValue()+ " : "
                                                                    + message +"\n"+ getSeparator()+"\n");
                messageTextArea.setText("");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    }
    
    private void cleaConversationButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                       
        InboxTextArea.setText("");
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
                        onlineList.setListData(onlineUsers);
                    } else {
                        //System.out.println(((Message) newMessage).getContents());
                        Message clientMessage = (Message)newMessage;
                        String oldText = InboxTextArea.getText();
                        InboxTextArea.setText(oldText + "\n"+ 
                                        clientMessage.getSender() +" : "+ clientMessage.getContents()+"\n"+getSeparator());
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("At ReciveTask" + ex);
            }
        }
    }
    
    String getSeparator(){
        return "- - - - - - - - - - - - -";
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
    
    public void LoadPastConversation(){
        try {
            
            Conversation old;
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(userName+".dat"));
            old = (Conversation)in.readObject();
            InboxTextArea.setText(old.getConversation());
            
        } catch (IOException|ClassNotFoundException ex) {
            System.out.println("Now history file found");
        }
    }
    
    public void SaveConversation(){
        try {
            
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userName+".dat"));
            Conversation old = new Conversation(InboxTextArea.getText(), new Date());
            if(! old.getConversation().equals(""))
                out.writeObject(old);
            
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
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
