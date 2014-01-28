//package cs455.overlay.wireformats;
public class Protocol {

    // message types
    public static final int NOTYPE                  = -1;
    public static final int REGISTER                = 0;
    public static final int REGISTER_RESPONSE       = 1;
    public static final int DEREGISTER              = 2;
    public static final int MESSAGING_NODES_LIST    = 3;
    public static final int LINK_WEIGHTS            = 4;
    public static final int TASK_INITIATE           = 5;
    public static final int TASK_COMPLETE           = 6;
    public static final int PULL_TRAFFIC_SUMMARY    = 7;
    public static final int TRAFFIC_SUMMARY         = 8;

    // status codes
    public static final byte NOSTATUS               = (byte) 0x00;
    public static final byte SUCCESS                = (byte) 0x01;
    public static final byte FAILURE                = (byte) 0x02;
}
