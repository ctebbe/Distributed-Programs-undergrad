//package cs455.overlay.Registry;
import java.net.*;
import java.io.*;

public class Registry implements Runnable {
    private ServerSocket serverSocket = null;
    private Thread connectionListener = null;
    private int port = -999;
    private boolean preparedToStop = true;

    public Registry(int port) {
        try {
            this.port = port;
            display("port:"+getPort());
            serverSocket = new ServerSocket(getPort());
            initialize();
        } catch(IOException ioe) {
            display(ioe.toString());
        }
    }

    private void display(String s) {
        System.out.println(s);
    }

    private void initialize() {
        if(connectionListener  == null) connectionListener = new Thread(this);
    }

    private void stop() {
        //if(connectionListener != null)
    }

    public void run() {
        display("run()");
        while(connectionListener  != null) {
            //try {
                display("server listening for clients");
                //addThread(serverSocket.accept());
            /*} catch(IOException ioe) {
                display("Server error accepting client:"+ioe);

            }*/
        }
    }

    public void startListening() throws IOException {
        if(!isListening()) {
            if(serverSocket == null) {
                serverSocket = new ServerSocket(getPort(), 5); // num clients allowed in conn queue
            }
            connectionListener = new Thread(this);
            connectionListener.start();
        }
    }

    public boolean isListening() {
        //return (serverSocket != null && connectionListener != null && connectionListener.isAlive());
        return (connectionListener != null && connectionListener.isAlive());
    }

    public int getPort() { return this.port; }

    public static void main(String args[]) {
        Registry registry = null;
        int port = 8080; // default port

        if(args.length < 1) System.out.println("No port provided, default to port 8080");
        else port = Integer.parseInt(args[0]);

        registry = new Registry(port);
    }
}
