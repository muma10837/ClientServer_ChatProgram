/*
* This class runs as the ChatBox thread so that it can receive and send messages 
* on the chat server.
*/
package Client;

import java.net.*;
import java.io.*;

/**
 * @version 2.1
 * @author muma10837
 */
public class ClientThread extends Thread {
    //initializing fields
   private Socket socket;
   private ChatBox client;
   private DataInputStream in;
   
   /**
    * This is a constructor with two parameters a client and a socket
    * @param inClient the client using the ChatBox
    * @param inSocket the socket that the client is sending messages and receiving them through
    */
   public ClientThread(ChatBox inClient, Socket inSocket) {
       client = inClient;
       socket = inSocket;
       open();
       start();
    }
   
   /**
    * opens an input stream received from the server
    */
    public void open(){  
        try {  
            in  = new DataInputStream(socket.getInputStream());
            
        } catch(IOException ioe) {  
            System.out.println("clientThread.open()" + ioe);
            client.disconnect();
        }
    }
    
    /**
     * This method reads in the data sent from the server and runs until ChatBox gets disconnected 
     * or an IOException happens
     */
   @Override
    public void run() {
        while (true) {  
            try {  
                client.handle(in.readUTF());
            } catch(IOException ioe) {  
                System.out.println("Listening error: " + ioe.getMessage());
                client.disconnect();
            }
        }
    }
    /**
     * This method closes the DataInputStream 
     */
    public void close() {  
        try {  
            if (in != null) {
                in.close();
            }
        } catch(IOException ioe) {  
            System.out.println("clientThread.stop()" + ioe);
        }
    }
}