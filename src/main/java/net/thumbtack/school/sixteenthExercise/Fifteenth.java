package net.thumbtack.school.sixteenthExercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//15. Реализовать очередь данных. Данные - экземпляр класса Data с единственным методом int[] get().
// Потоки-писатели ставят в очередь экземпляры Data,
// количество экземпляров Data, которое ставит в очередь каждый писатель, определяется в его конструкторе.
// Потоки - читатели берут их из очереди и распечатывают, и в конечном итоге должны взять все экземпляры Data,
// которые записали все писатели вместе взятые.
// Количество тех и других потоков может быть любым и определяется в main.
public class Fifteenth {
    private static class Data {
        private final int[] arr;

        public Data(int[] arr){
            this.arr = arr;
        }

        public int[] get(){
            return arr;
        }

    }

    private static class DataReader extends Thread {
        private final BlockingQueue<Data> dataQueue;

        public DataReader(BlockingQueue<Data> dataQueue) {
            this.dataQueue = dataQueue;
        }

        @Override
        public void run() {
            Data data;
            try {
                while (true) {
                    System.out.println("__DataReader Start");
                    data = dataQueue.take();
                    if (data.get() == null) {
                        break;
                    }
                    int[] array = data.get();
                    System.out.println("__Data length = " + array.length + "\n__" + Arrays.toString(array));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("__DataReader End!");
        }
    }

    private static class DataWriter extends Thread {
        private final BlockingQueue<Data> dataQueue;
        private final int count;

        private DataWriter(BlockingQueue<Data> dataQueue, int count) {
            this.dataQueue = dataQueue;
            this.count = count;
        }

        @Override
        public void run() {
            Random random = new Random();
            try {
                System.out.println("DataWriter Start");
                for (int i = 0; i <= count; i++) {
                    int[] data = new int[] {random.nextInt(7), random.nextInt(7),
                            random.nextInt(7), random.nextInt(7), random.nextInt(7)};
                    dataQueue.put(new Data(data));
                    System.out.println("Data length = " + data.length + "\n" + Arrays.toString(data));
                }
                //добавляем яд
                dataQueue.put(new Data(null));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("DataWriter End");
        }
    }

    public static void main(String[] args) {
        int queueSize = 5;
        BlockingQueue<Data> dataQueue = new ArrayBlockingQueue<>(queueSize);
        int numberOfWriters = 3;
        int numberOfReaders = 3;
        List<DataWriter> writers = new ArrayList<>();
        List<DataReader> readers = new ArrayList<>();

        for (int i = 0; i < numberOfWriters; i++) {
            writers.add(new DataWriter(dataQueue, 20));
            writers.get(i).start();
        }
        for (int i = 0; i < numberOfReaders; i++) {
            readers.add(new DataReader(dataQueue));
            readers.get(i).start();
        }

        try {
            for (Thread thread : writers) {
                thread.join();
            }
            for (Thread thread : readers) {
                thread.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("--DataQueue's final state:" + dataQueue);
    }
}