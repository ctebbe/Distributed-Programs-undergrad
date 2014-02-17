package cs455.overlay.node;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.util.*;
import cs455.overlay.dijkstra.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class MessageTaskThread extends Thread {

    private MessageTracker tracker = null;
    private HashMap<String, NodeConnection> connectionMap = null;
    private String[] linkWeights = null;
    private RoutingCache routeCache = null;

    public MessageTaskThread(HashMap<String, NodeConnection> map, MessageTracker tracker, String[] links, RoutingCache cache) {
        this.tracker = tracker;
        this.connectionMap = map;
        this.linkWeights = links;
        this.routeCache = cache;
    }

    public void run() {
    }

    private String getRandomDestinationNode() {

    }
    // get a connection key from the link but not ourselves
    private String getValidDestinationKey(String link) {
        String[] tokens = link.split(Pattern.quote(" "));
        if(tokens[0].equals(routeCache.getSourceKey())) return tokens[1];
        return tokens[0];
    }
}
