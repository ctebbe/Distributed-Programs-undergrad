package cs455.overlay.node;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.util.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class MessageTracker {

    private int sendTracker, receiveTracker, relayTracker;
    private long sendSummation, receiveSummation;

    public MessageTracker() {
        sendTracker=0; receiveTracker=0; relayTracker=0;
        sendSummation=0; receiveSummation=0;
    }

    public void messageSent() { sendTracker++; }
    public void messageReceived() { receiveTracker++; }
    public void messageRelayed() { relayTracker++; }
    public void addSendSummation(int toAdd) { sendSummation += toAdd; }
    public void addReceiveSummation(int toAdd) { receiveSummation += toAdd; }
}
