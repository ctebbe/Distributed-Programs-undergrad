package cs455.scaling.threadpool;

import java.util.concurrent.BlockingQueue;

/**
 * Created by crt on 2/18/14.
 */
public class PoolThread extends Thread {

    private BlockingQueue<Task> taskQueue = null;
    private boolean isDone = false;


    public PoolThread(BlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run() {
        try {

            while(!isDone()) taskQueue.take().run(); // take() waits until a task becomes available

        } catch (InterruptedException ie) { ie.printStackTrace(); }
    }

    public synchronized void killThread() {
        this.isDone = true;
        this.interrupt(); // break out of dequeue
    }

    private synchronized boolean isDone() { return this.isDone; }
}
