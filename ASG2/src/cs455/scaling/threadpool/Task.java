package cs455.scaling.threadpool;

/**
 * Created by crt on 2/18/14.
 */
public interface Task {

    public static final int READ = 200;
    public static final int WRITE = 300;
    public static final int ACCEPT = 400;

    public void execute();      // a method to allow each task to execute their work
    public int getType();       // an identifier to tell what each task is meant to do
}
