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
    private int serverPort = -999; // a place to hold which port a messaging node is listening on after it registers

    public NodeConnection(Node node, Socket sock) throws IOException {
        this.hashKey =  Util.generateHashKey(sock);
        this.eventKey = Util.generateEventKey(sock);
        this.node = node;
        this.receiver = new TCPReceiverThread(node, sock);
        this.sender = new TCPSender(sock);
        this.socket = sock;

        //System.out.println("LocalSocketAddress:"+sock.getLocalSocketAddress().toString().substring(1));
        //System.out.println("LocalAddress:"+sock.getLocalAddress().toString().substring(1));
        //System.out.println("RemoteAddress:"+sock.getRemoteSocketAddress().toString().substring(1));
        node.registerConnection(this);
        receiver.start();
    }

    public void sendEvent(Event event) throws IOException {
        this.sender.sendEvent(event);
    }

    // added server info for messaging nodes
    public void setServerPort(int port) { this.serverPort = port; }
    public int getServerPort() { return this.serverPort; }
    public String getServerKey() { return getIP() + ":" + Integer.toString(getServerPort()); }

    // basic info
    public String getIP() { return Util.stripIP(getHashKey()); }
    public int getPort() { return Util.stripPort(getHashKey()); }

    // hash keys
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
