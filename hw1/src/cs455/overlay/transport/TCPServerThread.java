package cs455.overlay.transport;
import cs455.overlay.node.*;
import java.io.*;
import java.net.*;
public class TCPServerThread { //}extends Thread {

    private ServerSocket serverSocket;
    private Node node;
    private TCPReceiverThread receiver = null;
    private TCPSender sender = null;

    public TCPServerThread (Node node, ServerSocket ssocket) throws IOException {
        this.node = node;
        this.serverSocket = ssocket;
        display("Serverthread listening on IP:"+serverSocket.getInetAddress().getLocalHost().toString());
        display("ServerThread listening on port:"+getPort());
    }

    //public void run() {
    public void start() {
        while(serverSocket != null) {
            try {
                Socket socket = serverSocket.accept();
                //while(socket.isConnected()) {
                receiver = new TCPReceiverThread(this.node, socket);
                sender = new TCPSender(socket);

                receiver.start();

                String str = "msg from serverThread";
                sender.sendData(str.getBytes());
                //}
            } catch(IOException ioe) { display("IOE thrown:"+ioe.getMessage()); }
        }
    }

    private void display(String s) { System.out.println(s); }
    public int getPort() { return serverSocket.getLocalPort(); }
}
