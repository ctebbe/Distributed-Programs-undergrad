package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import java.util.*;
import java.io.*;
public class NodeList implements Event {

    private Header header = null;
    private int connectionSize = -1; // number of nodes to connect to
    private String[] connectionList;

    public NodeList(Header header, int numNodes, String[] list) {
        this.header = header;
        this.connectionSize = numNodes;
        this.connectionList = list;
    }

    public NodeList(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        // header
        this.header = Header.parseHeader(din);

        // num connection
        this.connectionSize = din.readInt();

        // list
        this.connectionList = new String[this.connectionSize];
        for(int i=0; i < this.connectionSize; i++) {
            int len = din.readInt();
            byte[] strBytes = new byte[len];
            din.readFully(strBytes);
            this.connectionList[i] = new String(strBytes);
        }

        bais.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baos));

        // header
        byte[] headerBytes = header.getBytes();
        dout.write(headerBytes);

        //  num nodes
        dout.writeInt(getConnectionSize());

        // list
        for(String str : getConnectionList()) {
            byte[] infoBytes = str.getBytes();
            dout.writeInt(infoBytes.length);
            dout.write(infoBytes);
        }

        // clean up
        dout.flush();
        marshalledBytes = baos.toByteArray();

        baos.close();
        dout.close();
        return marshalledBytes;
    }

    public int getType() { return this.header.getType(); }
    public String getIP() { return this.header.getIP(); }
    public int getPort() { return this.header.getPort(); }
    public String getSenderKey() { return this.header.getSenderKey(); }
    public int getConnectionSize() { return this.connectionSize; }
    public String[] getConnectionList() { return this.connectionList; }
    public String toString() {
        return header.toString() + " \n\tConnection #:" + getConnectionSize() + 
            " \n\tList:\t" + getConnectionListString();
    }
    public String getConnectionListString() {
        String ret = "";
        for(String s : getConnectionList()) {
            ret += "\t\t" + s + "\n";
        }
        return ret.trim();
    }
}
