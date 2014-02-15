package cs455.overlay.node;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.util.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MessagingNode implements Node { // , Runnable

    private NodeConnection connectionRegistry = null;
    private HashMap<String, NodeConnection> connectionMap = null; // holds adjacent messaging nodes
    private TCPServerThread serverThread = null;
    private Scanner keyboard = new Scanner(System.in);
    private String[] linkWeights = null;
    private MessageTracker tracker = null; // init this when ready to collect stats

    // factories
    private EventFactory eventFactory = EventFactory.getInstance();
    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public MessagingNode(String host, int port) {
        try {
            connectionMap = new HashMap<>();
            connectionRegistry = connectionFactory.buildConnection(this, new Socket(host, port));
            serverThread = new TCPServerThread(this, new ServerSocket(0)); // listen on first avail port
        } catch(IOException ioe) { display("Error connecting to registry:"+ioe.getMessage()); }
        init();
    }

    public synchronized void onEvent(Event event) {
        //display(event.toString());
        switch(event.getType()) {
            case Protocol.REGISTER_RESPONSE:
                break;
            case Protocol.MESSAGING_NODES_LIST:
                setupConnections(((NodeList) event).getConnectionList());
                break;
            case Protocol.LINK_WEIGHTS:
                handleLinkWeights(((LinkWeights) event).getLinkArray());
                break;
            case Protocol.TASK_INITIATE:
                tracker = new MessageTracker();
                startTask();
            case Protocol.MESSAGE:
                handleMessage(((Message) event));
            default:
        }
    }

    private void handleMessage(Message message) {
        
    }

    private void startTask() {
    }

    private void handleLinkWeights(String[] weights) {
        this.linkWeights = weights;
        try {
            display("myHash:"+serverThread.getHashKey());
            // update connectionMap hash keys via IP-match to easily transfer 
            for(String link : linkWeights) {
                if(link.contains(serverThread.getIP())) { // one of our edges
                    display(link);
                    for(Map.Entry<String, NodeConnection> entry : connectionMap.entrySet()) {
                        NodeConnection node = entry.getValue();
                        if(link.contains(node.getIP())) {
                            display("\tfound match:"+node.toString());
                            display("\thash key:"+entry.getKey().toString());
                        }
                        
                    }
                }
            }
        } catch(IOException ioe) { display(ioe.toString()); }
    }

    private void setupConnections(String[] connectionList) {
        for(String toConnect : connectionList) {
            String host = Util.stripIP(toConnect);
            int port = Util.stripPort(toConnect);
            try {
                connectionFactory.buildConnection(this, new Socket(host, port));
            } catch(IOException ioe) { display("Error connecting to node:"+ioe.getMessage()); }
        }
    }

    public synchronized void registerConnection(NodeConnection connection) {
        if(connectionRegistry == null) return; // avoid registering the registry
        display("New connection:"+connection.toString());
        //display("event key:"+connection.getEventKey());
        connectionMap.put(connection.getIP(), connection);
        //connectionMap.put(connection.getHashKey(), connection);
    }

    public synchronized void deregisterConnection(NodeConnection connection) {
    }

    private void display(String str) {
        System.out.println(str);
    }

    // attempts to send a register request to the connectionRegistry
    private void init() {
        try { 
            serverThread.start();
            connectionRegistry.sendEvent(eventFactory.buildRegisterEvent(connectionRegistry, serverThread.getPort()));
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
