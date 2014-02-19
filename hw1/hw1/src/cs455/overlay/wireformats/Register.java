package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import java.util.*;
import java.io.*;
public class Register implements Event {

    private Header header;
    private int port;

    public Register(int protocol, NodeConnection connection, int port) {
        header = new Header(protocol, connection);
        this.port = port;
    }

    public Register(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        // header
        this.header = Header.parseHeader(din);

        // port
        this.port = din.readInt();

        bais.close();
        din.close();
    }

    public int getType() { return this.header.getType(); }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baos));

        // header
        byte[] headerBytes = header.getBytes();
        dout.write(headerBytes);

        // port
        dout.writeInt(getServerPort());

        // clean up
        dout.flush();
        marshalledBytes = baos.toByteArray();

        baos.close();
        dout.close();
        return marshalledBytes;
    }
    public String getIP() { return this.header.getIP(); }
    public int getServerPort() { return this.port; }
    public String getSenderKey() { return this.header.getSenderKey(); }

    public String toString() {
        return header.toString() + "\n\t" + "Port:"+getServerPort();
    }
}
