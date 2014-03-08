package cs455.scaling.threadpool;

/**
 * Created by ctebbe on 3/5/14.
 */
public class WriteTask implements Task {
    @Override
    public void execute() {

    }

    @Override
    public int getType() { return Task.WRITE; }
}
