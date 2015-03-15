/**
 * TCP based multithreaded server , that handles users (clients) and perform message routing
 * The server has 5 tasks , 3 of them are now implemented:
 * 0 - Register task - not completed yet partial implementation : It recives user credentials and verfies the user
 * 1-  ReceiveTask : implements Runnable , one task is created for each user logged in , it recives the message from the user 
 *     and put the message in the server inboc
 * 2- RouteTask : implements Runnable , it runs in case the server inbox was not empty using locks, This task enumerates all messages in the
 *    server inbox and route them to their respective users
 *    The route task uses a HashMap data structure to convert usernames to connections 
 * 3- 
 * 3 - Update online users list - not implemented yet  
 * 4- Perfrom logout - not implemented yet
 * 
 *
 */

/**
* Work left:
* complete the gui of the server
* complete the logic of the register task
* add the  logic for the 2 tasks
* 
*/

package tcp;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
//import java.util.UUID;
/**
 *
 * @author Motasim
 */
public class TCPServer extends JFrame {
    Vector<UserCredentials> registeredUsers; // A vector of all registered users
    Vector<String> guestUsers;
    Vector<String> offlineUsers; // A vector of offline registered users
    HashMap<String,Socket> userPortMapping; /*Maps online users' usernames to their connections (contains both registered and guests)*/
    LinkedList<Message> Inbox;
    ServerSocket server ;
    Lock sendLock;
    Condition empty;
    FileOutputStream fileOut;
    FileInputStream fileIn;
    private final int TIMEOUT_DURATON = 5000;
    
