import java.io.*;
import java.net.*;
public class TCPSender {
    private Socket socket;
    private DataOutputStream dout;

    public TCPSender(Socket socket) throws IOException {
        this.socket = socket;
        this.dout   = new DataOutputStream(socket.getOutputStream());
    }

    public void sendData(byte[] toSend) throws IOException {
        int len = toSend.length;
        dout.writeInt(len);
        dout.write(toSend, 0, len);
        dout.flush();
    }
}
