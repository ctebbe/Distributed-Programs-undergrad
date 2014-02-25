package cs455.scaling.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by ctebbe on 2/21/14.
 */
public class Util {

    public static final int BYTE_BUFFER_SIZE = 8192; // 8 kb of space
    public static final Random random = new Random();

    public static byte[] generateRandomByteArray() {
        byte[] randomBytes = new byte[BYTE_BUFFER_SIZE];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

    public static String SHA1FromBytes(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        byte[] hash = digest.digest(data);
        BigInteger hashInt = new BigInteger(1, hash);
        return hashInt.toString(16);
    }
}
