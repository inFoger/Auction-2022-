package net.thumbtack.school.sixteenthExercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class AtomicIntegerIncrementor implements Runnable{
    private final AtomicInteger atomicInteger;

    public AtomicIntegerIncrementor(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }

    @Override
    public void run() {
        atomicInteger.addAndGet(1);
    }
}

public class FirthSecondAndThird {
    //1. Напечатать все свойства текущего потока.
    public static String first() {
        Thread thread = Thread.currentThread();
        return thread.toString();
    }

    //2. Создать новый поток и дождаться его окончания в первичном потоке.
    public static int second() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Thread otherThread = new Thread(new AtomicIntegerIncrementor(atomicInteger));
        otherThread.start();
        try {
            otherThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return atomicInteger.get();
    }

    //3. Создать 3 новых потока и дождаться окончания их всех в первичном потоке.
    // Для каждого потока создать свой собственный экземпляр класса.
    public static int third() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Runnable runnable = new AtomicIntegerIncrementor(atomicInteger);
        Thread one = new Thread(runnable);
        Thread two = new Thread(runnable);
        Thread three = new Thread(runnable);
        one.start();
        two.start();
        three.start();
        try {
            one.join();
            two.join();
            three.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return atomicInteger.get();
    }




}
