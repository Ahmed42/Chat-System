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
    public String content;
    boolean unreadMessageFlag;
    
    public Conversation(String content, Date date, boolean unreadMessageFlag){
        this.content = content;
        this.date = date.toString();
        this.unreadMessageFlag = unreadMessageFlag;
    }
    
    public String getDateString(){
        return date;
    }
    
    public String getContent(){
        return content;
    }
}
