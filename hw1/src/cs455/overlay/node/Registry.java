package cs455.overlay.node;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.util.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Registry implements Node { //implements Runnable {

    private TCPServerThread serverThread = null; // listens for incoming messaging nodes
    private HashMap<String, NodeConnection> connectionMap = null; // holds registered messaging nodes 
    private ArrayList<NodeConnection> connectionBuffer = null; // holds connected but not yet registered nodes
    private Scanner keyboard = new Scanner(System.in);

    // factories
    private EventFactory eventFactory = EventFactory.getInstance();
    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public Registry(int port) {
        try {
            connectionMap = new HashMap<>();
            connectionBuffer = new ArrayList<>();
            serverThread = new TCPServerThread(this, new ServerSocket(port));
            run();
        } catch(IOException ioe) {
            display(ioe.toString());
        }
    }

    public synchronized void onEvent(Event event) {
        switch(event.getType()) {
            case Protocol.REGISTER:
                display("Register request received:"+event.toString());
                display("key:"+((Register)event).getSenderKey());
                break;
            default:
                display("unknown event type.");
        }
    }

    // buffers incoming connections to wait for a registration request
    public synchronized void registerConnection(NodeConnection connection) {
        display("buffered connection key:"+connection.getKey());
        connectionBuffer.add(connection);
        /*try {
            display("connection key:"+connection.getKey());
            connectionMap.put(connection.getKey(), connection);
            connection.sendEvent(
                eventFactory.buildRegisterResponseEvent(connection, Protocol.SUCCESS, "Successfully connected"));
            //connection.sendData("Registered");
        } catch(IOException ioe) { display("Error registering:"+ioe.toString()); }*/
    }

    public void deregisterConnection(NodeConnection connection) {
    }

    public void display(String str) {
        System.out.println(str);
    }

    public void run() {
        serverThread.start();
        //try {
            // listen for commands from keyboard
            String input = keyboard.nextLine();
            while(input != null || !input.equalsIgnoreCase("quit")) {
                //sender.sendData(input.getBytes());
                display(input);
                input = keyboard.nextLine();
            }
        //} catch(IOException ioe) { display("IOE thrown:"+ioe.getMessage()); }
    }

    public int getPort() { return serverThread.getPort(); }

    public static void main(String args[]) {
        Registry registry = null;
        int port = 8082; // default port

        if(args.length > 0) port = Integer.parseInt(args[0]);

        registry = new Registry(port);
    }
}
