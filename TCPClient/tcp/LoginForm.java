/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.IOException;
import java.net.*;
import javax.swing.*;

/**
 *
 * @author Motasim
 */
public class LoginForm extends JFrame {
    
    private final JTextField nameField;
    private final JLabel userLabel;
    private final JPasswordField passwordField;
    private final JLabel passLabel;
    private final JButton loginButton;
    private boolean logged;
    Socket socket;
    
    public LoginForm(){
        
        nameField = new JTextField(25);
        passwordField = new JPasswordField(25);
        loginButton = new JButton("Login");
        userLabel = new JLabel("User Name : ");
        passLabel = new JLabel("Password :  ");
        setSize(400,150);
        setLayout(new FlowLayout(10));
        add(userLabel);
        add(nameField);
        add(passLabel);
        add(passwordField);
        
        loginButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        add(loginButton);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public String getUserName(){
        return nameField.getText();
    }
    
    public Socket performLogin(){
        
        Socket loginSocket;
        try{
         
         loginSocket = new Socket("localhost" , 8000);
         System.out.println("Here 1"); 
         ObjectOutputStream out = new ObjectOutputStream(loginSocket.getOutputStream());
         
         System.out.println("Here 2"); 
         out.writeObject(new UserCredentials(nameField.getText(), passwordField.getPassword()));
         
         DataInputStream in = new DataInputStream(loginSocket.getInputStream());
         
        // String Logged = in.readUTF();
         System.out.println("Here 3");
         socket = loginSocket;
         
        }catch(Exception e){
           System.out.println(e.getMessage());
            return null;
        }
        return null;
    }
    
    public static void main(String[] args){
        new LoginForm();
    }
}
