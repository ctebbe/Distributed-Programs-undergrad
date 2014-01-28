import java.util.*;
import java.io.*;
public class Register implements Event {

    private int type    = Protocol.NOTYPE;
    private int port    = Protocol.NOTYPE;
    private String IP   = null;

    public Register(int type, String ip, int port) {
        this.type   = type;
        this.IP     = ip;
        this.port   = port;
    }

    public Register(byte[] marshalledBytes) throws IOException {
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
            Register reg = new Register(Protocol.REGISTER, "IP_address", 1234);
            Register reg2 = new Register(reg.getBytes());
            System.out.println(reg2.getType());
            System.out.println(reg2.getIP());
            System.out.println(reg2.getPort());
        } catch(IOException e) {
            System.out.println("failure");
        }
    }
}
