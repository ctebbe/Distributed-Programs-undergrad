package cs455.overlay.Registry;
import java.net.*;
import java.io.*;

public class Registry implements Runnable {
    private ServerSocket serverSocket = null;
    private Thread connectionListener = null;
    private int port;
    private boolean preparedToStop = true;

    public Registry(int port) {
        this.port = port;
    }

    public void startListening() throws IOException {
        if(!isListening()) {
            if(serverSocket == null) {
                serverSocket = new ServerSocket(getPort(), 5); // num clients allowed in conn queue
            }
            connectionListener = new Thread(this);
            connectionListener.start();
        }
    }

    public boolean isListening() {
        //return (serverSocket != null && connectionListener != null && connectionListener.isAlive());
        return (connectionListener != null && connectionListener.isAlive());
    }

    public int getPort { return this.port; }
}
