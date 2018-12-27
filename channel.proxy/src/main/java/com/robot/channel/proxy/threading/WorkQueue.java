package com.robot.channel.proxy.threading;

import org.springframework.stereotype.Component;

import java.util.LinkedList;

@SuppressWarnings("unchecked")
@Component
public class WorkQueue {
    private final PoolWorker[] threads;
    @SuppressWarnings("rawtypes")
    private final LinkedList queue;
    private int nThreads = 10;

    @SuppressWarnings("rawtypes")
    public WorkQueue() {
        queue = new LinkedList();
        threads = new PoolWorker[nThreads];
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public void execute(Runnable r) {
        boolean emergency = false;
        synchronized (queue) {
            if (emergency) {
                queue.addFirst(r);
            } else {
                queue.addLast(r);
            }
            queue.notify();
        }
    }

    private class PoolWorker extends Thread {
        public void run() {
            Runnable r;
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    r = (Runnable) queue.removeFirst();
                }
                try {
                    r.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
