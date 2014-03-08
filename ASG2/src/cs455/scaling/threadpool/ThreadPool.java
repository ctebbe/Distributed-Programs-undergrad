package cs455.scaling.threadpool;

import cs455.scaling.threadpool.testtasks.AddTask;
import cs455.scaling.threadpool.testtasks.MultiplyTask;
import cs455.scaling.threadpool.testtasks.SleepTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by crt on 2/18/14.
 */
public class ThreadPool {

    private BlockingQueue<Task> taskQueue   = new LinkedBlockingQueue<Task>(); // holds tasks to be completed
    private List<PoolThread> threadPool     = new ArrayList<PoolThread>(); // holds all our working threads

    public ThreadPool(int numThreads) {
        for(int i=0; i < numThreads; i++) // init all the objects in our pool
            threadPool.add(new PoolThread(taskQueue));
    }

    public ThreadPool initialize() {
        for(PoolThread thread : threadPool) // start up our thread pool
            (new Thread(thread)).start();
        return this;
    }

    // queues a task to the tail to be executed
    public void addTaskToExecute(Task task) throws InterruptedException {
        System.out.println("adding to thread pool...");
        this.taskQueue.put(task); // add a task to end of the queue

        this.taskQueue.take().execute();
    }

    /* *** MAIN *** */
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(10).initialize();
        try {
            for(int i=0; i < 1000000; i++) {
                pool.addTaskToExecute(new AddTask(0, 1, i));
                pool.addTaskToExecute(new MultiplyTask(0, 1, i));
                pool.addTaskToExecute(new SleepTask(i));
                pool.addTaskToExecute(new MultiplyTask(0, 1, i));
                pool.addTaskToExecute(new SleepTask(i));
                pool.addTaskToExecute(new MultiplyTask(0, 1,i));
                pool.addTaskToExecute(new SleepTask(i));
                pool.addTaskToExecute(new AddTask(0, 2,i));
                pool.addTaskToExecute(new MultiplyTask(0, 1,i));
                pool.addTaskToExecute(new SleepTask(i));
            }

        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}
