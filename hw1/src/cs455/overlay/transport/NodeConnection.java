package cs455.overlay.transport;
import cs455.overlay.node.*;
import cs455.overlay.util.*;
import cs455.overlay.wireformats.*;
import java.io.*;
import java.net.*;
public class NodeConnection {

    private String hashKey = null;
    private String eventKey = null;
    private Node node = null;
    private TCPReceiverThread receiver = null;
    private TCPSender sender = null;
    private Socket socket = null;

    public NodeConnection(Node node, Socket sock) throws IOException {
        this.hashKey =  Util.generateHashKey(sock);
        this.eventKey = Util.generateEventKey(sock);
        this.node = node;
        this.receiver = new TCPReceiverThread(node, sock);
        this.sender = new TCPSender(sock);
        this.socket = sock;

        node.registerConnection(this);
        receiver.start();
    }

    public void sendEvent(Event event) throws IOException {
        this.sender.sendEvent(event);
    }

    public String getHashKey() { return this.hashKey; }
    public String getEventKey() { return this.eventKey; }
    public String toString() { return getHashKey(); }

    public boolean equals(Object o) {
        if(o == this) return true;
        if(!(o instanceof NodeConnection)) return false;

        NodeConnection nc = (NodeConnection) o;
        return this.getHashKey().equals(nc.getHashKey());
    }
}
