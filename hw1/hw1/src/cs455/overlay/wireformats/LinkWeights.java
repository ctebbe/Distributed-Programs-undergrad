package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import java.util.*;
import java.io.*;
public class LinkWeights implements Event {

    private Header header = null;
    private int linkSize = -1;
    private String[] linkArray;

    public LinkWeights(Header header, int numNodes, String[] list) {
        this.header = header;
        this.linkSize = numNodes;
        this.linkArray = list;
    }

    public LinkWeights(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        // header
        this.header = Header.parseHeader(din);

        // num connection
        this.linkSize = din.readInt();

        // list
        this.linkArray = new String[this.linkSize];
        for(int i=0; i < this.linkSize; i++) {
            int len = din.readInt();
            byte[] strBytes = new byte[len];
            din.readFully(strBytes);
            this.linkArray[i] = new String(strBytes);
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
        dout.writeInt(getLinkSize());

        // list
        for(String str : getLinkArray()) {
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
    public int getLinkSize() { return this.linkSize; }
    public String[] getLinkArray() { return this.linkArray; }
    public String toString() {
        return header.toString() + " \n\tLinks #:" + getLinkSize() + 
            " \n\tList:\t" + getLinkArrayString();
    }
    public String getLinkArrayString() {
        String ret = "";
        for(String s : getLinkArray()) {
            ret += "\t\t" + s + "\n";
        }
        return ret.trim();
    }
}
