package cs455.overlay.wireformats;
import cs455.overlay.transport.*;
import java.util.*;
import java.io.*;
import java.net.*;
public class EventFactory {

    protected EventFactory() {}

    private static EventFactory factory = null;
    public static EventFactory getInstance() {
        if(factory == null) factory = new EventFactory();
        return factory;
    }

    public static Event buildRegisterEvent(NodeConnection connection) throws IOException {
        return new Register(new Header(Protocol.REGISTER, connection.getSocket()));
    }
    public static Event buildRegisterResponseEvent(NodeConnection connection, byte status, String info) throws IOException {
        return new RegisterResponse(new Header(Protocol.REGISTER_RESPONSE, connection.getSocket()), 
            status, info);
    }

    public static Event buildEvent(byte[] marshalledBytes) throws IOException {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(marshalledBytes);
            DataInputStream din = new DataInputStream(new BufferedInputStream(bais));

            switch(din.readInt()) { // read protocol type byte
                case Protocol.REGISTER: 
                    return new Register(marshalledBytes);
                case Protocol.REGISTER_RESPONSE: 
                    return new RegisterResponse(marshalledBytes);
                default: return null;
            }
        } catch(IOException ioe) { System.out.println(ioe.toString()); }
        return null;
    }
}
