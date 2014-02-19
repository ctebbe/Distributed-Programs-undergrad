package cs455.overlay.wireformats;
import cs455.overlay.node.*;
import java.util.*;
import java.io.*;
public class TrafficSummary implements Event {

    private Header header = null;
    private int countSent;
    private int countReceived;
    private int countRelayed;
    private Long sumSent;
    private Long sumReceived;

    public TrafficSummary(Header header, MessageTracker tracker) {
        this.header = header;
        this.countSent = tracker.getSendTracker();
        this.countReceived = tracker.getReceiveTracker();
        this.countRelayed = tracker.getRelayTracker();
        this.sumSent = tracker.getSendSummation();
        this.sumReceived = tracker.getReceiveSummation();
    }

    public TrafficSummary(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        // header
        this.header = Header.parseHeader(din);

        // ints
        this.countSent = din.readInt();
        this.countReceived = din.readInt();
        this.countRelayed = din.readInt();

        // longs
        this.sumSent = din.readLong();
        this.sumReceived = din.readLong();

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

        // ints
        dout.writeInt(getSentTracker());
        dout.writeInt(getReceiveTracker());
        dout.writeInt(getRelayTracker());

        // longs
        dout.writeLong(getSummationSent());
        dout.writeLong(getSummationReceived());

        // clean up
        dout.flush();
        marshalledBytes = baos.toByteArray();

        baos.close();
        dout.close();
        return marshalledBytes;
    }

    public int getSentTracker() { return this.countSent; }
    public int getReceiveTracker() { return this.countReceived; }
    public int getRelayTracker() { return this.countRelayed; }
    public Long getSummationSent() { return this.sumSent; }
    public Long getSummationReceived() { return this.sumReceived; }

    public int getType() { return this.header.getType(); }
    public String getIP() { return this.header.getIP(); }
    public int getPort() { return this.header.getPort(); }
    public String getSenderKey() { return this.header.getSenderKey(); }
}
