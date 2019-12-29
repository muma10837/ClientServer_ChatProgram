/*
* This class acts as a client on the server holding most of their info when they log onto it.
 It is in charge of receiving and sending messages and sending received messages to the server to handle.
*/
package Server;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version 2.1
 * @author muma10837
 */
public class ServerThread extends Thread{
    //initilaizing fields
    private Server server;
    private Socket socket;
    private int ID = -1;
    private String newAccount;
    private String user;
    private String password;
    private DataInputStream in;
    private DataOutputStream out;
    
    /**
     * This is a constructor with two parameters
     * @param inServer 
     * @param inSocket 
     */
    public ServerThread(Server inServer, Socket inSocket) {
        server = inServer;
        socket = inSocket;
        ID = socket.getPort();
    }
    
    /**
     * 
     * @throws IOException 
     */
    public void open() throws IOException{
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }
    
    public void run() {
        System.out.println("Client " + ID + "running.");
        
        try {
            newAccount = in.readUTF();
            user = in.readUTF();
            password = in.readUTF();
            ServerDatabase temp = new ServerDatabase(user, password);
            if (!server.accountTest(newAccount, temp)) {
                server.remove(ID);
                stop();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true) {
            try { 
                server.handle(ID, in.readUTF(), user);
                
            } catch (IOException ex) {
                System.out.println("ServerThread.run handle error");
                server.remove(ID);
                stop();
            }
        }
    }
    
    public void send(String line) {
        try {
            out.writeUTF(line);
            out.flush();
        } catch (IOException ex) {
            System.out.println("ServerThread.send " + ex);
            server.remove(ID);
            stop();
        }
    }

    public int getID() {
        return ID;
    }

    public void close() throws IOException{
        if (socket != null) {
            socket.close();
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
}
