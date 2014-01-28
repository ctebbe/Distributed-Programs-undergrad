import java.util.*;
import java.io.*;
public class RegisterResponse implements Event {

    private int type            = Protocol.NOTYPE;
    public byte status          = Protocol.NOSTATUS; // success or failure status code
    public String information   = null;              // additional info about success for failure

    public RegisterResponse(int type, byte status, String info) {
        this.type = type;
        this.status = status;
        this.information = info;
    }

    public RegisterResponse(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

        this.type = din.readInt();

        this.status = din.readByte();

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

        // type
        dout.writeInt(getType());

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

    public int getType() { return this.type; }
    public byte getStatus() { return this.status; }
    public String getInformation() { return this.information; }

    public static void main(String[] args) {
        try {
            RegisterResponse reg = new RegisterResponse(Protocol.REGISTER_RESPONSE, 
            Protocol.SUCCESS, "info1");
            RegisterResponse reg2 = new RegisterResponse(reg.getBytes());
            System.out.println(reg2.getType());
            System.out.println(reg2.getStatus());
            System.out.println(reg2.getInformation());
        } catch(IOException e) {
            System.out.println("failure");
        }
    }
}
