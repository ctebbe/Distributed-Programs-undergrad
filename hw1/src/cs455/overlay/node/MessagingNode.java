package cs455.overlay.node;
import cs455.overlay.transport.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MessagingNode implements Runnable, Node {
    /*public void onEvent(Event event) {
    }*/
    private Socket socket = null;
    private TCPReceiverThread receiver = null;
    private TCPSender sender = null;
    private int port = Integer.MAX_VALUE;
    private String host = null;
    private Scanner keyboard = new Scanner(System.in);

    public MessagingNode(String host, int port) {
        try {
            this.port = port;
            this.host = host;
            socket = new Socket(host, port);
            sender = new TCPSender(socket);
            receiver = new TCPReceiverThread(this, socket);
            //display("hostname:"+serverSocket.getInetAddress().getHostName());
            //display("port:"+serverSocket.getLocalPort());
            initialize();
        } catch(IOException ioe) {
            display(ioe.toString());
        }
    }

    public void onEvent(String s) {
        display(s);
    }

    private void display(String str) {
        System.out.println(str);
    }

    private void initialize() {
        //if(connectionListener  == null) connectionListener = new Thread(this);
    }

    public void run() {
        display("run()");
        while(socket != null) {
            try {
                String input = keyboard.nextLine();
                while(input != null || !input.equalsIgnoreCase("quit")) {
                    sender.sendData(input.getBytes());
                    receiver.start();
                    input = keyboard.nextLine();
                }
            } catch(IOException ioe) { display("IOE thrown:"+ioe.getMessage()); }
        }
    }

    public int getPort() { return this.port; }

    public static void main(String args[]) {
        MessagingNode node = null;
        int port = 8080; // default port

        if(args.length < 1) System.out.println("No port provided, default to port 8080");
        else port = Integer.parseInt(args[0]);

        node = new MessagingNode("localhost", port);
        Thread thread = new Thread(node);
        thread.start();
    }
}
