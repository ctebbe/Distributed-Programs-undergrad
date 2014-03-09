package cs455.scaling.server;

import cs455.scaling.threadpool.AcceptTask;
import cs455.scaling.threadpool.ReadTask;
import cs455.scaling.threadpool.ThreadPool;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by crt on 2/18/14.
 */
public class Server implements Runnable {

    private InetAddress hostAddress;
    private int port;

    private ServerSocketChannel serverChannel;  // channel to accept connections
    private Selector selector;                  // selector to monitor
    private ThreadPool threadPool = null;
    private List<ClientInfo> clientList;

    public Server(InetAddress host, int port) throws IOException{
        this.hostAddress = host;
        this.port = port;
        this.selector = this.initializeSelector();
        this.threadPool = new ThreadPool(3).initialize();
        this.clientList = new ArrayList<ClientInfo>();
    }

    // opens our selector on the specified host/port
    private Selector initializeSelector() throws IOException {
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // create non-blocking channel and bind it to our host and port
        this.serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(getHost(), getPort()));

        // invite connections
        SelectionKey selectionKey = serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);
        System.out.println("Server information:" + selectionKey.channel().toString());
        return socketSelector;
    }

    @Override
    public void run() { // accept connections and send incoming reads to our threadpool to handle
        while(true) {
            try {
                this.selector.select(); // get an event from a registered channel
                //for(SelectionKey key : this.selector.selectedKeys()) {
                Iterator selectedKeys = this.selector.selectedKeys().iterator();
                while(selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if(!key.isValid()) continue;

                    //System.out.println("handling key from:"+key.channel().toString());

                    //this.selector.selectedKeys().remove(key);

                    // handle the event for this key
                    if(key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel channel  = serverSocketChannel.accept();

                        if(channel != null) {
                            ClientInfo clientInfo = new ClientInfo(channel);
                            if(clientList.contains(clientInfo)) {
                                System.out.println("Already accepting...");
                            } else {
                                clientList.add(clientInfo);
                                new AcceptTask(channel, this.selector).execute();
                                //threadPool.addTaskToExecute(new AcceptTask(channel, this.selector));
                            }
                        }

                    } else if(key.isReadable()) {
                        //System.out.println("readable key");
                        threadPool.addTaskToExecute(new ReadTask((SocketChannel) key.channel(), this.selector));
                    }
                }
            }
            catch (IOException e)           { e.printStackTrace(); }
            catch (InterruptedException e)  { e.printStackTrace(); }
        }
    }

    private String getHost()    { return this.hostAddress.getHostName(); }
    private int getPort()       { return this.port; }

    public static void main(String[] args) {
        try {

            InetAddress host = InetAddress.getLocalHost();
            int port = 8080;
            if(args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            new Thread(new Server(host, port)).start();

        } catch (IOException e) { e.printStackTrace(); }
    }
}
