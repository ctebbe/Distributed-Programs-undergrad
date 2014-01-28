//package cs455.overlay.node;
//import cs455.overlay.wireformats.Event;
import java.io.*;
import java.net.*;

public class Registry implements Runnable { //implements Node {
    /*public void onEvent(Event event) {
    }*/

    private ServerSocket serverSocket = null;
    private TCPReceiverThread receiver = null;
    private TCPSender sender = null;
    private int port = Integer.MAX_VALUE;

    public Registry(int port) {
        try {
            this.port = port;
            serverSocket = new ServerSocket(getPort());
            //display("hostname:"+serverSocket.getInetAddress().getHostName());
            display("port:"+serverSocket.getLocalPort());
            initialize();
        } catch(IOException ioe) {
            display(ioe.toString());
        }
    }

    private void display(String str) {
        System.out.println(str);
    }

    private void initialize() {
        //if(connectionListener  == null) connectionListener = new Thread(this);
    }

    public void run() {
        display("run()");
        while(serverSocket != null) {
            try {
                Socket socket = serverSocket.accept();
                while(socket.isConnected()) {
                    receiver = new TCPReceiverThread(socket);
                    sender = new TCPSender(socket);
                    receiver.start();
                    String str = "msg from server";
                    sender.sendData(str.getBytes());
                }
            } catch(IOException ioe) { display("IOE thrown:"+ioe.getMessage()); }
        }
    }

    public int getPort() { return this.port; }

    public static void main(String args[]) {
        Registry registry = null;
        int port = 8080; // default port

        if(args.length < 1) System.out.println("No port provided, default to port 8080");
        else port = Integer.parseInt(args[0]);

        registry = new Registry(port);
        Thread thread = new Thread(registry);
        thread.start();
    }
}
