package net.thumbtack.school.sixteenthExercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//6. Можно ли корректно решить задачу 4, используя Collections.synchronizedList и
// не используя synchronized явно ? Если да - напишите программу.
public class Sixth {
    private static class ListAdder extends Thread {
        public final List<Integer> list;

        public ListAdder(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            Random random = new Random();
            for(int i = 0; i < 10000; i++) {
                list.add(random.nextInt());
            }
        }
    }

    private static class ListRemover extends Thread {
        public final List<Integer> list;

        public ListRemover(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            Random random = new Random();
            for(int i = 0; i < 10000; i++) {
                if(!list.isEmpty()) {
                    list.remove(random.nextInt(list.size()));
                }
            }
        }
    }

    public static void sixth() {
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        ListAdder adder = new ListAdder(list);
        ListRemover remover = new ListRemover(list);
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
