import java.io.*;
import java.net.*;
public class TCPServerThread extends Thread {
    
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private Registry registry;
    private int ID;

    public TCPServerThread (Registry registry, Socket socket) throws IOException {
        this.registry = registry;
        this.socket = socket;
        this.ID = socket.getPort();
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
        int dataLen;
        while(socket != null) {
            try {
                dataLen = din.readInt();
                byte[] data = new byte[dataLen];
                din.readFully(data, 0, dataLen);
            } catch(SocketException se) {
                System.out.println(se.getMessage());
                break;
            } catch(IOException ioe) {
                System.out.println(ioe.getMessage());
                break;
            }
        }
    }
}
