package cs455.scaling.util;

import java.util.Random;

/**
 * Created by ctebbe on 2/21/14.
 */
public class Util {

    public static final int BUFFER_SIZE = 8192; // 8 kb of space
    public static final Random random = new Random();

    public static byte[] generateRandomByteArray() {
        byte[] randomBytes = new byte[BUFFER_SIZE];
        random.nextBytes(randomBytes);
        return randomBytes;
    }
}
