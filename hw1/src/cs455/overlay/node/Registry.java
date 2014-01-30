package cs455.overlay.node;
import cs455.overlay.transport.*;
import java.io.*;
import java.net.*;

public class Registry implements Node { //implements Runnable {

    private TCPServerThread serverThread = null;

    public Registry(int port) {
        try {
            //this.port = port;
            serverThread = new TCPServerThread(this, new ServerSocket(port));
            initialize();
        } catch(IOException ioe) {
            display(ioe.toString());
        }
    }

    public void onEvent(String s) {
        display(s);
    }

    public void display(String str) {
        System.out.println(str);
    }

    private void initialize() {
        run();
    }

    public void run() {
        serverThread.start();
    }

    public int getPort() { return serverThread.getPort(); }

    public static void main(String args[]) {
        Registry registry = null;
        int port = 8080; // default port

        if(args.length > 0) port = Integer.parseInt(args[0]);

        registry = new Registry(port);
    }
}
