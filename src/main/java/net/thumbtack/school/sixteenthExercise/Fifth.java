package net.thumbtack.school.sixteenthExercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//5. То же самое, но оба потока на базе одного и того же класса, использовать synchronized методы.
// В конструктор класса потока передается параметр типа enum, указывающий, что должен делать этот поток.
public class Fifth {
    private enum EnumAddOrRemove {
        REMOVE("Remove"),
        ADD("Add");
        private final String string;

        EnumAddOrRemove(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }

    private static class ListAdderOrRemover extends Thread {
        public final List<Integer> list;
        public final String string;

        public ListAdderOrRemover(List<Integer> list, EnumAddOrRemove enumAddOrRemove) {
            this.list = list;
            string = enumAddOrRemove.getString();
        }

        @Override
        public void run() {
            Random random = new Random();
            if(string.equals(EnumAddOrRemove.ADD.getString())){
                for(int i = 0; i < 10000; i++) {
                    addRandomNumber(random);
                }
            } else {
                for(int i = 0; i < 10000; i++) {
                    removeRandomNumber(random);
                }
            }

        }

        public synchronized void addRandomNumber(Random random){
            list.add(random.nextInt());
        }

        public synchronized void removeRandomNumber(Random random){
            if(!list.isEmpty()) {
                list.remove(random.nextInt(list.size()));
            }
        }
    }

    public static void fifth() {
        List<Integer> list = new ArrayList<>();
        ListAdderOrRemover adder = new ListAdderOrRemover(list, EnumAddOrRemove.ADD);
        ListAdderOrRemover remover = new ListAdderOrRemover(list, EnumAddOrRemove.REMOVE);
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
