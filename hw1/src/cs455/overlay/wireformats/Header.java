package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import java.net.*;
public class Header {
    private int protocol;
    private int port = -999; 
    private String IP;
    private String key;

    // build a new header from connection key info
    public Header(int protocol, NodeConnection connection) {
        this.protocol = protocol;
        this.key = connection.getKey();
        int delimIndex = connection.getKey().indexOf(":");
        this.IP = key.substring(0, delimIndex);
        this.port = Integer.parseInt(key.substring(delimIndex+1));
    }

    // build a new header from local socket info
    public Header(int protocol, Socket socket) {
        this.protocol = protocol;
        this.IP = socket.getLocalAddress().toString();
        this.port = socket.getLocalPort();
        this.key = IP + ":" + port;
    }

    // build new custom header
    public Header(int protocol, String ip, int port) {
        this.protocol = protocol;
        this.key = ip+":"+port;
        this.IP = ip;
        this.port = port;
    }

    public String toString() {
        return "Type:"+getType() + " Key:"+getKey();
    }

    public int getPort() { return this.port; }
    public String getIP() { return this.IP; }
    public String getKey() { return this.key; }
    public int getType() { return this.protocol; }
}
