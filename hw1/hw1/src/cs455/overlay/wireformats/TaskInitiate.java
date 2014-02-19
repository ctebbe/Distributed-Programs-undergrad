package cs455.overlay.wireformats;
import java.util.*;
import java.io.*;
public class TaskInitiate implements Event {

    private Header header = null;

    public TaskInitiate(Header header) {
        this.header = header;
    }
    public String toString() {
        return header.toString();
    }

    public TaskInitiate(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        // header
        this.header = Header.parseHeader(din);

        bais.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {
        return this.header.getBytes();
    }

    public int getType() { return this.header.getType(); }
    public String getIP() { return this.header.getIP(); }
    public int getPort() { return this.header.getPort(); }
    public String getSenderKey() { return this.header.getSenderKey(); }
}
