package cs455.overlay.node;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.util.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.*;
public class MessageTracker {

    private AtomicInteger sendTracker, receiveTracker, relayTracker;
    private AtomicLong sendSummation, receiveSummation;

    public MessageTracker() {
        sendTracker = new AtomicInteger();
        receiveTracker = new AtomicInteger();
        relayTracker = new AtomicInteger();
        sendSummation = new AtomicLong();
        receiveSummation = new AtomicLong();

        sendTracker.set(0); receiveTracker.set(0); relayTracker.set(0);
        sendSummation.set(0); receiveSummation.set(0);
    }

    public void messageSent() { sendTracker.getAndIncrement(); }
    public void messageReceived() { receiveTracker.getAndIncrement(); }
    public void messageRelayed() { relayTracker.getAndIncrement(); }
    public void addSendSummation(int toAdd) { sendSummation.addAndGet(toAdd); }
    public void addReceiveSummation(int toAdd) { receiveSummation.addAndGet(toAdd); }

    public int getSendTracker() { return sendTracker.get(); }
    public int getReceiveTracker() { return receiveTracker.get(); }
    public int getRelayTracker() { return relayTracker.get(); }
    public Long getSendSummation() { return sendSummation.get(); }
    public Long getReceiveSummation() { return receiveSummation.get(); }

    public String toString() {
        return "Messages sent:"+getSendTracker() +
        "\nMessages received:"+getReceiveTracker() +
        "\nMessages relayed:"+getRelayTracker() +
        "\nMessage send summation:"+getSendSummation() +
        "\nMessage receive summation:"+getReceiveSummation();
    }
}

