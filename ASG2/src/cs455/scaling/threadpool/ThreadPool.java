package cs455.scaling.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by crt on 2/18/14.
 */
public class ThreadPool {

    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<Task>(); // holds tasks to be completed
    private List<PoolThread> threadPool = new ArrayList<PoolThread>(); // holds all our working threads

    public ThreadPool(int numThreads) {
        for(int i=0; i < numThreads; i++) // init all the objects in our pool
            threadPool.add(new PoolThread(taskQueue));
        for(PoolThread thread : threadPool) // start up our thread pool
            thread.start();
    }

    // queues a task to the tail to be executed
    public synchronized void addTaskToExecute(Task task) throws InterruptedException {
        this.taskQueue.put(task); // add a task to end of the queue
    }

    // spins down all threads in the current thread pool
    public synchronized void killPool() {
        for(PoolThread thread : threadPool)
            thread.killThread();
    }

    /* *** MAIN *** */
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(3);
        try {
            pool.addTaskToExecute(new AddTask(0, 1));
            pool.addTaskToExecute(new AddTask(0, 2));
            pool.addTaskToExecute(new AddTask(0, 3));
            pool.addTaskToExecute(new AddTask(0, 4));
            pool.addTaskToExecute(new AddTask(0, 5));
            pool.addTaskToExecute(new AddTask(0, 6));
            pool.addTaskToExecute(new AddTask(0, 7));

        } catch (InterruptedException e) { e.printStackTrace(); }
        pool.killPool();
    }
}
