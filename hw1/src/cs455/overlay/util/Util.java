package cs455.overlay.util;
import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;
import java.net.*;
import java.io.*;
import java.util.*;
public class Util {
    // generates a uniq identifier based on the socket
    public static String generateHashKey(Socket sock) throws IOException {
        String address = sock.getRemoteSocketAddress().toString();
        return address.substring(address.indexOf("/")+1);
    }
    // generates id to put in event header
    public static String generateEventKey(Socket sock) throws IOException {
        return sock.getLocalAddress().toString().substring(1) + ":" + sock.getLocalPort(); // exclude leading /
    }
    // strips away the IP in the key format
    public static String stripIP(String key) {
        return key.substring(0, key.indexOf(":"));
    }
    public static int stripPort(String key) {
        return Integer.parseInt(key.substring(key.indexOf(":")+1));
    }

    public static int generateRandomNumber(int min, int max) {
        return (int)(Math.random() * ((max-min) + 1) + min);
    }
}
