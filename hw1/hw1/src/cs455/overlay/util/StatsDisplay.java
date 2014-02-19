package cs455.overlay.util;
import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;
import java.net.*;
import java.io.*;
import java.util.*;
public class StatsDisplay {
    public static void calculateAndDisplayStats(ArrayList<TrafficSummary> statsList) {
        int sumSent=0, sumReceived=0;
        Long sumSummationSent = new Long(0), sumSummationReceived = new Long(0);

        for(TrafficSummary nodeSummary : statsList) {
            int sent = nodeSummary.getSentTracker(); 
            int received = nodeSummary.getReceiveTracker(); 
            int relayed = nodeSummary.getRelayTracker(); 
            Long summationSent = nodeSummary.getSummationSent();
            Long summationReceived = nodeSummary.getSummationReceived();

            System.out.println("Node:"+nodeSummary.getIP());
            System.out.println("\tNumber of messages sent:"+sent);
            System.out.println("\tNumber of messages received:"+received);
            System.out.println("\tNumber of messages relayed:"+relayed);
            System.out.println("\tSummation of sent messages:"+summationSent);
            System.out.println("\tSummation of received messages:"+summationReceived);

            sumSent += sent;
            sumReceived += received;
            sumSummationSent += summationSent;
            sumSummationReceived += summationReceived;
        }
        System.out.println("*** TRAFFIC SUMMARY *** ");
        System.out.println("Total messages sent:"+sumSent);
        System.out.println("Total messages received:"+sumReceived);
        System.out.println("Total summation of messages sent:"+sumSummationSent);
        System.out.println("Total summation of messages received:"+sumSummationReceived);
    }
}
