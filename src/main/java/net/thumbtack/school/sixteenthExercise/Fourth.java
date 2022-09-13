package net.thumbtack.school.sixteenthExercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//4. В основном потоке создать ArrayList<Integer>.
// Запустить 2 потока на базе разных классов, один поток 10000 раз добавляет в список случайное целое число,
// другой 10000 раз удаляет элемент по случайному индексу (если при удалении выясняется, что список пуст,
// ничего не делать). Использовать внешний synchronized блок.
// Потоки должны работать конкурентно, то есть одновременно должно идти и добавление, и удаление.
public class Fourth {
    private static class ListAdder extends Thread {
        public final List<Integer> list;

        public ListAdder(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            Random random = new Random();
            for(int i = 0; i < 10000; i++) {
                synchronized (list) {
                    list.add(random.nextInt());
                }
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
                synchronized (list){
                    if(!list.isEmpty()) {
                        list.remove(random.nextInt(list.size()));
                    }
                }
            }
        }
    }

    public static void fourth() {
        List<Integer> list = new ArrayList<>();
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
