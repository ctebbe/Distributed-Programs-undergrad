package cs455.scaling.client;

import cs455.scaling.util.Util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by ctebbe on 2/20/14.
 */
public class Client implements Runnable {

    private InetAddress hostAddress = null;
    private int port = Integer.MIN_VALUE;
    private int sendRate = 0;
    private int successCounter = 0; // a counter to keep track of how many successful hashes received

    private ArrayList<String> sentDataHashList = null;

    private Selector selector = null;

    public Client(InetAddress host, int port, int sendRate) throws IOException {
        this.hostAddress = host;
        this.port = port;
        this.sendRate = sendRate;
        sentDataHashList = new ArrayList<String>();
        this.selector = SelectorProvider.provider().openSelector();
        initiateConnection();
    }

    private SocketChannel initiateConnection() throws IOException {
        // open a non-blocking channel
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);

        // connect to our host and port
        channel.connect(new InetSocketAddress(getHost(), getPort()));

        // register with our selector
        selector = Selector.open();
        SelectionKey key = channel.register(selector, SelectionKey.OP_CONNECT);
        System.out.println("key:"+key.channel().toString());

        return channel;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                selector.select();
                for(SelectionKey key : selector.selectedKeys()) {
                    selector.selectedKeys().remove(key);
                    SocketChannel currentChannel = (SocketChannel) key.channel();

                    if(key.isConnectable()) {
                        System.out.println("reading connectable key");
                        //System.out.println("finish connect?:"+currentChannel.finishConnect());
                        while(!currentChannel.finishConnect()) { System.out.println("still connecting..."); }
                        key.interestOps(SelectionKey.OP_READ);

                        // start up our sending task to spam the server
                        new Thread(new ClientSender(currentChannel, this.sentDataHashList, this.sendRate)).start();

                    } else if(key.isReadable()) { // read a hash from the server and remove it from the data hash list
                        String readHash = readHashFromSocketChannel(currentChannel);
                        //System.out.println(readHash);
                        synchronized (this.sentDataHashList) {
                            if(!this.sentDataHashList.remove(readHash))
                                System.out.println("***** FAILURE *****");
                            else
                                System.out.println(++successCounter + " SUCCESS!");
                        }
                    }
                }
            }
        }
        catch (IOException e) { e.printStackTrace(); }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
    }

    private static String readHashFromSocketChannel(SocketChannel socketChannel)
            throws IOException, NoSuchAlgorithmException {

        ByteBuffer readBuffer = ByteBuffer.allocate(Util.MAX_HASH_SIZE);

        int bytesRead = socketChannel.read(readBuffer);
        if(bytesRead == -1) {
            socketChannel.close();
            System.out.println("Lost connection to server");
            System.exit(0);
        }
        readBuffer.rewind();

        final byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);

        return (new String(bytes)).trim(); // trim any excess bytes off if size < MAX_HASH_SIZE
    }

    public int getPort() { return this.port; }
    public String getHost() { return this.hostAddress.getHostName(); }

    public static void main(String[] args) {
        try {
            InetAddress host = InetAddress.getLocalHost();
            int port = 8080;
            if(args.length > 0) {
                host = InetAddress.getByName(args[0]);
                port = Integer.parseInt(args[1]);
            }
            new Thread(new Client(host, port, 2)).start();

        } catch (IOException e) { e.printStackTrace(); }
    }
}
