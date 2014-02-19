package cs455.overlay.transport;
import cs455.overlay.node.*;
import cs455.overlay.wireformats.*;
import java.io.*;
import java.net.*;

// a class to write events to a connected socket
public class TCPSender {
    private Socket socket;
    private DataOutputStream dout;

    public TCPSender(Socket socket) throws IOException {
        this.socket = socket;
        this.dout   = new DataOutputStream(socket.getOutputStream());
    }

    public void sendEvent(Event event) throws IOException {
        byte[] toSend = event.getBytes();
        int len = toSend.length;
        dout.writeInt(len);
        dout.write(toSend, 0, len);
        dout.flush();
    }
}
