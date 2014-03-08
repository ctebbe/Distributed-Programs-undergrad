package cs455.scaling.server;

import java.net.InetAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by ctebbe on 3/7/14.
 */
public class ClientInfo {

    public InetAddress hostAddress = null;
    private int port = Integer.MIN_VALUE;

    public ClientInfo(ServerSocketChannel channel) {
        this.hostAddress = channel.socket().getInetAddress();
        System.out.println(hostAddress.toString());
    }

    public boolean equals(Object other) {
        if(other == null) return false;
        else if(other == this) return true;
        else if(!(other instanceof ClientInfo)) return false;

        ClientInfo ci = (ClientInfo) other;
        return this.hostAddress.equals(ci.hostAddress);
    }
}
