package cs455.scaling.server;

import cs455.scaling.util.Util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.security.NoSuchAlgorithmException;

/**
 * Created by crt on 2/18/14.
 */
public class Server implements Runnable {

    private InetAddress hostAddress;
    private int port;

    private ServerSocketChannel serverChannel; // channel to accept connections

    private Selector selector; // selector to monitor

    public Server(InetAddress host, int port) throws IOException{
        this.hostAddress = host;
        this.port = port;
        selector = this.initializeSelector();
        //System.out.println(getHost());
        //System.out.println(getPort());
    }

    private Selector initializeSelector() throws IOException {
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // create non-blocking channel and bind it to our host and port
        this.serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(getHost(), getPort()));

        // invite connections
        SelectionKey selectionKey = serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);
        System.out.println("socketChannel's registered key:" +
                Util.stripInfo(selectionKey.channel()));
        return socketSelector;
    }

    // accepts a connection from a ServerSocketChannel
    private void accept(SelectionKey key) throws IOException {
        System.out.println("Accepting a connection from:"+Util.stripInfo(key.channel()));
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        // accept connection and turn blocking off
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);

        // register the channel with our selector and wait for data
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    // read byte data
    private void read(SelectionKey key) throws IOException, NoSuchAlgorithmException {
        System.out.println(Util.SHA1FromSocketChannel((SocketChannel) key.channel()));
    }

    @Override
    public void run() {
        while(true) {
            try {
                this.selector.select(); // get an event from a registered channel
                for(SelectionKey key : this.selector.selectedKeys()) {
                    if(!key.isValid()) continue;

                    //System.out.println("handling key from:"+key.channel().toString());

                    this.selector.selectedKeys().remove(key);
                    // handle the event for this key
                    if(key.isAcceptable()) this.accept(key);
                    else if(key.isReadable()) this.read(key);
                }
            }
            catch (IOException e) { e.printStackTrace(); }
            catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        }
    }
    private String getHost()    { return this.hostAddress.getHostName(); }
    private int getPort()       { return this.port; }

    public static void main(String[] args) {
        try {

            new Thread(new Server(InetAddress.getLocalHost(), 8080)).start();

        } catch (IOException e) { e.printStackTrace(); }
    }
}
