package cs455.scaling.threadpool;

/**
 * Created by crt on 2/18/14.
 */
public class AddTask extends Task {

    private int first;
    private int second;

    public AddTask(int first, int second){
        this.first = first;
        this.second = second;
    }

    @Override
    public String getType() {
        return "add task";
    }

    @Override
    public void run() {
        System.out.println(getType());
        int total = this.first + this.second;
        System.out.println(total);
    }
}
