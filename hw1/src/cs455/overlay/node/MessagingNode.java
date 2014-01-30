package cs455.overlay.node;
import cs455.overlay.transport.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MessagingNode implements Node { // , Runnable
    /*public void onEvent(Event event) {
    }*/
    private Socket registrySocket = null;
    private TCPReceiverThread receiver = null;
    private TCPSender sender = null;
    private int port = Integer.MAX_VALUE;
    private String host = null;
    private Scanner keyboard = new Scanner(System.in);

    public MessagingNode(String host, int port) {
        try {
            this.port = port;
            this.host = host;
            registrySocket = new Socket(host, port);
            sender = new TCPSender(registrySocket);
            receiver = new TCPReceiverThread(this, registrySocket);
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
        while(registrySocket != null) {
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
        String host = "localhost";
        int port = 8080; // default port
        if(args.length > 0) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        node = new MessagingNode(host, port);
        node.run();
        /*
        Thread thread = new Thread(node);
        thread.start();
        */
    }
}
