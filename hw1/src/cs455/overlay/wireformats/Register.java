package cs455.overlay.wireformats;
import java.util.*;
import java.io.*;
public class Register implements Event {

    private Header header = null;

    public Register(Header header) {
        this.header = header;
    }

    public Register(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        int type = din.readInt();

        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];
        din.readFully(ipBytes);
        String IP = new String(ipBytes);

        int port = din.readInt();

        bais.close();
        din.close();
        this.header = new Header(type, IP, port);
    }

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
    public int getType() { return this.header.getType(); }
    public String getIP() { return this.header.getIP(); }
    public int getPort() { return this.header.getPort(); }
    public String getSenderKey() { return this.header.getKey(); }

    public String toString() {
        return header.toString();
    }
}
