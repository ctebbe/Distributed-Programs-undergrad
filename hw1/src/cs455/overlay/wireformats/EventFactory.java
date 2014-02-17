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

    // MESSAGE
    public static Event buildMessageEvent(NodeConnection connection, int rand, String[] msgPath) throws IOException {
        return new Message(new Header(Protocol.REGISTER_RESPONSE, connection), rand, msgPath);
    }
    // REGISTER
    public static Event buildRegisterEvent(NodeConnection connection, int port) throws IOException {
        return new Register(Protocol.REGISTER, connection, port);
    }
    // REGISTER_RESP
    public static Event buildRegisterResponseEvent(NodeConnection connection, byte status, String info) throws IOException {
        return new RegisterResponse(new Header(Protocol.REGISTER_RESPONSE, connection), status, info);
    }
    // TASK_INIT
    public static Event buildTaskInitiateEvent(NodeConnection connection) throws IOException {
        return new TaskInitiate(new Header(Protocol.TASK_INITIATE, connection));
    }
    // NODE_LIST
    public static Event buildNodeListEvent(NodeConnection connection, int num, String[] nodes) throws IOException {
        return new NodeList(new Header(Protocol.MESSAGING_NODES_LIST, connection), num, nodes);
    }
    // LINK_WEIGHTS
    public static Event buildLinkWeightsEvent(NodeConnection connection, int num, String[] nodes) throws IOException {
        return new LinkWeights(new Header(Protocol.LINK_WEIGHTS, connection), num, nodes);
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
                case Protocol.MESSAGING_NODES_LIST:
                    return new NodeList(marshalledBytes);
                case Protocol.LINK_WEIGHTS:
                    return new LinkWeights(marshalledBytes);
                case Protocol.MESSAGE:
                    return new Message(marshalledBytes);
                case Protocol.TASK_INITIATE:
                    return new TaskInitiate(marshalledBytes);
                default: return null;
            }
        } catch(IOException ioe) { System.out.println(ioe.toString()); }
        return null;
    }
}
