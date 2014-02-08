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
    private Scanner keyboard = null; // listen for registry commands from user

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
        display("Event:"+event.toString());
        switch(event.getType()) {
            case Protocol.REGISTER:
                //display("Attempting to register node:"+((Register) event).getSenderKey());
                addConnectionToRegistry(getBufferedConnection(((Register) event).getSenderKey()));
                break;
            default:
                display("unknown event type.");
        }
    }


    // *** REGISTRATION ***

    // buffers incoming connections to wait for a registration request
    public synchronized void registerConnection(NodeConnection connection) {
        //display("buffered new connection. key:"+connection.getHashKey());
        connectionBuffer.add(connection);
    }

    // performs checks to register a node
    // sends a REGISTER_RESPONSE event indicating success or failure.
    private void addConnectionToRegistry(NodeConnection nc) {
        try {
            Event response;
            if(connectionMap.containsKey(nc.getHashKey())) { // check if connection already in connectionMap
                response = eventFactory.buildRegisterResponseEvent(nc, Protocol.FAILURE,
                    "Register failure. Connection already registered with Registry."+nc.getHashKey());

            } else { // passed checks, add new connection to connectionMap
                connectionMap.put(nc.getHashKey(), nc);
                response = eventFactory.buildRegisterResponseEvent(nc, Protocol.SUCCESS,
                    "Register success. Currently "+ connectionMap.size() + " nodes in the overlay.");
            }

            nc.sendEvent(response);

        } catch(IOException ioe) { display("Error sending regsiter request:"+ioe.toString()); }
    }

    // finds, removes, and returns a buffered connection with hashKey key
    private NodeConnection getBufferedConnection(String key) {
        for(NodeConnection nc : this.connectionBuffer) {
            if(nc.getHashKey().equals(key)) return connectionBuffer.remove(connectionBuffer.indexOf(nc));
        }
        return null;
    }

    // *** DEREGISTRATION ***

    public void deregisterConnection(NodeConnection connection) {
    }

    public void display(String str) {
        System.out.println(str);
    }

    public void run() {
        keyboard = new Scanner(System.in);
        serverThread.start();
        display("Server started");
        display("Server started");
        String input = keyboard.next();
        while(input != null) {
            display("Server started");
            display(input);
            if(input.equals("setup")) connectNodes();
            input = keyboard.nextLine();
        }
    }

    private void connectNodes() {
        NodeConnection[] conn = ((NodeConnection[])connectionMap.values().toArray());
        String node1 = conn[0].getHashKey();
        String node2 = conn[1].getHashKey();
        try {
            conn[0].sendEvent(eventFactory.buildNodeListEvent(conn[0], 1, node2));
            conn[1].sendEvent(eventFactory.buildNodeListEvent(conn[1], 1, node1));
        } catch(IOException ioe) { display("Error sending node list:"+ioe.toString()); }
    }

    public int getPort() { return serverThread.getPort(); }

    public static void main(String args[]) {
        Registry registry = null;
        int port = 8082; // default port

        if(args.length > 0) port = Integer.parseInt(args[0]);

        registry = new Registry(port);
    }
}
