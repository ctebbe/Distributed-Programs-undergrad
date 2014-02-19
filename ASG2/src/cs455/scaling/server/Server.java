package cs455.scaling.server;

import java.net.InetAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Scanner;

/**
 * Created by crt on 2/18/14.
 */
public class Server implements Runnable {

    private InetAddress hostAddress;
    private int port;

    private ServerSocketChannel serverChannel; // channel to accept connections

    private Selector selector; // selector to monitor




    public Server(InetAddress host, int port) {
        this.hostAddress = host;
        this.port = port;
    }
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        while(true) System.out.println(kb.nextLine());
    }

    @Override
    public void run() {

    }
}
