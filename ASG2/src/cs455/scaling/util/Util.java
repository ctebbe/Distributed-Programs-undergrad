package cs455.scaling.util;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by ctebbe on 2/21/14.
 */
public class Util {

    public static final int BYTE_BUFFER_SIZE = 8192; // 8 kb of space

    private static final Random random = new Random();
    private static byte[] generateRandomByteArray() {
        byte[] randomBytes = new byte[BYTE_BUFFER_SIZE];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

    public static ByteBuffer byteBufferFromBytes(byte[] bytes) {
        final ByteBuffer ret = ByteBuffer.wrap(new byte[bytes.length]);
        ret.put(bytes);
        ret.flip();
        return ret;
    }

    public static ByteBuffer generateRandomByteBuffer() {
        final byte[] randomBytes = generateRandomByteArray();
        random.nextBytes(randomBytes);

        final ByteBuffer randomBuffer = ByteBuffer.wrap(new byte[BYTE_BUFFER_SIZE]);

        randomBuffer.put(randomBytes);
        randomBuffer.flip();
        return randomBuffer;
    }

    public static String SHA1FromByteBuffer(ByteBuffer bb) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[BYTE_BUFFER_SIZE];
        bb.rewind();
        bb.get(bytes);
        bb.rewind();
        return SHA1FromBytes(bytes);
    }

    public static String SHA1FromSocketChannel(SocketChannel socketChannel)
            throws IOException, NoSuchAlgorithmException {
        ByteBuffer readBuffer = ByteBuffer.allocate(Util.BYTE_BUFFER_SIZE);
        byte[] readBytes = new byte[BYTE_BUFFER_SIZE];
        int readBytesSize = socketChannel.read(readBuffer);
        if (readBytesSize == -1) ; // handle client closed

        readBuffer.rewind();
        readBuffer.get(readBytes);

        return SHA1FromBytes(readBytes);
    }

    public static String stripInfo(SelectableChannel channel) {
        String rawString = channel.toString();
        return rawString.substring(rawString.indexOf("[") + 1, rawString.indexOf("]"));
    }


    public static String SHA1FromBytes(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        byte[] hash = digest.digest(bytes);
        BigInteger hashInt = new BigInteger(1, hash);
        return hashInt.toString(16);
    }
}
