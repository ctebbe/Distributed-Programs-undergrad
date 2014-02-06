package cs455.overlay.transport;
import cs455.overlay.node.*;
import java.io.*;
import java.net.*;
import java.util.*;

// a thread to act as the server spawning new connections to its node
public class TCPServerThread extends Thread {

    private ServerSocket serverSocket;
    private Node node;
    private TCPReceiverThread receiver = null;
    private TCPSender sender = null;
    private Scanner keyboard = new Scanner(System.in);

    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public TCPServerThread (Node node, ServerSocket ssocket) throws IOException {
        this.node = node;
        this.serverSocket = ssocket;
        display("Serverthread listening on IP:"+serverSocket.getInetAddress().getLocalHost().toString());
        display("ServerThread listening on port:"+getPort());
    }

    public void run() {
        while(serverSocket != null) {
            try {
                connectionFactory.buildConnection(node, serverSocket.accept());
            } catch(IOException ioe) { display("Error accepting new node connection:"+ioe.getMessage()); }
        }
    }

    private void display(String s) { System.out.println(s); }
    public int getPort() { return serverSocket.getLocalPort(); }
}
