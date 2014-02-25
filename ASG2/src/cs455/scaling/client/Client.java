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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ctebbe on 2/20/14.
 */
public class Client implements Runnable {

    private InetAddress hostAddress = null;
    private int port = Integer.MIN_VALUE;

    private ArrayList<String> sentDataHashes = null;
    private ByteBuffer byteBuffer;

    private Selector selector = null;

    public Client(InetAddress host, int port) throws IOException {
        this.hostAddress = host;
        this.port = port;
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
        channel.register(selector, SelectionKey.OP_CONNECT);

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
                        if(currentChannel.isConnectionPending()) currentChannel.finishConnect();
                        //new Thread(new ClientSenderTask(currentChannel, sentDataHashes)).start();
                        ByteBuffer buffer = ByteBuffer.allocate(Util.BYTE_BUFFER_SIZE);
                        final byte[] randomData = Util.generateRandomByteArray();
                        System.out.println("random data hash:"+Util.SHA1FromBytes(randomData));
                        buffer.put(randomData);
                        currentChannel.write(buffer);
                    }

                }
            }

        } catch (IOException e) { e.printStackTrace(); } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        /*
        while(true) {
            try {

                this.selector.select();
                //for(SelectionKey key : )

            } catch (IOException e) { e.printStackTrace(); }
        }
        */
    }

    public int getPort() { return this.port; }
    public String getHost() { return this.hostAddress.getHostName(); }

    public static void main(String[] args) {
        try {

            new Thread(new Client(InetAddress.getLocalHost(), 8080)).start();

        } catch (IOException e) { e.printStackTrace(); }
    }
}
