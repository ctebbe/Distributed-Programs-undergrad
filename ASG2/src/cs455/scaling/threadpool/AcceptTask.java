package cs455.scaling.threadpool;

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
    private SelectionKey selectionKey   = null; // key associated with client interested in connecting

    public AcceptTask(SelectionKey selectionKey, Selector selector) {
        this.selectionKey = selectionKey;
        this.selector = selector;
    }

    @Override
    public void execute() {
        try {

            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) this.selectionKey.channel();
            //System.out.println("serverSocketChannel:"+serverSocketChannel.toString());

            // accept connection and turn blocking off
            SocketChannel channel  = serverSocketChannel.accept();
            if(channel != null) {
                channel.configureBlocking(false);

                System.out.println("accepted connection:"+channel.toString());

                // register the channel with our selector and wait for data
                channel.register(this.selector, SelectionKey.OP_READ);
            }
        }
        catch (IOException e) { e.printStackTrace(); }
        //catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public int getType() { return Task.ACCEPT; }
}
