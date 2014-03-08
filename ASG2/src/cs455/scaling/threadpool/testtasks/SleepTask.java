package cs455.scaling.threadpool.testtasks;

import cs455.scaling.threadpool.Task;

/**
 * Created by crt on 2/19/14.
 */
public class SleepTask implements Task {

    private int round;

    public SleepTask(int round){
        this.round = round;
    }

    @Override
    public int getType() {
        return this.round;
    }

    @Override
    public void execute() {
        System.out.println("round:"+round);
        System.out.println(getType());
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
