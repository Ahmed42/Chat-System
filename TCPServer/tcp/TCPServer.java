/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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
                   // ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                   //out.writeUTF("TRUE");
                    System.out.println("At register task");
                    executor.execute(new RecieveTask(connection));
                }
            }
            catch (IOException | ClassNotFoundException ex) {
                System.out.println("Not found " + ex.getMessage());
            }
            
        }
        
    }
    
    class RecieveTask implements Runnable{
        
        public Socket client;
        
        public RecieveTask(Socket client){
            this.client = client;
        }
        
        @Override
        public void run() {
            ObjectInputStream in;
            try{
                while(true){
                    System.out.println("At recive task");
                    in = new ObjectInputStream(client.getInputStream());
                    Message newMessage = (Message)in.readObject();
                    System.out.println(newMessage.getContents());
                    addMessage(newMessage);
                    
                }
            }
            catch(IOException | ClassNotFoundException e){
                System.out.println(e.getMessage());
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
    
}