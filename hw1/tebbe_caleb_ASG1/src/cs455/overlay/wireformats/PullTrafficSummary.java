package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import java.util.*;
import java.io.*;
public class PullTrafficSummary implements Event {

    private int type;

    public PullTrafficSummary(int type) {
        this.type = type;
    }

    public PullTrafficSummary(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        // type
        this.type = din.readInt();

        bais.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baos));

        // type
        dout.writeInt(getType());

        // clean up
        dout.flush();
        marshalledBytes = baos.toByteArray();

        baos.close();
        dout.close();
        return marshalledBytes;
    }
    public int getType() { return this.type; }
    public String toString() {
        return "["+Protocol.getProtocolString(getType())+"]";
    }
}