    public TCPServer() throws IOException{
        // Deserialize registered users and saved messages
        try {
            fileIn = new FileInputStream("registered_users.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            registeredUsers = (Vector<UserCredentials>) in.readObject();
            fileOut = new FileOutputStream("registered_users.dat");
        } catch (Exception ex) {
            fileOut = new FileOutputStream("registered_users.dat");
            registeredUsers = new Vector<>();
            //Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        this.addWindowListener(new WindowAdapter() {
            // Perform registered users and messages serialization upon closing
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(registeredUsers);
                    System.exit(0);
                } catch (IOException ex) {
                    Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
        // Initializing offline users to all registered users, userPortMapping and guestUsers to empty, Inbox to users messages
        guestUsers = new Vector<>();
        offlineUsers = new Vector<>();
        for(UserCredentials credential: registeredUsers) {
            offlineUsers.add(credential.getUserName());
        }
        
        sendLock = new ReentrantLock();
        empty = sendLock.newCondition();
        Inbox = new LinkedList<>();
        userPortMapping = new HashMap<>();
        
        /*Graphics part initialization*/
        initComponents();
        allUsersList.setListData(offlineUsers);
        

        onlineUsersList.setListData(new String[]{});
        /**/
        server = new ServerSocket(8000); 
    }
    
    
    public static void main(String[] args) throws IOException {
        try{
            
            TCPServer server = new TCPServer();
            server.setStyle();
            server.setVisible(true);
            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.execute(server.new RegisterTask());
            executor.execute(server.new RouteTask());
        
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    
    /* Later ...
    enum Request {
        SignInRequest(), SignUpRequest(), GuestSignInRequest();
        UserCredentials credentials;
        private Request (UserCredentials credentials) {
            this.credentials = credentials;
        }
    }*/
    
    class RegisterTask implements Runnable{
        
        @Override
        public void run() {
            ExecutorService executor = Executors.newCachedThreadPool();
            try {
                while(true){
                    Socket connection = server.accept();
                    connection.setSoTimeout(TIMEOUT_DURATON);
                    ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                            
                    Object newUser = in.readObject();
                    String name;
                    // Perhaps it is better to use enum for requests
                    if(newUser instanceof SignInRequest) { // An already registered user is requesting to be signed in 
                        UserCredentials credentials = ((SignInRequest) newUser).getCredentials();
                        //JOptionPane.showMessageDialog(null, "Server: Sign in request received");
                        if(checkCredentials(credentials)) {
                            name = credentials.getUserName();
                            offlineUsers.remove(name); // remove from offline list
                            // Send sign in success response to client
                            out.writeObject(Response.SignInSuccess);
                        }
                        else {
                            // Send wrong username/password response to client
                            
                            out.writeObject(Response.WrongUserNameOrPassword);
                            //JOptionPane.showMessageDialog(null, "Server: Wrong cred .. message sent");
                            continue;
                        }
                    }
                    else if(newUser instanceof SignUpRequest) { // A new user requesting to be signed up
                        UserCredentials credentials = ((SignUpRequest) newUser).getCredentials();
                        name = credentials.getUserName();
                        if(isUserRegistered(name)) {
                            // Send user already exits response to client
                            out.writeObject(Response.UserAlreadyExist);
                            continue;
                        } else {
                            registerNewUser(credentials);
                            out.writeObject(Response.SignUpSuccess);
                        }
                    } else if (newUser instanceof GuestSignInRequest) { // A guest user requesting to be signed in
                        GuestNameResponse guestNameResponse;
                        do {
                            guestNameResponse = new GuestNameResponse(Response.assignGuestName());
                            name = guestNameResponse.guestName;
                        } while (doesGuestUserExist(name)); // keep generating names while the name is not unique
                        guestUsers.add(name);
                        //System.out.println(guestNameResponse.guestName);
                        out.writeObject(guestNameResponse);
                    } else {
                        // Invalid request
                        out.writeObject(Response.InvalidRequest);
                        continue;
                    }
                    
                    userPortMapping.put(name, connection); 
                    // Since a new user went online, an update needs to be sent to all online users
                    sendUpdateOnlineUsersMessage();
                    onlineUsersList.setListData(userPortMapping.keySet().toArray());
                    System.out.println("At register task");
                    allUsersList.setListData(registeredUsers);
                    executor.execute(new RecieveTask(name,connection));
                }
            }
            catch (Exception ex) {
                //JOptionPane.showMessageDialog(null, "SERVER:" + ex);
                //System.out.println("Not found " + ex.getMessage());
            }
            
        }
        // A more secure way should be adopted .. hashing and salting passwords
        boolean checkCredentials(UserCredentials credentials) {
            for(UserCredentials registeredUser: registeredUsers) {
                String testUsername = credentials.getUserName(), registeredUsername = registeredUser.getUserName(),
                        testPassword = new String(credentials.getPassword()), registeredPassword = new String(registeredUser.getPassword());
                System.out.println(registeredUsername);
                System.out.println(registeredPassword);
                if(testUsername.equals(registeredUsername) && testPassword.equals(registeredPassword)) {
                    return true;
                }
            }
            return false;
        }
        
        
        
        void registerNewUser(UserCredentials credentials) {
            registeredUsers.add(credentials);
        } 
    }
    
    boolean isUserRegistered(String name) {
            for(UserCredentials user: registeredUsers) {
                if((user.getUserName()).equals(name)) {
                    return true;
                }
            }
            return false;
        }
    
    boolean doesGuestUserExist(String name) {
        return guestUsers.contains(name);
    }
  
    
    class RecieveTask implements Runnable{
        
        public Socket client;
        String username;
        
        public RecieveTask(String name, Socket client){
            this.client = client;
            this.username = name;
            try {
                this.client.setSoTimeout(TIMEOUT_DURATON);
            } catch (SocketException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            ObjectInputStream in;

            while (true) {
                System.out.println("At recive task");
                try {
                    in = new ObjectInputStream(client.getInputStream());
                    Object newMessage = in.readObject();
                    if (newMessage instanceof Message) {
                        addMessage((Message)newMessage);
                    }
                    /*else if (newMessage.equals("Alive")){
                        client.setSoTimeout(TIMEOUT_DURATON);
                    }*/
                } catch (IOException | ClassNotFoundException e) {
                    // if failed to read or timeout, client probably went offline
                    userPortMapping.remove(username);
                    if(isUserRegistered(username)) {
                        offlineUsers.add(username);
                    } else {
                        guestUsers.remove(username);
                    }
                    sendUpdateOnlineUsersMessage();
                    break;
                }

            }
        }
            
        
        
        public synchronized void addMessage(Message newMessage){
            try{
                sendLock.lock();
                Inbox.add(newMessage);
                empty.signalAll();
            }
            finally{
                sendLock.unlock();
            }
        }
    }
    
    
    class RouteTask implements Runnable{

        @Override
        public void run() {
            
            while(true){
                sendMessages();
            }
        }   
    }
   
    public void sendMessages(){
        
        sendLock.lock();
        try{
            while(Inbox.size() == 0){
                empty.await();
            }
            System.out.println("At send Messages");         
            for(Message message : Inbox){
                if (message.getRecpt().equals("All")){
                    for(Object user : userPortMapping.keySet().toArray()){
                        Socket recepientSocket = userPortMapping.get(user.toString()); 
                        ObjectOutputStream out = new ObjectOutputStream(recepientSocket.getOutputStream());
                        out.writeObject(message);
                    }
                }
                else{
                    String recepientName = message.getRecpt();
                    Socket recepientSocket = userPortMapping.get(recepientName); 
                    ObjectOutputStream out = new ObjectOutputStream(recepientSocket.getOutputStream());
                    out.writeObject(message);
                }
            }
            Inbox.clear();
        } 
        catch (IOException|InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        finally{
            sendLock.unlock();
        }
    }
    
    
    
    void sendUpdateOnlineUsersMessage() {
            UpdateOnlineUsersMessage onlineUsers = new UpdateOnlineUsersMessage(userPortMapping.keySet().toArray());
            for(Socket connection : userPortMapping.values()) {
                try {
                    ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                    out.writeObject(onlineUsers);
                } catch (IOException ex) {
                    Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            onlineUsersList.setListData(userPortMapping.keySet().toArray());
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        masterPanel = new javax.swing.JPanel();
        onlineScrollPane = new javax.swing.JScrollPane();
        onlineUsersList = new javax.swing.JList();
        onlineUsesrLabel = new javax.swing.JLabel();
        allUsersScrollPane = new javax.swing.JScrollPane();
        allUsersList = new javax.swing.JList();
        allUsersLabel = new javax.swing.JLabel();
        startServerButton = new javax.swing.JButton();
        deleteUserButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(419, 307));

        onlineUsersList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
        onlineScrollPane.setViewportView(onlineUsersList);

        onlineUsesrLabel.setText("Online users");

        allUsersList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
        allUsersScrollPane.setViewportView(allUsersList);

        allUsersLabel.setText("All users");

        startServerButton.setText("Stop server");
        startServerButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopServerButtonActionPerformed(evt);
            }
        });

        deleteUserButton.setText("Delete user");
        deleteUserButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUserButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout masterPanelLayout = new javax.swing.GroupLayout(masterPanel);
        masterPanel.setLayout(masterPanelLayout);
        masterPanelLayout.setHorizontalGroup(
            masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(masterPanelLayout.createSequentialGroup()
                .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(onlineUsesrLabel)
                    .addComponent(onlineScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(allUsersLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(allUsersScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                        .addGap(8, 8, 8)
                        .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startServerButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(deleteUserButton, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        masterPanelLayout.setVerticalGroup(
            masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, masterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addComponent(deleteUserButton)
                        .addGap(18, 18, 18)
                        .addComponent(startServerButton))
                    .addGroup(masterPanelLayout.createSequentialGroup()
                        .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(onlineUsesrLabel)
                            .addComponent(allUsersLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(masterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(allUsersScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(onlineScrollPane))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(masterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(masterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
    }// </editor-fold>    
    
    
     public void setStyle(){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
    }

    private void deleteUserButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        try{
            
            offlineUsers.remove(allUsersList.getSelectedIndex());
            registeredUsers.remove(allUsersList.getSelectedIndex());
            userPortMapping.remove((String)allUsersList.getSelectedValue());
            allUsersList.setListData(offlineUsers);
            sendUpdateOnlineUsersMessage();
            System.out.println(allUsersList.getSelectedIndex());
            
        }catch(Exception e){
            
            allUsersList.setListData(offlineUsers);
            sendUpdateOnlineUsersMessage();
        }
    }                                                

    private void stopServerButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        try {

            Object[] users = userPortMapping.keySet().toArray();
            UpdateOnlineUsersMessage clearedUsers = new UpdateOnlineUsersMessage(new String[]{});
            
            for(Object user : users){
                ObjectOutputStream out = new ObjectOutputStream(userPortMapping.get(user.toString()).getOutputStream());
                out.writeObject(new Message("Server", user.toString(),"Server Stopped !!"));
            }
            
            for(Object user : users){
                ObjectOutputStream out = new ObjectOutputStream(userPortMapping.get(user.toString()).getOutputStream());
                out.writeObject(clearedUsers);
            }
            
            
            server.close();
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(registeredUsers);
            System.exit(0);
            this.dispose();
            
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            
        }
    } 
    
    
    private javax.swing.JLabel allUsersLabel;
    private javax.swing.JList allUsersList;
    private javax.swing.JScrollPane allUsersScrollPane;
    private javax.swing.JButton deleteUserButton;
    private javax.swing.JPanel masterPanel;
    private javax.swing.JScrollPane onlineScrollPane;
    private javax.swing.JList onlineUsersList;
    private javax.swing.JLabel onlineUsesrLabel;
    private javax.swing.JButton startServerButton;
    
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

enum Response implements Serializable {
        SignInSuccess(null), WrongUserNameOrPassword(null), UserAlreadyExist(null), SignUpSuccess(null), InvalidRequest(null), GuestName(null);
        String guestName;
        static Random rand = new Random();
        private Response(String guestName) {
            this.guestName = guestName;
        }
        
        static String assignGuestName() {
            // randomely assign a random unique name to the guest
            int id = rand.nextInt(10001);
            return "guest" + id; 
        }
    };

class GuestNameResponse implements Serializable {
    String guestName;
    GuestNameResponse(String guestName) {
        this.guestName = guestName;
    }
}

class SignUpRequest implements Serializable{
    private UserCredentials credentials;
    SignUpRequest(UserCredentials credentials) {
        this.credentials = credentials;
    }
    UserCredentials getCredentials() { return credentials; }
}

class SignInRequest implements Serializable{
    private UserCredentials credentials;
    SignInRequest(UserCredentials credentials) {
        this.credentials = credentials;
    }
    UserCredentials getCredentials() { return credentials; }
}

class GuestSignInRequest implements Serializable{
    private UserCredentials credentials;
    GuestSignInRequest(UserCredentials credentials) {
        this.credentials = credentials;
    }
    UserCredentials getCredentials() { return credentials; }
}