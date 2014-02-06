package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import java.util.*;
import java.io.*;
import java.net.*;
public class Header implements Event {
    private int protocol;
    private int port = -999;
    private String IP;
    private String senderKey;

    // build a new header from connection info
    public Header(int protocol, NodeConnection connection) {
        this.protocol = protocol;
        this.senderKey = connection.getEventKey();

        int delimIndex = senderKey.indexOf(":");
        this.IP = senderKey.substring(0, delimIndex);
        this.port = Integer.parseInt(senderKey.substring(delimIndex+1));
    }

    // build new custom header
    public Header(int protocol, String ip, int port) {
        this.protocol = protocol;
        this.senderKey = ip+":"+port;
        this.IP = ip;
        this.port = port;
    }

    // strips a header out of the input stream and returns a new header
    public static Header parseHeader(DataInputStream din) throws IOException {

        int type = din.readInt();

        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];
        din.readFully(ipBytes);
        String IP = new String(ipBytes);

        int port = din.readInt();

        return new Header(type, IP, port);
    }

    public int getType() { return this.protocol; }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baos));

        // header
        // type
        dout.writeInt(getType());
        // IP
        byte[] ipBytes = getIP().getBytes();
        dout.writeInt(ipBytes.length);
        dout.write(ipBytes);
        // port
        dout.writeInt(getPort());

        // clean up
        dout.flush();
        marshalledBytes = baos.toByteArray();
        baos.close();
        dout.close();
        return marshalledBytes;
    }

    public String toString() {
        return "Type:"+Protocol.getProtocolString(getType()) + " Sender Key:"+getSenderKey();
    }

    public int getPort() { return this.port; }
    public String getIP() { return this.IP; }
    public String getSenderKey() { return this.senderKey; }
}
