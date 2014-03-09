package cs455.scaling.server;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by ctebbe on 3/7/14.
 */
public class ClientInfo {

    public SocketChannel channel = null;
    //private int port = Integer.MIN_VALUE;

    public ClientInfo(SocketChannel channel) {
        //System.out.println(channel.toString());
        this.channel = channel;
    }

    public boolean equals(Object other) {
        if(other == null) return false;
        else if(!(other instanceof ClientInfo)) return false;

        ClientInfo ci = (ClientInfo) other;
        return this.channel.equals(ci.channel);
    }
}
