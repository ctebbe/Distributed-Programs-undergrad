package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import java.util.*;
import java.io.*;
public class Register implements Event {

    private Header header;

    public Register(int protocol, NodeConnection connection) {
        header = new Header(protocol, connection);
    }
    public Register(Header header) {
        this.header = header;
    }

    public Register(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        this.header = Header.parseHeader(din);

        bais.close();
        din.close();
    }

    public int getType() { return this.header.getType(); }

    public byte[] getBytes() throws IOException {
        return this.header.getBytes();
    }
    public String getIP() { return this.header.getIP(); }
    public int getPort() { return this.header.getPort(); }
    public String getSenderKey() { return this.header.getSenderKey(); }

    public String toString() {
        return header.toString();
    }
}
