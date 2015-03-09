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
}
