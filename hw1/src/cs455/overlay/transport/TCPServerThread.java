package cs455.overlay.transport;
import cs455.overlay.node.*;
import java.io.*;
import java.net.*;
public class TCPServerThread { //extends Thread {

    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private Node node;
    private int ID;

    public TCPServerThread (Node node, Socket socket) throws IOException {
        this.node = node;
        this.socket = socket;
        this.ID = socket.getPort();
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }

    //public void run() {
    public void start() {
        int dataLen;
        //while(socket != null) {
            System.out.println("ServerThread connected on port:"+this.ID);
            try {
                dataLen = din.readInt();
                byte[] data = new byte[dataLen];
                din.readFully(data, 0, dataLen);
                node.onEvent(new String(data));
            } catch(SocketException se) {
                System.out.println(se.getMessage());
                //break;
            } catch(IOException ioe) {
                System.out.println(ioe.getMessage());
                //break;
            }
        //}
    }
}
