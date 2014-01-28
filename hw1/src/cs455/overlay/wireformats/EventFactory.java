public class EventFactory {

    protected EventFactory() {}

    private static EventFactory factory = null;
    public static EventFactory getInstance() {
        if(factory == null) factory = new EventFactory();
        return factory;
    }

    public static Event buildEvent(int protocol, String ip, int port) {
        //if(protocol == Protocol.REGISTER) return new Register();
        return null;
    }
}
