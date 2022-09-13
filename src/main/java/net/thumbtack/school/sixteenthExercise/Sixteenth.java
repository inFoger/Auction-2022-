package net.thumbtack.school.sixteenthExercise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//16. Реализовать очередь задач. Задача - экземпляр класса Task или его наследника,
// имплементирующего Executable - свой интерфейс с единственным методом void execute().
// Потоки - разработчики ставят в очередь экземпляры Task аналогично (15),
// потоки - исполнители берут их из очереди и исполняют.
// Количество тех и других потоков может быть любым и определяется в main.
public class Sixteenth {
    private interface Executable {
        void execute();
    }

    private static class Task implements Executable {
        private final String string;

        public Task(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }

        @Override
        public void execute() {
            System.out.println(string);
        }
    }

    private static class Executor extends Thread {
        private final BlockingQueue<Task> tasks;

        public Executor(BlockingQueue<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public void run() {
            Task task;
            try {
                while (true) {
                    System.out.println("__Executor Start");
                    task = tasks.take();
                    if (task.getString().equals("null")) {
                        break;
                    }
                    task.execute();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("__Executor End!");
        }
    }

    private static class Developer extends Thread {
        private final BlockingQueue<Task> tasks;
        private final int count;

        private Developer(BlockingQueue<Task> tasks, int count) {
            this.tasks = tasks;
            this.count = count;
        }

        @Override
        public void run() {
            try {
                System.out.println("Developer Start");
                for (int i = 0; i <= count; i++) {
                    Thread.sleep(100);
                    tasks.put(new Task("__Task Done"));
                    System.out.println("Task was added");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("Developer End");
        }
    }

    public static void main(String[] args) {
        int countTasks = 20;
        int queueSize = 5;
        BlockingQueue<Task> tasksQueue = new ArrayBlockingQueue<>(queueSize);
        int numberOfDevelopers = 3;
        int numberOfExecutors = 3;
        List<Developer> developers = new ArrayList<>();
        List<Executor> executors = new ArrayList<>();

        for (int i = 0; i < numberOfDevelopers; i++) {
            developers.add(new Developer(tasksQueue, countTasks));
            developers.get(i).start();
        }
        for (int i = 0; i < numberOfExecutors; i++) {
            executors.add(new Executor(tasksQueue));
            executors.get(i).start();
        }

        try {
            for (Thread thread : developers) {
                thread.join();
            }
            for(int i = 0; i < numberOfExecutors; i++) {
                tasksQueue.put(new Task("null"));
                Thread.sleep(100 * countTasks/2);
            }
            for (Thread thread : executors) {
                thread.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("--TasksQueue's final state:" + tasksQueue);
    }
}
