/*
* This is the Server class for the client server chat program.
* To use just run the file does not have a way to close so you will have to manually
* it down. On Netbeans is pretty simple but BlueJ may have to close down the entire project
*/
package Server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version 2.1
 * @author muma10837
 */
public class Server implements Runnable{
    //initializing fields
    private ArrayList<ServerThread> clients = new ArrayList<>();
    private static ArrayList<ServerDatabase> serverDatabase ;
    private ServerSocket socket;
    private Thread thread;
    private int count = 0;
    
    /**
     * This is a Server constructor with a port number parameter
     * @param port the port number for the client and server to message through
     */
    public Server(int port) {
        try {
            System.out.println("Binding to port. Please wait....");
            socket = new ServerSocket(5000);
            System.out.println("Server running " + socket);
            start();
        } catch (IOException ex) {
            System.out.println("Server.Server" + ex);
        }
        
    }
   /**
    * This method starts a thread so that the Server can continuously add in users while
    * still handling messages
    */
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    
    /**
     * This method adds clients to the server then sends them to a serverThread
     */
    @Override
    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a user.....");
                addClientThread(socket.accept());
            } catch (IOException ex) {
                System.out.println("Server.run " + ex);
            }
        }
    }
    
    /**
     * This method adds in users to the serverThread ArrayList so that all users can send and receive 
     * messages
     * @param inSocket the socket this current client is connected to
     */
    private void addClientThread(Socket inSocket) {
        System.out.println("Client accepted: " + inSocket);
        clients.add(new ServerThread(this, inSocket));
        try {
            clients.get(count).open();
            clients.get(count).start();
            count++;
        } catch (IOException ex) {
            System.out.println("Servrt.addClientThread " + ex);
        }
        
    }
    
    /**
     * Tests to see if the account is new or not and if not new if the hit the new user button
     * @param newAccount String new if new blank if not
     * @param temp the account being tested
     * @return 
     */
    public boolean accountTest(String newAccount, ServerDatabase temp) {
        if (newAccount.equals("new")) {
            try {
                serverDatabase.add(temp);
                PrintWriter outFile = new PrintWriter("ServerDatabase.txt");
                for (int x = 0; x < serverDatabase.size(); x++) {
                    outFile.println(serverDatabase.get(x).toString());
                }
                outFile.close();
                
            } catch (FileNotFoundException ex) {
                System.out.println("new account addition fail!!!!");
            } catch (IOException ex) {
                System.out.println("new account addition fail!!!!");
            }
            return true;
        } 
        for (int x = 0; x < serverDatabase.size(); x++) {
            if (serverDatabase.get(x).equals(temp)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Tests if the user needs to be removed from the server due to being disconnected
     * If not then it sends User: message to the send method to send to all connected users
     * @param ID the socket number
     * @param msg The message the user sent
     * @param user user's name
     */
    public synchronized void handle(int ID, String msg, String user) {
        if (msg.equals(".bye")) {
            clients.get(findClient(ID)).send(".bye");
            remove(ID);
        } else {
            for (int x = 0; x < count; x++) {
                clients.get(x).send(user + ": " + msg);
            }
        }
    }
    
    /**
     * finds the client based off their client ID which is their socket number
     * @param ID the socket number
     * @return returns index of the client searched or -1 if does not exist
     */
    private int findClient(int ID) {
        for (int x = 0; x < count; x++) {
            if (clients.get(x).getID() == ID) {
                return x;
            }
        }
        return -1;
    }
    
    /**
     * Removes a client from the serverThread ArrayList and then closes the serverThread
     * @param ID the socket number
     */
    public void remove(int ID) {
        int index = findClient(ID);
        if (index >= 0) {
            ServerThread temp = clients.remove(index);
            System.out.println("Removing client at: " + index);
            count--;
            
            try {
                temp.close();
            } catch (IOException ex) {
                System.out.println("Server.close " + ex);
            }
        }
    }
    
    /**
     * This method stops the thread
     */
    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }
    
    public static void main(String[] args) {
        
        Scanner inFile = null;
        try {
            inFile = new Scanner(new File("ServerDatabase.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        serverDatabase = new ArrayList<>();
        StringTokenizer stok;
        
        while(inFile.hasNext()) {
            stok = new StringTokenizer(inFile.nextLine(), ",\n");
            serverDatabase.add(new ServerDatabase(stok.nextToken(), stok.nextToken()));
            System.out.println("1");
        }
        
        inFile.close();

        Server server = new Server(5000);
    }
}
