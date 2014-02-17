package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import cs455.overlay.util.*;
import java.util.*;
import java.io.*;
public class Message implements Event {

    private Header header = null;
    private int payload = -1;
    private String[] pathArray;

    public Message(Header header, int payload, String[] list) {
        this.header = header;
        this.payload = payload;
        this.pathArray = list;
    }

    public Message(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        // header
        this.header = Header.parseHeader(din);

        // payload
        this.payload = din.readInt();

        // path
        int pathSize = din.readInt();
        this.pathArray = new String[pathSize];
        for(int i=0; i < pathSize; i++) {
            int len = din.readInt();
            byte[] strBytes = new byte[len];
            din.readFully(strBytes);
            this.pathArray[i] = new String(strBytes);
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

        //  payload
        dout.writeInt(this.payload);

        // path
        dout.writeInt(getMessagePathSize());
        for(String str : getMessagePathArray()) {
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

    public boolean isRelayMessage() { return (this.pathArray.length > 1); }
    public boolean isFinalDestination() { return (this.pathArray.length == 1); }
    public String getNextDestination() { return Util.stripIP(this.pathArray[0]); }

    public int getPayload() { return this.payload; }
    public String[] getMessagePathArray() { return this.pathArray; }
    public void setMessagePathArray(String[] array) { this.pathArray = array; }
    public int getMessagePathSize() { return getMessagePathArray().length; }

    public String toString() {
        return header.toString() + " \n\tHops #:" + getMessagePathSize() +
            " \n\tList:\t" + getMessagePathString();
    }
    public String getMessagePathString() {
        String ret = "";
        for(String s : getMessagePathArray()) {
            ret += "\t\t" + s + "\n";
        }
        return ret.trim();
    }
}
