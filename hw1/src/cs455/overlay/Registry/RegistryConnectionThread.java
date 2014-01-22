package cs455.overlay.Registry;
import java.net.*;
import java.io.*;

public class RegistryConnectionThread extends Thread {
    private Registry registry = null;
    private Socket socket = null;
    private int id = -999;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    public int getID() { return this.id; }

    public RegistryConnectionThread(Registry registry, Socket socket) {
        super();
        this.registry = registry;
        this.socket = socket;
        this.id = socket.getPort();
    }

    public void run() {
        System.out.println("registry thread id:"+this.id+" started.");
        while(true) {
            try {
                //registry.handleNodeMessage(getID(), streamIn.readUTF());
            } catch(IOException ioe) { handleIOException(ioe); }
        }
    }

    public void send(String msg) {
        try {
            streamOut.writeUTF(msg);
            streamOut.flush(); // send anything remaining in the output buffer
        } catch(IOException ioe) { handleIOException(ioe); }
    }

    // open IO streams
    public void open() throws IOException {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    // close all sockets and IO streams
    public void close() throws IOException {
        if(streamOut != null) streamOut.close();
        if(streamIn != null) streamIn .close();
        if(socket != null) socket.close();
    }

    private void handleIOException(IOException ioe) {
        System.out.println("ERROR: id:"+this.id+". Error:"+ioe.getMessage());
        // registry.remove(ID);
        stop();
    }
}
