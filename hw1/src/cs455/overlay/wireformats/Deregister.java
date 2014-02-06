package cs455.overlay.wireformats;
import java.util.*;
import java.io.*;
public class Deregister implements Event {

    private int type    = Protocol.NOTYPE;
    private int port    = Protocol.NOTYPE;
    private String IP   = null;

    public Deregister(int type, String ip, int port) {
        this.type   = type;
        this.IP     = ip;
        this.port   = port;
    }

    public Deregister(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        this.type = din.readInt();

        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];
        din.readFully(ipBytes);
        this.IP = new String(ipBytes);

        this.port = din.readInt();

        bais.close();
        din.close();
    }

    public byte[] getBytes() throws IOException { 
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baos));

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
    public int getType() { return this.type; }
    public String getIP() { return this.IP; }
    public int getPort() { return this.port; }

    public static void main(String[] args) {
        try {
            Deregister reg = new Deregister(Protocol.DEREGISTER, "IP_address", 1234);
            Deregister reg2 = new Deregister(reg.getBytes());
            System.out.println(reg2.getType());
            System.out.println(reg2.getIP());
            System.out.println(reg2.getPort());
        } catch(IOException e) {
            System.out.println("failure");
        }
    }
}
