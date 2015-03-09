/**
 * This the data structure used to transfer messages between the clients
 * via object serialization and deserialization
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
