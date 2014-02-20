package cs455.scaling.threadpool;

/**
 * Created by crt on 2/19/14.
 */
public class MultiplyTask extends Task {
    private int first;
    private int second;

    public MultiplyTask(int first, int second){
        this.first = first;
        this.second = second;
    }

    @Override
    public String getType() {
        return "multiply task";
    }

    @Override
    public void run() {
        int total = this.first * this.second;
        System.out.println(getType()+":"+total);
    }
}
