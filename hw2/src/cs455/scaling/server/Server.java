package cs455.scaling.server;
import java.io.*;
import java.net.*;
public class Server {

    private InetAddress hostAddress;
    private int port;

    public Server(InetAddress host, int port) {
        this.hostAddress = host;
        this.port = port;
        display(""+hostAddress);
        display(""+port);
    }

    private void display(String str) { System.out.println(str); }
    public static void main(String[] args) {
        try {
            new Server(InetAddress.getByName("localhost"), 8080);
        //} catch(IOException ioe) { ioe.printStackTrace(); }
        } catch(UnknownHostException uhe) { uhe.printStackTrace(); }
    }
}
