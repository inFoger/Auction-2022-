package net.thumbtack.school.sixteenthExercise;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//11. “Ping Pong” на базе ReentrantLock и Conditional переменной.
public class Eleventh {
    private static final Lock lock = new ReentrantLock();
    private static final Condition pingCond = lock.newCondition();
    private static final Condition pongCond = lock.newCondition();
    private static Boolean wasPing = false;

    private static class Ping extends Thread{
        public Ping() {
            super("PingThread");

        }
        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (wasPing) {
                        pongCond.await();
                    }
                    System.out.println("ping");
                    wasPing = true;
                    pingCond.signal();
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
    private static class Pong extends Thread{
        public Pong() {
            super("PongThread");
        }

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (!wasPing) {
                        pingCond.await();
                    }
                    System.out.println("pong");
                    wasPing = false;
                    pongCond.signal();
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void eleventh() {
        Ping pingThread = new Ping();
        Pong pongThread = new Pong();
        pingThread.start();
        pongThread.start();
        try {
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
