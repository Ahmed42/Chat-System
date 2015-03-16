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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Motasim
 */
public class TCPClient extends JFrame {
    private javax.swing.JCheckBox BroadcastCheckBox;
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
    //private HashMap<String, LinkedList<String>> usersToInbox;
    HashMap<String,Conversation> conversations; // maps other clients' usernames to coversations
    Vector<UserRow> usersDisplay; // contains the users to be displayed by thr JList, in the form: [Online status] username [Unread message status]
    ObjectOutputStream out;
    ObjectInputStream in;
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
        BroadcastCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        onlineList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });

        // For listening to changes in selection in the online users list, switch between conversations
        onlineList.getSelectionModel().addListSelectionListener(new onlineUserSelectionListener());
        onlineList.setListData(usersDisplay);
        onlineScrollPane.setViewportView(onlineList);

        sendButton.setText("Send");
        sendButton.addActionListener(new SendActionListener());

        InboxTextArea.setEditable(false);
        InboxTextArea.setColumns(20);
        InboxTextArea.setRows(5);
        inboxScrollPane.setViewportView(InboxTextArea);

        onlineLabel.setText("Online users + past conversations:");

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

        BroadcastCheckBox.setText("Broadcast");

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
                    .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addComponent(messageLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BroadcastCheckBox)))
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
                        .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sendButton)
                            .addComponent(BroadcastCheckBox))
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
                       
    // initialize DisplayUsers to loaded past conversations
    void initializeUsersDisplay() {
        usersDisplay = new Vector<>();
        for(String username: conversations.keySet()) {
            usersDisplay.add(new UserRow(username,false,conversations.get(username).unreadMessageFlag));
        }
    }
    
    public TCPClient(String userName , Socket connection, ObjectOutputStream out, ObjectInputStream in){
        this.out = out;
        this.in = in;
        setTitle(userName);
        this.connection = connection;
        this.userName = userName;
        LoadPastConversation();
        initializeUsersDisplay();
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setStyle();        
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
    
    public static void initializeTCPClient(String username, Socket connection, ObjectOutputStream out, ObjectInputStream in) {
        try {
            connection.setSoTimeout(0);
        } catch (SocketException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        executor = Executors.newCachedThreadPool();
        
        TCPClient client = new TCPClient(username, connection, out, in);
        client.setStyle();
        executor.execute(client.new RecieveTask());
        
        // Periodically executes the ConfirmLivenessTask
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(client.new ConfirmLivenessTask(),1000,PERIOD);
        
 
    }
    
    class onlineUserSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            UserRow selectedUser = (UserRow) onlineList.getSelectedValue();
            if(e.getValueIsAdjusting() || selectedUser == null) {
                return;
            }
            
            if(selectedUser.unreadMessage) { // remove the unread message tag
                selectedUser.unreadMessage = false;
                conversations.get(selectedUser.name).unreadMessageFlag = false;
                int i = usersDisplay.indexOf(selectedUser);
                usersDisplay.set(i, selectedUser);
                onlineList.setListData(usersDisplay);
            }
            
            Conversation currentCoversation = conversations.get(selectedUser.name);
            
            if( currentCoversation == null) {
                InboxTextArea.setText("");
            } else { // set the inbox to the selected user conversation
                InboxTextArea.setText(currentCoversation.getContent()); 
            }
            
            if(selectedUser.online) { // disable the send button in case the user is offline
                sendButton.setEnabled(true);
            }
            else {
                sendButton.setEnabled(false);
            }        
        }      
    }
    
    class SendActionListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(BroadcastCheckBox.isSelected()&&(!messageTextArea.getText().equals(""))){
                Message broadcast = new Message(userName, "All", messageTextArea.getText());
                //ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                out.writeObject(broadcast);

                String oldContent;
                Conversation oldConversation = conversations.get("Broadcast");
                if(oldConversation == null) {
                    oldContent = "";
                }
                else {
                    oldContent = oldConversation.getContent();
                }
                
                String newContent = oldContent + "\nMe to All: " + broadcast.getContents() +"\n"+ getSeparator()+"\n";
                Conversation newConversation = new Conversation(newContent, new Date(),false);
                conversations.put("Broadcast",newConversation);
                InboxTextArea.setText(newContent);
                messageTextArea.setText("");
            }
            
            else if ((onlineList.getSelectedValue() != null)&&(!messageTextArea.getText().equals("")) ){
                
                //ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                
                UserRow recipient = (UserRow) onlineList.getSelectedValue();
                //JOptionPane.showMessageDialog(null, userName+": Sending to: " + recipient.name);
                Message newMessage = new Message(userName, recipient.name, messageTextArea.getText());
                System.out.println(newMessage);
                out.writeObject(newMessage);
                //JOptionPane.showMessageDialog(null, userName+": Sent to: " + recipient.name);
                String message = messageTextArea.getText();
                
                String oldContent;
                Conversation oldConversation = conversations.get(recipient.name);
                if(oldConversation == null) {
                    oldContent = "";
                } else {
                    oldContent = oldConversation.getContent();
                }
                String newContent = oldContent + "\nMe to " + recipient.name+ ": " + message +"\n"+ getSeparator()+"\n";
                Conversation newConversation = new Conversation(newContent, new Date(),false);
                conversations.put(recipient.name,newConversation);
                InboxTextArea.setText(newContent);
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
    
    
    //String unreadMessageTag = "[Unread Message]", onlineTag = "[Online]", offlineTag = "[Offline]";
    //Vector<String> listToDisplayV;    
    class RecieveTask implements Runnable {
        
        @Override
        public void run() {

            try {              
                while (true) {
                    ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                    Object newMessage = in.readObject();
                    if (newMessage instanceof UpdateOnlineUsersMessage) { // Update online users message
                        Object[] onlineUsers = ((UpdateOnlineUsersMessage) newMessage).getOnlineUsers();
                        Vector<Object> onlineUsersV = new Vector<>(Arrays.asList(onlineUsers));
                        Vector<UserRow> usersDisplayCopy = new Vector<>(usersDisplay);
                        //System.out.println(onlineUsersV.get(0));
                        /*for(int i=0; i< usersDisplay.size(); i++) {
                            if(onlineUsersV.contains(usersDisplay.get(i).name)) {
                                onlineUsersV.remove(usersDisplay.get(i).name);
                                usersDisplay.set(i, new UserRow(usersDisplay.get(i).name,usersDisplay.get(i).online, usersDisplay.get(i).unreadMessage ));
                            }
                            else {
                                if(conversations.get(user.name) != null) {
                                    //user.online = true;
                                    usersDisplay.set(i, new UserRow(usersDisplay.get(i).name,usersDisplay.get(i).online, usersDisplay.get(i).unreadMessage ));
                                }
                                else {
                                    usersDisplay.remove(usersDisplay.get(i).name);
                                }
                            }
                        }*/
                        for(UserRow user: usersDisplayCopy) {
                            if(onlineUsersV.contains(user.name)) {
                                onlineUsersV.remove(user.name);
                                //user.online = true;
                                int i = usersDisplay.indexOf(user);
                                usersDisplay.set(i, new UserRow(user.name, true, user.unreadMessage));
                            }
                            else {
                                if(conversations.get(user.name) != null) {
                                    //user.online = true;
                                    int i = usersDisplay.indexOf(user);
                                    usersDisplay.set(i, new UserRow(user.name, false, user.unreadMessage));
                                }
                                else {
                                    usersDisplay.remove(user);
                                }
                            }
                        }
                        
                        for(Object user: onlineUsersV) {
                            //System.out.println(user);
                            usersDisplay.add(new UserRow((String)user, true, false));
                        }
                        onlineList.setListData(usersDisplay);
                        
                    } else { // users message
                        //System.out.println(((Message) newMessage).getContents());
                        //JOptionPane.showMessageDialog(null, userName+": Received!");
                        Message clientMessage = (Message) newMessage;
                        String sender = clientMessage.getSender();
                        if (!(sender.equals(userName) && clientMessage.getRecpt().equals("All"))) {
                            Conversation oldConversation = conversations.get(sender);
                            String oldContent;
                            String newContent = "\n" + sender + ": " + clientMessage.getContents() + "\n" + getSeparator() + "\n";

                            if (oldConversation != null) { // user had a conversation with the client
                                oldContent = oldConversation.getContent();
                            } else {
                                oldContent = "";
                            }
                            String contentToDisplay = oldContent + newContent;
                            boolean unreadMessageFlag;

                            if (onlineList.getSelectedValue() != null && ((UserRow) onlineList.getSelectedValue()).name.equals(sender)) { // whether to display the message or not
                                unreadMessageFlag = false;
                                InboxTextArea.setText(contentToDisplay);
                            } else {
                                unreadMessageFlag = true;
                                for (UserRow user : usersDisplay) {
                                    if (user.name.equals(sender)) {
                                        user.unreadMessage = unreadMessageFlag;
                                    }
                                }
                                onlineList.setListData(usersDisplay);
                            }
                            conversations.put(sender, new Conversation(contentToDisplay, new Date(), unreadMessageFlag));
                        }
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
                //ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                out.writeObject("Alive");
                
            } catch (IOException ex) {
                Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public void LoadPastConversation(){
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(userName+".dat"));
            conversations = (HashMap<String,Conversation>)in.readObject();
            
        } catch (IOException|ClassNotFoundException ex) {
            System.out.println("No history file found");
            conversations = new HashMap<>();
        }
    }
    
    public void SaveConversation(){
        try {
            
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userName+".dat"));
            //Conversation old = new Conversation(InboxTextArea.getText(), new Date());
            //if(! old.getConversation().equals(""))
              //  out.writeObject(old);
            out.writeObject(conversations);
            
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

class UserRow {
    String name;
    boolean unreadMessage, online;
    UserRow(String name,boolean online,boolean unreadMessage) {
        this.name = name;
        this.unreadMessage = unreadMessage;
        this.online = online;
    }
    String unreadMessageTag = "[Unread Message]", onlineTag = "[Online]", offlineTag = "[Offline]";
    @Override
    public String toString() {
        String rep = 
                (online ? onlineTag : offlineTag) +
                name +
                (unreadMessage ? unreadMessageTag : "");
        return rep;
    }
}
