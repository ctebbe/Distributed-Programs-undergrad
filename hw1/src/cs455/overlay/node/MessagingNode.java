package cs455.overlay.node;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.util.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MessagingNode implements Node { // , Runnable

    private NodeConnection connectionRegistry = null;
    private Scanner keyboard = new Scanner(System.in);

    // factories
    private EventFactory eventFactory = EventFactory.getInstance();
    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public MessagingNode(String host, int port) {
        try {
            connectionRegistry = connectionFactory.buildConnection(this, new Socket(host, port));
        } catch(IOException ioe) { display("Error connecting to registry:"+ioe.getMessage()); }
        register();
    }

    public void onEvent(Event event) {
        switch(event.getType()) {
            case Protocol.REGISTER_RESPONSE:
                display("Register response received:"+event.toString());
            case Protocol.MESSAGING_NODES_LIST:
                display("node list received:"+event.toString());
            default:
        }
    }

    public void registerConnection(NodeConnection connection) {
    }

    public void deregisterConnection(NodeConnection connection) {
    }

    private void display(String str) {
        System.out.println(str);
    }

    // attempts to send a register request to the connectionRegistry
    private void register() {
        try { connectionRegistry.sendEvent(eventFactory.buildRegisterEvent(connectionRegistry));
        } catch(IOException ioe) { display("Error sending register request:"+ioe.getMessage()); }
    }

    public void run() {
        /*while(registrySocket != null) {
            try {
                String input = keyboard.nextLine();
                while(input != null || !input.equalsIgnoreCase("quit")) {
                    sender.sendData(input.getBytes());
                    input = keyboard.nextLine();
                }
            } catch(IOException ioe) { display("IOE thrown:"+ioe.getMessage()); }
        }*/
    }

    public static void main(String args[]) {
        MessagingNode node = null;
        String host = "localhost";
        int port = 8082; // default port
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
