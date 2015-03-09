/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;

import java.io.Serializable;

/**
 *
 * @author Motasim
 * The class represents a message object that is transfered between two clients through the server
 * 
 */
public class Message implements Serializable {
    
    private final String sender;
    private final String recepient;
    private final String contents;
    
    public Message(String Sender, String Recepient, String Contents){
        this.recepient = Recepient;
        this.sender = Sender;
        this.contents = Contents;
    }
    
    public String getSender(){
        return sender;
    }
    
    public String getRecpt(){
        return recepient;
    }
    
    public String getContents(){
        return contents;
    }
}
