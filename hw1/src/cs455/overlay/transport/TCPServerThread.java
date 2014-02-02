package cs455.overlay.transport;
import cs455.overlay.node.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServerThread extends Thread {

    private ServerSocket serverSocket;
    private Node node;
    private TCPReceiverThread receiver = null;
    private TCPSender sender = null;
    private Scanner keyboard = new Scanner(System.in);

    public TCPServerThread (Node node, ServerSocket ssocket) throws IOException {
        this.node = node;
        this.serverSocket = ssocket;
        display("Serverthread listening on IP:"+serverSocket.getInetAddress().getLocalHost().toString());
        display("ServerThread listening on port:"+getPort());
    }

    public void run() {
    //public void start() {
        while(serverSocket != null) {
            try {
                Socket socket = serverSocket.accept();
                display("Accepted new client");
                receiver = new TCPReceiverThread(this.node, socket);
                sender = new TCPSender(socket);
                receiver.start();
                while(socket.isConnected()) {
                    String input = keyboard.nextLine();
                    while(input != null || !input.equalsIgnoreCase("quit")) {
                        sender.sendData(input.getBytes());
                        input = keyboard.nextLine();
                    }
                }
            } catch(IOException ioe) { display("IOE thrown:"+ioe.getMessage()); }
        }
    }

    private void display(String s) { System.out.println(s); }
    public int getPort() { return serverSocket.getLocalPort(); }
}
