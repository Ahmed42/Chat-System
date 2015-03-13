/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author Motasim
 */
public class Conversation implements Serializable{
    
    public String date;
    public String conversation;
    
    public Conversation(String conversation, Date date){
        this.conversation = conversation;
        this.date = date.toString();
        
    }
    
    public String getDateString(){
        return date;
    }
    
    public String getConversation(){
        return conversation;
    }
}
