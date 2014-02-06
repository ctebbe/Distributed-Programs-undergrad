package cs455.overlay.wireformats;
public class Protocol {

    // message types
    public static final int NOTYPE                  = -1;
    public static final int REGISTER                = 100;
    public static final int REGISTER_RESPONSE       = 101;
    public static final int DEREGISTER              = 102;
    public static final int MESSAGING_NODES_LIST    = 103;
    public static final int LINK_WEIGHTS            = 104;
    public static final int TASK_INITIATE           = 105;
    public static final int TASK_COMPLETE           = 106;
    public static final int PULL_TRAFFIC_SUMMARY    = 107;
    public static final int TRAFFIC_SUMMARY         = 108;

    // status codes
    public static final byte NOSTATUS               = (byte) 0x00;
    public static final byte SUCCESS                = (byte) 0x01;
    public static final byte FAILURE                = (byte) 0x02;

    public static String getProtocolString(int protocol) {
        switch(protocol) {
            case NOTYPE: return "NOTYPE";
            case REGISTER: return "REGISTER";
            case REGISTER_RESPONSE: return "REGISTER_RESPONSE";
            case DEREGISTER: return "DEREGISTER";
            case MESSAGING_NODES_LIST: return "";
            case LINK_WEIGHTS: return "LINK_WEIGHTS";
            case TASK_INITIATE: return "TASK_INITIATE";
            case TASK_COMPLETE: return "TASK_COMPLETE";
            case PULL_TRAFFIC_SUMMARY: return "PULL_TRAFFIC_SUMMARY";
            case TRAFFIC_SUMMARY: return "TRAFFIC_SUMMARY";
            default: return "UNKNOWN";
        }
    }
}
