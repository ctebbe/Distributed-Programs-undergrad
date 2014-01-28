import java.io.*;
import java.net.*;
public class TCPReceiverThread extends Thread {
    
    private Socket socket;
    private DataInputStream din;

    public TCPReceiverThread (Socket socket) throws IOException {
        this.socket = socket;
        din = new DataInputStream(socket.getInputStream());
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
