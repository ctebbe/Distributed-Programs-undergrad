package cs455.overlay.wireformats;
import java.util.*;
import java.io.*;
public class RegisterResponse implements Event {

    private Header header = null;
    private byte status          = Protocol.NOSTATUS; // success or failure status code
    private String information   = null;              // additional info about success for failure

    public RegisterResponse(Header header, byte status, String info) {
        this.header = header;
        this.status = status;
        this.information = info;
    }
    public String toString() {
        return header.toString() + " status:" + getStatus() + " information:" + getInformation();
    }

    public RegisterResponse(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        // type
        int type = din.readInt();
        // IP
        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];
        din.readFully(ipBytes);
        String IP = new String(ipBytes);
        // port
        int port = din.readInt();
        // header
        this.header = new Header(type, IP, port);

        // status
        this.status = din.readByte();

        // info
        int infoLength = din.readInt();
        byte[] infoBytes = new byte[infoLength];
        din.readFully(infoBytes);
        this.information = new String(infoBytes);

        bais.close();
        din.close();
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

        // status
        dout.writeByte(getStatus());

        // info
        byte[] infoBytes = getInformation().getBytes();
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
    public byte getStatus() { return this.status; }
    public String getInformation() { return this.information; }
}
