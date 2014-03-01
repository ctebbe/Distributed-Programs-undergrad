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

    private ArrayList<String> sentDataHashes = null;
    private ByteBuffer byteBuffer;

    private Selector selector = null;

    public Client(InetAddress host, int port, int sendRate) throws IOException {
        this.hostAddress = host;
        this.port = port;
        this.sendRate = sendRate;
        sentDataHashes = new ArrayList<String>();
        byteBuffer = ByteBuffer.allocate(Util.BYTE_BUFFER_SIZE);
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

                    System.out.println("handling key from:"+key.channel().toString());

                    if(key.isConnectable()) {
                        currentChannel.finishConnect();
                        new Thread(new ClientSenderTask(
                                currentChannel, this.sentDataHashes, this.sendRate)).start();

                        /*
                        final byte[] randomData = Util.generateRandomByteArray();
                        System.out.println("random data hash:" + Util.SHA1FromBytes(randomData));

                        ByteBuffer buffer = Util.byteBufferFromBytes(randomData);
                        */
                    }
                }
            }
        }
        catch (IOException e) { e.printStackTrace(); }
        //catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
    }

    public int getPort() { return this.port; }
    public String getHost() { return this.hostAddress.getHostName(); }

    public static void main(String[] args) {
        try {

            new Thread(new Client(InetAddress.getLocalHost(), 8080, 2)).start();

        } catch (IOException e) { e.printStackTrace(); }
    }
}
