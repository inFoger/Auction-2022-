package net.thumbtack.school.sixteenthExercise;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//7. “Ping Pong”, задача заключается в том чтобы бесконечно выводить на консоль сообщения
// “ping” или “pong” из двух разных потоков. При этом сообщения обязаны чередоваться,
// т.е. не может быть ситуации когда ping или pong появляется в консоли более одного раза подряд.
// Первым должно быть сообщение “ping”.
public class Seventh {
    private static class Ping extends Thread{
        private final Semaphore semPing;
        private final Semaphore semPong;

        public Ping(Semaphore semPing, Semaphore semPong) {
            this.semPing = semPing;
            this.semPong = semPong;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    semPing.acquire();
                    System.out.println("ping");
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semPong.release();
                }
            }
        }
    }
    private static class Pong extends Thread{
        private final Semaphore semPing;
        private final Semaphore semPong;

        public Pong(Semaphore semPing, Semaphore semPong) {
            this.semPing = semPing;
            this.semPong = semPong;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    semPong.acquire();
                    System.out.println("pong");
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semPing.release();
                }
            }
        }
    }

    public static void seventh() {
        Semaphore semPing = new Semaphore(1);
        Semaphore semPong = new Semaphore(0);
        Ping pingThread = new Ping(semPing, semPong);
        Pong pongThread = new Pong(semPing, semPong);
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
