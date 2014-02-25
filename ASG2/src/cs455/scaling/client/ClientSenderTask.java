package cs455.scaling.client;

import cs455.scaling.util.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by crt on 2/24/14.
 */
public class ClientSenderTask implements Runnable {

    private SocketChannel channel = null;
    private ArrayList<String> sentDataHashes = null;

    public ClientSenderTask(SocketChannel channel, ArrayList<String> sentDataHashes) {
        this.channel = channel;
        this.sentDataHashes = sentDataHashes;
    }

    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(Util.BYTE_BUFFER_SIZE);
            final byte[] randomData = Util.generateRandomByteArray();
            System.out.println("random data hash:"+Util.SHA1FromBytes(randomData));
            buffer.put(randomData);
            channel.write(buffer);
        }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
