package cs455.scaling.util;

import java.io.BufferedInputStream;
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

    public static final int BYTE_BUFFER_SIZE    = 8192; // 8 kb of space
    public static final int MAX_HASH_SIZE       = 40;   // max SHA1 byte size

    public static ByteBuffer byteBufferFromBytes(byte[] bytes) {
        final ByteBuffer ret = ByteBuffer.wrap(new byte[bytes.length]);
        ret.put(bytes);
        ret.flip();
        return ret;
    }

    public static ByteBuffer ByteBufferFromString(String str) {
        return ByteBuffer.wrap(str.getBytes());
    }

    public static String SHA1FromByteBuffer(ByteBuffer bb) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[BYTE_BUFFER_SIZE];
        bb.rewind();
        bb.get(bytes);
        bb.rewind();
        return SHA1FromBytes(bytes);
    }


    public static String stripConnectionInformation(SelectableChannel channel) {
        String rawString = channel.toString();
        return rawString.substring(rawString.indexOf("[") + 1, rawString.indexOf("]"));
    }


    public static String SHA1FromBytes(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        byte[] hash = digest.digest(bytes);
        BigInteger hashInt = new BigInteger(1, hash);
        String hashString = hashInt.toString(16);
        //System.out.println("num hash bytes:"+hashString.getBytes().length);
        return hashString;
    }
}
