package cs455.overlay.transport;
import cs455.overlay.node.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.util.*;
import java.io.*;
import java.net.*;

// a separate thread to read data from a connected socket
public class TCPReceiverThread extends Thread {

    private Socket socket;
    private Node node;
    private DataInputStream din;

    public TCPReceiverThread (Node node, Socket socket) throws IOException {
        this.node = node;
        this.socket = socket;
        din = new DataInputStream(socket.getInputStream());
    }

    public void run() {
    //public void start() {
        while(socket != null) {
            try {
                node.onEvent(EventFactory.getInstance().buildEvent(receiveData()));
            } catch(SocketException se) {
                System.out.println("Socket error in receiver thread:"+se.getMessage());
                break;
            } catch(IOException ioe) {
                System.out.println("IO Exception in receiver thread:"ioe.getMessage());
                break;
            }
        }
        closeConnection();
    }

    // close the socket and in stream if anything goes wrong
    private void closeConnection() {
        try {
            din.close();
            socket.close();
        } catch(IOException ioe) {
            //System.out.println(ioe.getMessage()); // who cares?
        }
    }

    private byte[] receiveData() throws IOException, SocketException {
        int dataLen = din.readInt();
        byte[] data = new byte[dataLen];

        din.readFully(data, 0, dataLen); // read everything coming thru the pipe
        return data;
    }
}
