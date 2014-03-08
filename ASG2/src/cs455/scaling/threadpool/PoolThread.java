package cs455.scaling.threadpool;

import java.util.concurrent.BlockingQueue;

/**
 * Created by crt on 2/18/14.
 */
public class PoolThread implements Runnable {

    private BlockingQueue<Task> taskQueue = null;   // an instance of our pool-queue to pull tasks from

    public PoolThread(BlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run() {
        //while(!Thread.currentThread().isInterrupted()) {
                /*
            try {

                Task task = taskQueue.take();
                task.execute();

            } catch (InterruptedException ie) { 
                Thread.currentThread().interrupt();
                return;
            }
                */
        //}
    }
}
