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
    private OverlayCreator overlayCreator;
    private String[][] overlay;
    private String[] linkWeights;

    // factories
    private EventFactory eventFactory = EventFactory.getInstance();
    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public Registry(int port) {
        try {
            serverThread        = new TCPServerThread(this, new ServerSocket(port));
            connectionMap       = new HashMap<>();
            connectionBuffer    = new ArrayList<>();
            overlayCreator      = new OverlayCreator();
            run();
        } catch(IOException ioe) {
            display("IOException thrown:"+ioe.toString());
        }
    }

    public synchronized void onEvent(Event event) {
        display("Event:"+event.toString());
        switch(event.getType()) {
            case Protocol.REGISTER:
                NodeConnection toAdd = getBufferedConnection(((Register) event).getSenderKey());
                //display(Boolean.toString(toAdd == null));
                //display(toAdd.getHashKey());
                toAdd.setServerPort(((Register) event).getServerPort());
                addConnectionToRegistry(toAdd);
                break;
            default:
                display("unknown event type.");
        }
    }


    // *** REGISTRATION ***

    // buffers incoming connections to wait for a registration request
    public synchronized void registerConnection(NodeConnection connection) {
        //display("buffered new connection. hash key:"+connection.getHashKey());
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
        //display("key to get from buffer:"+key);
        for(NodeConnection nc : this.connectionBuffer) {
            //display("this key:"+nc.getHashKey());
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

    public void run() throws IOException {
        serverThread.start();
        Scanner keyboard = new Scanner(System.in);
        String input = keyboard.nextLine();
        while(input != null) {
            //display(input);
            if(input.contains("setup-overlay")) setupOverlay();
            else if(input.contains("send-link-weights")) setupAndSendLinkWeights();
            else if(input.contains("list-messaging nodes")) printRegisteredMessagingNodes();
            else if(input.contains("list-weights")) printLinkWeights();
            else if(input.contains("start")) sendTaskInitiate();
            input = keyboard.nextLine();
        }
    }

    private void sendTaskInitiate() throws IOException {
        for(NodeConnection node : getNodeConnectionArray()) {
            node.sendEvent(eventFactory.buildTaskInitiateEvent(node));
        }
    }

    private void printLinkWeights() {
        if(this.linkWeights == null) return;
        for(String link : this.linkWeights) {
            display(link);
        }
    }

    private void setupAndSendLinkWeights() throws IOException {
        NodeConnection[] nodes  = getNodeConnectionArray();
        this.linkWeights        = overlayCreator.assignLinkWeights(nodes, this.overlay);
        for(NodeConnection nc : nodes) { // send generated link weights to all registered nodes
            nc.sendEvent(eventFactory.buildLinkWeightsEvent(nc, linkWeights.length, linkWeights));
        }
    }

    private void printRegisteredMessagingNodes() {
        for(NodeConnection nc : getNodeConnectionArray()) {
            display(nc.getServerKey());
        }
    }

    // sets up connections and link weights
    private void setupOverlay() throws IOException {
        NodeConnection[] nodes  = getNodeConnectionArray();
        this.overlay            = overlayCreator.generateOverlay(nodes);
        for(int i=0; i < nodes.length; i++) {
            if(overlay[i].length < 1) continue; // skip nodes with noone to connect to
            nodes[i].sendEvent(eventFactory.buildNodeListEvent(nodes[i], overlay[i].length, overlay[i]));
        }
    }

    public int getPort() { return serverThread.getPort(); }
    private NodeConnection[] getNodeConnectionArray() {
        return connectionMap.values().toArray(new NodeConnection[0]);
    }

    public static void main(String args[]) {
        Registry registry = null;
        int port = 8082; // default port

        if(args.length > 0) port = Integer.parseInt(args[0]);

        registry = new Registry(port);
    }
}
