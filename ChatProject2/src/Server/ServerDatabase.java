/*
 * This serves as an object of a user with an account in this chat server
 */
package Server;

/**
 * @version 2.1
 * @author muma10837
 */
public class ServerDatabase {
    //initializing fields
    private String user;
    private String password;
    
    /**
     * This is a constructor with two parameters
     * @param inUser the account's username
     * @param inPass the account's password
     */
    public ServerDatabase(String inUser, String inPass) {
        user = inUser;
        password = inPass;
    }
    
    /**
     * This is a getter method to get a username
     * @return account's username
     */
    public String getUser() {
        return user;
    }
    
    /**
     * This is a getter method for the password
     * @return account's password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * This is an equal method comparing user and password
     * @param other another account on the serverdatabase
     * @return true if equal false if not
     */
    public boolean equals(ServerDatabase other) {
        return user.equals(other.user) && password.equals(other.password);
    }
    
    /**
     * String representation of this object
     * @return user,password
     */
    @Override
    public String toString() {
        return user + "," + password;
    }
}
