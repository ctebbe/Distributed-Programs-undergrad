package cs455.overlay.node;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.util.*;
import cs455.overlay.dijkstra.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;
// a separate thread to pick a random registered node and send a Message there for all rounds
public class MessageTaskThread extends Thread {

    private MessageTracker tracker = null;
    private HashMap<String, NodeConnection> connectionMap = null;
    private String[] linkWeights = null;
    private RoutingCache routeCache = null;
    private EventFactory eventFactory = EventFactory.getInstance();
    private MessagingNode messageNode = null;

    public MessageTaskThread(MessagingNode node, HashMap<String, NodeConnection> map, 
        MessageTracker tracker, String[] links, RoutingCache cache) {
        this.messageNode = node;
        this.tracker = tracker;
        this.connectionMap = map;
        this.linkWeights = links;
        this.routeCache = cache;
    }

    public void run() {
        try {
            for(int i=0; i < 5000; i++) { // 5000 rounds
                Thread.sleep(10); // give other nodes a change to complete last round

                String destinationNode = getRandomDestinationNode(); // destination for this round
                for(int j=0; j < 5; j++) { // 5 messages per round
                    Message message = (Message) eventFactory.buildMessageEvent(
                        Util.generateRandomNumber(), 
                        routeCache.getMessageRoutingPlanTo(destinationNode));

                    // update tracker
                    tracker.messageSent();
                    tracker.addSendSummation(message.getPayload());

                    // send message to next recipient
                    connectionMap.get(Util.stripIP(message.getNextDestination())).sendEvent(message);
                }
            }
            // notify node done with task
            messageNode.endTask();
        } 
        catch(IOException ioe) { System.out.println("IOE thrown:"+ioe.toString()); }
        catch(InterruptedException ie) { Thread.currentThread().interrupt(); }

    }

    private String getRandomDestinationNode() {
        return getValidDestinationKey(this.linkWeights[Util.generateRandomNumber(0, this.linkWeights.length-1)]);
    }

    // get a connection key from the link but not our key
    private String getValidDestinationKey(String link) {
        String[] tokens = link.split(Pattern.quote(" "));
        if(tokens[0].equals(routeCache.getSourceKey())) return tokens[1];
        return tokens[0];
    }
}
