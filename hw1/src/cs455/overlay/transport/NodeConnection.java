package cs455.overlay.transport;
import cs455.overlay.node.*;
import cs455.overlay.util.*;
import cs455.overlay.wireformats.*;
import java.io.*;
import java.net.*;
public class NodeConnection {

    private String key = null;
    private Node node = null;
    private TCPReceiverThread receiver = null;
    private TCPSender sender = null;
    private Socket socket = null;
    
    public NodeConnection(Node node, Socket sock) throws IOException {
        this.key =  Util.generateKeyFromSocket(sock);
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

    public boolean equals(Object o) {
        if(o == this) return true;
        if(!(o instanceof NodeConnection)) return false;

        NodeConnection nc = (NodeConnection) o;
        return this.getKey().equals(nc.getKey());
    }
    public String toString() { return getKey(); }
    public String getKey() { return this.key; }
    public Socket getSocket() { return this.socket; }
}
