/**
 * This the data structure used to transfer user information between the client and the server
 * via object serialization and deserialization
 */

package tcp;

import java.io.Serializable;

/**
 *
 * @author Motasim
 */
public class UserCredentials implements Serializable {
    private final String userName;
    private final char[] password;
    
    public UserCredentials(String userName, char[] password){
        this.userName = userName;
        this.password = password;
    }
    
    public String getUserName(){
        return userName;
    }
    
    public char[] getPassword(){
        return password;
    }
    
    @Override
    public String toString(){
        return userName;
    }
}
