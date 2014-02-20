package cs455.scaling.threadpool;

/**
 * Created by crt on 2/19/14.
 */
public class SleepTask extends Task {

    private int first;
    private int second;

    public SleepTask(){
    }

    @Override
    public String getType() {
        return "sleep task";
    }

    @Override
    public void run() {
        System.out.println(getType());
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
