import java.io.*;
public interface Event {
    public int      getType();
    public byte[]   getBytes() throws IOException;
}
