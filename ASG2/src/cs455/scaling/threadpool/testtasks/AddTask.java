package cs455.scaling.threadpool.testtasks;

import cs455.scaling.threadpool.Task;

/**
 * Created by crt on 2/18/14.
 */
public class AddTask implements Task {

    private int first;
    private int second;
    private int round;

    public AddTask(int first, int second, int round){
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
        int total = this.first + this.second;
        System.out.println(total);
    }
}
