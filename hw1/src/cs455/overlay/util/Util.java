package cs455.overlay.util;
import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;
import java.net.*;
import java.io.*;
import java.util.*;
public class Util {
    // generates a uniq identifier based on the socket
    public static String generateKeyFromSocket(Socket sock) throws IOException {
        return sock.getLocalAddress().toString() + ":" + sock.getPort();
    }
}
