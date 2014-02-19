package cs455.overlay.node;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
public interface Node {
    public void onEvent(Event event);
    public void registerConnection(NodeConnection connection);
    public void deregisterConnection(NodeConnection connection);
}
