package cs455.overlay.node;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.util.*;
import cs455.overlay.dijkstra.*;
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
    private RoutingCache routeCache = null;

    // factories
    private EventFactory eventFactory = EventFactory.getInstance();
    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public MessagingNode(String host, int port) {
        try {
            connectionMap = new HashMap<>();
            connectionRegistry = connectionFactory.buildConnection(this, new Socket(host, port));
            serverThread = new TCPServerThread(this, new ServerSocket(0)); // listen on first avail port
            tracker = new MessageTracker();
        } catch(IOException ioe) { display("Error connecting to registry:"+ioe.getMessage()); }
        init();
    }

    public synchronized void onEvent(Event event) {
        //display(event.toString());
        switch(event.getType()) {
            case Protocol.REGISTER_RESPONSE:
                if (((RegisterResponse) event).getInformation().contains("Deregister")) // in response to a deregister req
                    System.exit(0);
                break;
            case Protocol.MESSAGING_NODES_LIST:
                setupConnections(((NodeList) event).getConnectionList());
                break;
            case Protocol.LINK_WEIGHTS:
                this.linkWeights = ((LinkWeights) event).getLinkArray();
                routeCache = Dijkstra.generateRoutingPlan(serverThread.getHashKey(), this.linkWeights);
                break;
            case Protocol.TASK_INITIATE:
                startTask();
                break;
            case Protocol.MESSAGE:
                handleMessage(((Message) event));
                break;
            case Protocol.PULL_TRAFFIC_SUMMARY:
                sendTrafficSummary();
                this.tracker = new MessageTracker();
                break;
            default:
        }
    }

    private void sendTrafficSummary() {
        try { connectionRegistry.sendEvent(eventFactory.buildTrafficSummaryEvent(
                this.connectionRegistry, this.tracker));
        } catch(IOException ioe) { display("Error sending traffic summary"+ioe.toString()); }
    }

    private void handleMessage(Message message) {
        if(message.isFinalDestination()) {
            tracker.messageReceived();
            tracker.addReceiveSummation(message.getPayload());

        } else if(message.isRelayMessage()) {
            tracker.messageRelayed();
            // strip ourselves off the path and reset it
            message.setMessagePathArray(Util.stripFirstElement(message.getMessagePathArray()));
            // pass message off to its next destination
            connectionMap.get(Util.stripIP(message.getNextDestination())).sendEvent(message);
        }
    }


    private void startTask() {
        new MessageTaskThread(this, connectionMap, tracker, linkWeights, routeCache).start();
    }

    public void endTask() throws IOException {
        connectionRegistry.sendEvent(eventFactory.buildTaskCompleteEvent());
    }

    private void setupConnections(String[] connectionList) {
        for(String toConnect : connectionList) {
            String host = Util.stripIP(toConnect);
            int port = Util.stripPort(toConnect);

            try { connectionFactory.buildConnection(this, new Socket(host, port));
            } catch(IOException ioe) { display("Error connecting to node:"+ioe.getMessage()); }
        }
    }

    public synchronized void registerConnection(NodeConnection connection) {
        if(connectionRegistry == null) return; // avoid registering the registry
        //display("New connection:"+connection.toString());
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

    private void printShortestPath() {
        routeCache.toString(); 
    }
    private void sendDeregistrationRequest() {
        try { 
            connectionRegistry.sendEvent(eventFactory.buildDeregisterEvent(
                connectionRegistry, serverThread.getPort()));
        } catch(IOException ioe) { display("Error sending deregister request:"+ioe.getMessage()); }
    }
    public void run() {
        Scanner keyboard = new Scanner(System.in);
        String input = keyboard.nextLine();
        while(input != null) {
            //display(input);
            if(input.contains("print-shortest-path")) printShortestPath();
            else if(input.contains("exit-overlay")) sendDeregistrationRequest();
            input = keyboard.nextLine();
        }
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
