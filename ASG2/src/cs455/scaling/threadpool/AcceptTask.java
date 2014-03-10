package cs455.scaling.threadpool;

import cs455.scaling.server.ClientInfo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by ctebbe on 3/4/14.
 */
public class AcceptTask implements Task {

    private Selector selector           = null; // the selector which to register
    private SocketChannel socketChannel   = null; // key associated with client interested in connecting

    public AcceptTask(SocketChannel channel, Selector selector) {
        this.socketChannel = channel;
        this.selector = selector;
    }

    @Override
    public void execute() {
        try {

            if(socketChannel != null) {
                socketChannel.configureBlocking(false);
                socketChannel.register(this.selector, SelectionKey.OP_READ); //, new ClientInfo(serverSocketChannel));
                System.out.println("Accepted client:"+socketChannel.toString());
            }
        }
        catch (IOException e) { e.printStackTrace(); }
        //catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public int getType() { return Task.ACCEPT; }
}
