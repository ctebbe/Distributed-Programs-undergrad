package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import java.util.*;
import java.io.*;
public class NodeList implements Event {

    private Header header = null;
    private int numNodes = -1; // number of nodes in overlay
    private String nodeList;

    public NodeList(Header header, int numNodes, String list) {
        this.header = header;
        this.numNodes = numNodes;
        this.nodeList = list;
    }
    public String toString() {
        return header.toString() + " numNodes:" + getNumNodes() + " list:" + getNodeList();
    }

    public NodeList(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        // header
        this.header = Header.parseHeader(din);

        // status
        this.numNodes = din.readByte();

        // info
        int nodeListLength = din.readInt();
        byte[] listBytes = new byte[nodeListLength];
        din.readFully(listBytes);
        this.nodeList = new String(listBytes);

        bais.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baos));

        // header
        byte[] headerBytes = header.getBytes();
        //dout.writeInt(headerBytes.length);
        dout.write(headerBytes);

        //  num nodes
        dout.writeByte(getNumNodes());

        // info
        byte[] infoBytes = getNodeList().getBytes();
        dout.writeInt(infoBytes.length);
        dout.write(infoBytes);

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
    public int getNumNodes() { return this.numNodes; }
    public String getNodeList() { return this.nodeList; }
}
