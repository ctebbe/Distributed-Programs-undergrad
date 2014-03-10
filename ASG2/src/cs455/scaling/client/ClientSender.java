package cs455.scaling.client;

import cs455.scaling.util.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

/**
 * this class is responsible for generating random byte arrays and sending them to the server
 * at a rate specified by 1000/sendRate and adding the hash of the bytes to sendDataHashes
 */
public class ClientSender implements Runnable {

    private SocketChannel serverChannel = null;
    private ArrayList<String> sentDataHashes = null;
    private int sendRate;

    private static final Random random = new Random(); // used to generate random bytes to send to serverChannel

    public ClientSender(SocketChannel channel, ArrayList<String> sentDataHashes, int sendRate) {
        this.serverChannel = channel;
        this.sentDataHashes = sentDataHashes;
        this.sendRate = sendRate;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
        //for(int i=0; i < 100; i++) {
            try {
                Thread.sleep(1000 / this.sendRate); // ensure we are sending at given rate

                ByteBuffer buffer = generateRandomByteBuffer();
                String bufferHash = Util.SHA1FromByteBuffer(buffer);
                System.out.println(bufferHash);

                synchronized (this.sentDataHashes) {
                    this.sentDataHashes.add(bufferHash);
                }

                /*
                int bytesWritten = 0;
                int numWrites = 0;
                while(buffer.hasRemaining()) {
                    //System.out.println("numWrites:"+numWrites);
                    bytesWritten += serverChannel.write(buffer);
                }
                //System.out.println("bytes written:"+bytesWritten);
                */
                serverChannel.write(buffer);
            }
            catch (IOException e)               { break; }
            catch (NoSuchAlgorithmException e)  { e.printStackTrace(); }
            catch (InterruptedException e)      { e.printStackTrace(); }
        }
    }

    private static byte[] generateRandomByteArray() {
        byte[] randomBytes = new byte[Util.BYTE_BUFFER_SIZE];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

    private static ByteBuffer generateRandomByteBuffer() {
        final byte[] randomBytes = generateRandomByteArray();
        final ByteBuffer randomBuffer = ByteBuffer.wrap(new byte[Util.BYTE_BUFFER_SIZE]);

        randomBuffer.put(randomBytes);
        randomBuffer.flip();
        return randomBuffer;
    }
}
