package net.thumbtack.school.sixteenthExercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//10. Переписать упражнение 4, используя ReentrantLock
public class Tenth {
    private static class ListAdder extends Thread {
        private final List<Integer> list;
        private final Lock lock;


        public ListAdder(List<Integer> list, Lock lock) {
            this.list = list;
            this.lock = lock;
        }

        @Override
        public void run() {
            Random random = new Random();
            for(int i = 0; i < 10000; i++) {
                lock.lock();
                try {
                    list.add(random.nextInt());
                }  finally {
                    lock.unlock();
                }
            }
        }
    }

    private static class ListRemover extends Thread {
        private final List<Integer> list;
        private final Lock lock;

        public ListRemover(List<Integer> list, Lock lock) {
            this.list = list;
            this.lock = lock;
        }

        @Override
        public void run() {
            Random random = new Random();
            for(int i = 0; i < 10000; i++) {
                lock.lock();
                try {
                    if(!list.isEmpty()) {
                        list.remove(random.nextInt(list.size()));
                    }
                }  finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void tenth() {
        List<Integer> list = new ArrayList<>();
        Lock lock = new ReentrantLock();
        ListAdder adder = new ListAdder(list, lock);
        ListRemover remover = new ListRemover(list, lock);
        adder.start();
        remover.start();
        try {
            adder.join();
            remover.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
