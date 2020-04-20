package ru.otus.l27;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CounterDemo.
 *
 * @author Evgeniya_Yanchenko
 */
public class CounterDemo {

    public static final int START = 1;
    public static final int LIMIT = 10;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private String latest = "";


    public static void main(String[] args) {
        new CounterDemo().go();
    }


    private void go() {

        Thread thread1 = createNamedThread("thread1");
        Thread thread2 = createNamedThread("thread2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
        } catch (InterruptedException e) {
            thread1.interrupt();
            e.printStackTrace();
        }

        try {
            thread2.join();
        } catch (InterruptedException e) {
            thread2.interrupt();
            e.printStackTrace();
        }

        System.out.println("Finished");
    }


    private Thread createNamedThread(String threadName) {
        Thread thread = new Thread(() -> count(threadName));
        thread.setName(threadName);
        return thread;
    }


    private void count(String threadName) {
        for (int i = START; i < LIMIT; i++) {
            print(i, threadName);
        }
        for (int i = LIMIT; i >= START; i--) {
            print(i, threadName);
        }
    }


    private void print(int i, String threadName) {
        try {
            lock.lock();
            while (threadName.equals(latest)) {
                condition.await();
            }
            System.out.printf("%d ", i);
            sleep(threadName);
            latest = threadName;
            condition.signalAll();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    
    private void sleep(String threadName) {
        try {
            if (threadName.contains("1")) {
                Thread.sleep(500);
            } else {
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

}
