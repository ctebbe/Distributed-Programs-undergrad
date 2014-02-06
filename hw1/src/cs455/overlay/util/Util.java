package cs455.overlay.util;
import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;
import java.net.*;
import java.io.*;
import java.util.*;
public class Util {
    // generates a uniq identifier based on the socket
    public static String generateHashKey(Socket sock) throws IOException {
        return sock.getLocalAddress().toString() + ":" + sock.getPort();
    }
    // generates id to put in event header
    public static String generateEventKey(Socket sock) throws IOException {
        return sock.getLocalAddress().toString() + ":" + sock.getLocalPort();
    }
    public static byte[] concatByteArrays(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
