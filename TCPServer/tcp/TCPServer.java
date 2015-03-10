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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author Motasim
 */
public class TCPServer {
    
    LinkedList<Message> Inbox;
    HashMap<String,Socket> userPortMapping;
    ServerSocket server ;
    Lock sendLock;
    Condition empty;
    
    public TCPServer() throws IOException{
        
        sendLock = new ReentrantLock();
        empty = sendLock.newCondition();
        Inbox = new LinkedList<>();
        userPortMapping = new HashMap<>();
        server = new ServerSocket(8000); 
    }
    
    
    public static void main(String[] args) throws IOException {
        try{
            
            TCPServer server = new TCPServer();        
            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.execute(server.new RegisterTask());
            executor.execute(server.new RouteTask());
        
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    class RegisterTask implements Runnable{

        @Override
        public void run() {
            ExecutorService executor = Executors.newCachedThreadPool();
            try {
                while(true){
                    Socket connection = server.accept();
                    ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                    UserCredentials newUser = (UserCredentials)in.readObject();
                    String name = newUser.getUserName();
                    System.out.println(name);
                    userPortMapping.put(name, connection);
                    // Since a new user went online, an update needs to be sent to all online users
                    sendUpdateOnlineUsersMessage();
                    System.out.println("At register task");
                    executor.execute(new RecieveTask(name,connection));
                }
            }
            catch (IOException | ClassNotFoundException ex) {
                System.out.println("Not found " + ex.getMessage());
            }
            
        }
        
    }
    
    class RecieveTask implements Runnable{
        private final int TIMEOUT_DURATON = 10000;
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
                } catch (IOException | ClassNotFoundException e) {
                    userPortMapping.remove(username);
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
                String recepientName = message.getRecpt();
                Socket recepientSocket = userPortMapping.get(recepientName); 
                ObjectOutputStream out = new ObjectOutputStream(recepientSocket.getOutputStream());
                out.writeObject(message);
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