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

    private SocketChannel serverChannel = null;
    private ArrayList<String> sentDataHashes = null;
    private int sendRate;

    public ClientSenderTask(SocketChannel channel, ArrayList<String> sentDataHashes,
                            int sendRate) {
        this.serverChannel = channel;
        this.sentDataHashes = sentDataHashes;
        this.sendRate = sendRate;
    }

    @Override
    public void run() {
        //while(!Thread.interrupted()) {
        for(int i=0; i < 5; i++) {
            try {
                Thread.sleep(1000 / this.sendRate); // ensure we are sending at given rate

                ByteBuffer buffer = Util.generateRandomByteBuffer();
                System.out.println(Util.SHA1FromByteBuffer(buffer));

                synchronized (this.sentDataHashes) {
                    this.sentDataHashes.add(Util.SHA1FromByteBuffer(buffer));
                }

                serverChannel.write(buffer);
            }
            catch (IOException e) { e.printStackTrace(); }
            catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
