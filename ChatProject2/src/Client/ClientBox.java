/*
* This is the first class you wish to run it comes up as a login screen if you 
* login it starts the ChatBox class.
*
* To use just change the ip field to the server ip address and you are good to go.
* enter a username and password and click New User or Login
* When done click the red x to exit program
*/
package Client;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * @version 2.1
 * @author muma10837
 */
public class ClientBox extends Frame{
    //initializing fields
    private Button newUser = new Button("New User");
    private Button login = new Button("Login");
    private TextField user = new TextField(20);
    private TextField password = new TextField(20);
    private TextArea logs = new TextArea(2,40);
    private Socket socket;
    private String ip = "10.216.65.129";
    private int port = 5000;
    private DataOutputStream out;
    
    /**
     * Default constructor of the ClientBox Class
     * builds the login page gui
     */
    public ClientBox() {
        Panel buttons = new Panel();
        buttons.add(newUser);
        buttons.add(login);
        setLayout(new FlowLayout());
        add(new Label("Username:"));
        add(user);
        add(new Label("Password:"));
        add(password);
        add(logs);
        add(buttons);
        pack();
        setTitle("Login Page");
        setSize(300,200);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                dispose();
            } 
        });
    } 
    
    /**
     * This method runs an action when a button is pressed if login runs the first one
     * if New User runs the second
     * 
     * @param e the event of the button being pressed
     * @param o the button being pressed
     * @return always returns true
     */
    @Override
    public boolean action(Event e, Object o) {
        if(e.target == login) {
            connect();
            login("");
            dispose();
            new ChatBox(socket);
        }
        if(e.target == newUser) {
            connect();
            login("new");
            dispose();
            new ChatBox(socket);
        }
        
        return true;
    }
    
    /**
     * This method logs the user in if new sends a message to the server letting it
     * know the user is new. It then sends the login credentials to the server. 
     * @param in new or blank to tell server if user is new or not
     */
    public void login(String in) {
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.writeUTF(in);
            out.writeUTF(user.getText());
            out.writeUTF(password.getText());
            out.flush();
            
        } catch (IOException ex) {
            System.out.println("ClientBox.login " + ex);
        }
    }
    
    /**
     * This method connects the user to the server
     */
    public void connect() {
        print("Establishing Connection. Please wait.....");
        try {
            socket = new Socket(ip, port);
            print("Connected: " + socket);
        } catch (IOException ex) {
            print("ClientBox.connect: " + ex);
        }
    }
    
    /**
     * This method prints a message in the login gui logs letting user know what is happening
     * @param inString The message to be printed
     */
    private void print(String inString) {
        logs.append(inString + "\n");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientBox().setVisible(true);
            }
        });
    }
}

