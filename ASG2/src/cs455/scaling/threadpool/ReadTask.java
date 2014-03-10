package cs455.scaling.threadpool;

import cs455.scaling.util.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ctebbe on 3/4/14.
 */
public class ReadTask implements Task {

    private SocketChannel socketChannel = null;
    private Selector selector = null;

    public ReadTask(SocketChannel socketChannel, Selector selector) {
        this.socketChannel = socketChannel;
        this.selector = selector;
    }

    @Override
    public void execute() {
        try {
            Thread.sleep(2); // wait for data to be written to the channel

            // read
            String hash = readHashFromSocketChannel(this.socketChannel);

            if(hash == null) return;

            System.out.println(hash);

            // write
            ByteBuffer sendBuffer = Util.ByteBufferFromString(hash);
            socketChannel.write(sendBuffer);
        }
        catch (IOException e) { System.out.println("Socket channel to client closed."); }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public String readHashFromSocketChannel(SocketChannel socketChannel)
            throws IOException, NoSuchAlgorithmException {

        ByteBuffer readBuffer = ByteBuffer.allocate(Util.BYTE_BUFFER_SIZE);
        readBuffer.clear();
        byte[] readBytes = new byte[Util.BYTE_BUFFER_SIZE];

        int readBytesSize = socketChannel.read(readBuffer);
        //System.out.println("bytes read:"+readBytesSize);
        if (readBytesSize == -1) { // handle client closed
            socketChannel.close();
            //key.cancel();
            return "Connection ended";
        } else if(readBytesSize == 0) return null;

        readBuffer.rewind();
        readBuffer.get(readBytes);

        return Util.SHA1FromBytes(readBytes);
    }

    @Override
    public int getType() { return Task.READ; }
}
