package cs455.scaling.threadpool.testtasks;

import cs455.scaling.threadpool.Task;

/**
 * Created by crt on 2/19/14.
 */
public class MultiplyTask implements Task {
    private int first;
    private int second;
    private int round;

    public MultiplyTask(int first, int second, int round){
        this.first = first;
        this.second = second;
        this.round = round;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void execute() {
        System.out.println("round:"+round);
        int total = this.first * this.second;
    }
}
