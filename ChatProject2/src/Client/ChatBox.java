/*
* This is the main part of the client chat program.
* Type your message into the textbox next to the send button and hit send to 
* send message to all users currently logged onto the chat server.
* when done hit disconnect if you wish to run another again or red x to exit
*
*/
package Client;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

/**
 * @version 2.1
 * @author muma10837
 */
public class ChatBox extends Frame{
    //initializing fields
    private Socket socket;
    private ClientThread clientThread;
    private DataInputStream in;
    private DataOutputStream out;
    private TextField message = new TextField();
    private TextArea msgDisplay = new TextArea();
    private Button disconnect = new Button("Disconnect");
    private Button send = new Button("send");
    private String user;
    private String ip;
    private int port;
    
    /**
     * initializes the Chatbox constructor with a socket parameter 
     * @param inSocket the socket that the client sending messages and receiving them through 
     * the server
     */
    public ChatBox(Socket inSocket) {
        socket = inSocket;
        open();
        Panel buttons = new Panel();
        buttons.setLayout(new GridLayout());
        buttons.add(disconnect);
        Panel inputArea = new Panel();
        inputArea.setLayout(new BorderLayout());
        inputArea.add("West", buttons);
        inputArea.add("Center", message);
        inputArea.add("East", send);
        setLayout(new BorderLayout());
        add("Center", msgDisplay);
        add("South", inputArea);
        pack();
        disconnect.setEnabled(true);
        send.setEnabled(true);
        setTitle("Chat Room");
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                disconnect();
                dispose();
            } 
        });
    }
    
    /**
     * This method runs an action when a button is clicked if disconnect disconnects 
     * current user if send sends a message
     * @param ev the event of the button being pressed
     * @param t the button that is pressed
     * @return always returns true
     */
    public boolean action(Event ev, Object t) {
        if (ev.target == disconnect) {
            disconnect();
            dispose();
            new ClientBox();
        } else if (ev.target == send) {
            send();
            message.requestFocus();
        }
        return true;
    }
    
    /**
     * This method disconnects the current user and then switches back to ClientBox class
     */
    public void disconnect() {
        try {
            print("Goodbye");
            out.writeUTF(".bye");
            out.close();
            socket.close();
        } catch (IOException ex) {
            print("ClientBox.disconnect: " + ex);
        }
        clientThread.close();
        clientThread.stop();
    }
    
    /**
     * This method sends a message to the server
     */
    private void send() {
        try {
            out.writeUTF(message.getText());
            out.flush();
            message.setText("");
        } catch (IOException ex) {
            print("ClientBox.send: " + ex);
        }
    }
    
    /**
     * This method opens an output stream and starts a clientThread
     */
    public void open() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            clientThread = new ClientThread(this, socket);
        } catch (IOException ex) {
            print("clientbox.open: " + ex);
        }
    }
    
    /**
     * If message is .bye disconnects otherwise it sends it to print method
     * @param msg A string received from th server via client class includes username: then message
     */
    public void handle(String msg) {  
        if (msg.equals(".bye")) {  
            print("Good bye. Press RETURN to exit ..."); 
            disconnect(); 
        } else {
            print(msg);
        } 
    }
    
   /**
    * prints a message into the chatbox TextArea
    * @param inMessage the message that gets printed
    */
    private void print(String inMessage) {
       msgDisplay.append(inMessage + "\n");
    }
}
