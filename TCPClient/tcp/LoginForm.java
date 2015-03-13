/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Motasim
 */
public class LoginForm extends JFrame {
    
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel passLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JButton signupButton;
    private javax.swing.JButton guestButton;
    private javax.swing.JLabel signupLabel;
    private javax.swing.JLabel titleLabel;
    private boolean logged;
    Socket socket;
    
    public LoginForm(){
        
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setStyle();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
     @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        loginButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        passwordField = new javax.swing.JPasswordField();
        signupButton = new javax.swing.JButton();
        signupLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        passLabel = new javax.swing.JLabel();
        guestButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        titleLabel.setFont(new java.awt.Font("Yu Mincho Demibold", 0, 18)); // NOI18N
        titleLabel.setText("Welcome to Chat Chat ");

        loginButton.setText("Log in");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        signupButton.setText("Sign up");
        signupButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupButtonActionPerformed(evt);
            }
        });

        signupLabel.setText("Not regestered  ? Please sign up :");

        nameLabel.setText("User name :");

        passLabel.setText("Password :");

        guestButton.setText("Guest");
        guestButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guestButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(titleLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(passLabel))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(guestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(passwordField)
                            .addComponent(nameField))))
                .addGap(164, 164, 164))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(signupLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(signupButton)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passLabel))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loginButton)
                    .addComponent(guestButton))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(signupButton)
                    .addComponent(signupLabel))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        pack();
    }// </editor-fold>                    
    
    public String getUserName(){
        return nameField.getText();
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
    

    
    private final int TIMEOUT_DURATON = 8000;
    public void performLogin(){
        
        //Socket loginSocket;
        try{
         
         socket = new Socket("localhost" , 8000);
         socket.setSoTimeout(TIMEOUT_DURATON);
         System.out.println("Here 1"); 
         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         
         System.out.println("Here 2");
         
         // Sends Sign in request to the server
         out.writeObject(new SignInRequest(new UserCredentials(nameField.getText(), passwordField.getPassword())));
         
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
         
         // Await for server response
         
         Response serverResponse = (Response) in.readObject();
         //JOptionPane.showMessageDialog(null, "Client: reading done");
         if(serverResponse == Response.SignInSuccess) {
             // Sign in success
             // proceed to initialize the client
             TCPClient.initializeTCPClient(this.getUserName(),socket);
             //this.setVisible(false);
             this.dispose();
         }
         else if(serverResponse == Response.WrongUserNameOrPassword)  {
             // Display wrong username/password message
             JOptionPane.showMessageDialog(null, "Wrong username and/or password");
         }
         
        // String Logged = in.readUTF();
         System.out.println("Here 3");
         //socket = loginSocket;
         
        }catch(Exception e){
           System.out.println(e);
           // Display cannot connect to the server message (Due to connection failure or timeout)
           //JOptionPane.showMessageDialog(null, e);
           JOptionPane.showMessageDialog(null, "Cannot connect to the server");
        }
    }
    
     private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        performLogin();
    }                                           

    private void signupButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        try{
         
         socket = new Socket("localhost" , 8000);
         socket.setSoTimeout(TIMEOUT_DURATON);
         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         
         // Sends Sign up request to the server
         out.writeObject(new SignUpRequest(new UserCredentials(nameField.getText(), passwordField.getPassword())));
         
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
         
         // Await for server response
         Response serverResponse = (Response) in.readObject();
         if(serverResponse == Response.SignUpSuccess) {
             // Sign in success
             // proceed to initialize the client
             TCPClient.initializeTCPClient(this.getUserName(),socket);
             //this.setVisible(false);
             this.dispose();
         }
         else if(serverResponse == Response.UserAlreadyExist)  {
             // Display user alread exists message
             JOptionPane.showMessageDialog(null, "User already exists");
         }
         
        }catch(Exception e){
           System.out.println(e.getMessage());
           //JOptionPane.showMessageDialog(null, e);
           // Display cannot connect to the server message (Due to connection failure or timeout)
           JOptionPane.showMessageDialog(null, "Cannot connect to the server");
        }
        
    }
    
    // Please add the "Sign in as guest" button
    private void guestButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        try {
            
            socket = new Socket("localhost" , 8000);
            socket.setSoTimeout(TIMEOUT_DURATON);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            
            out.writeObject(new GuestSignInRequest(null));
            
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            Object response = in.readObject();
            if(response instanceof GuestNameResponse) {
                System.out.println(((GuestNameResponse) response).guestName);
                TCPClient.initializeTCPClient(((GuestNameResponse)response).guestName,socket);
                this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(null, "Cannot connect to the server");
            }
            
            
        } catch (IOException|ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Cannot connect to the server");
            //Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static void main(String[] args){
        new LoginForm();
    }
}

    

class SignUpRequest implements Serializable {
    private UserCredentials credentials;
    SignUpRequest(UserCredentials credentials) {
        this.credentials = credentials;
    }
    UserCredentials getCredentials() { return credentials; }
}

class SignInRequest implements Serializable {
    private UserCredentials credentials;
    SignInRequest(UserCredentials credentials) {
        this.credentials = credentials;
    }
    UserCredentials getCredentials() { return credentials; }
}
// Guests requests shouldn't have credentials though .. 
class GuestSignInRequest implements Serializable{
    private UserCredentials credentials;
    GuestSignInRequest(UserCredentials credentials) {
        this.credentials = credentials;
    }
    UserCredentials getCredentials() { return credentials; }
}

class GuestNameResponse implements Serializable {
    String guestName;
    GuestNameResponse(String guestName) {
        this.guestName = guestName;
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
