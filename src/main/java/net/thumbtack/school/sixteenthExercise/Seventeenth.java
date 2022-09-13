package net.thumbtack.school.sixteenthExercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//17. Реализовать очередь многостадийных задач. Многостадийная задача - экземпляр класса Task,
// имеющего список из стадий - List<Executable>, где Executable - интерфейс из задания 16.
// Потоки - разработчики ставят в очередь экземпляры Task аналогично (15),
// потоки - исполнители берут из очереди задачу, исполняют очередную ее стадию, после чего,
// если это не последняя стадия, ставят задачу обратно в очередь.
// Количество тех и других потоков может быть любым и определяется в main.
public class Seventeenth {
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

    private static class MultiStageTask implements Executable {
        private final List<Executable> stages;
        private final String string;
        int i = 1;

        public MultiStageTask(String string) {
            this.stages =  new ArrayList<>();
            stages.add(new Task("____Stage 1 is done"));
            stages.add(new Task("____Stage 2 is done"));
            stages.add(new Task("____Stage 3 is done"));;
            this.string = string;
        }

        public String getString() {
            return string;
        }

        public boolean isEmpty() {
            return stages.isEmpty();
        }

        @Override
        public void execute() {
            if (!stages.isEmpty()) {
                stages.remove(0);
                System.out.println("____Stage"+i+ " is done");
                i++;
            }
            if (stages.isEmpty()) {
                System.out.println("____MultiStage task is done");
            }
        }
    }

    private static class Executor extends Thread {
        private final BlockingQueue<MultiStageTask> tasks;

        public Executor(BlockingQueue<MultiStageTask> tasks) {
            this.tasks = tasks;
        }

        @Override
        public void run() {
            Random random = new Random();
            MultiStageTask task;
            try {
                System.out.println("__Executor Start");
                while (true) {
                    Thread.sleep(random.nextInt(10));
                    task = tasks.take();
                    if (task.getString().equals("null")) {
                        break;
                    }
                    task.execute();
                    if(!task.isEmpty()) {
                        tasks.put(task);
                        System.out.println("____MultiStageTask was returned");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("__Executor End!");
        }
    }

    private static class Developer extends Thread {
        private final BlockingQueue<MultiStageTask> tasks;
        private final int count;

        private Developer(BlockingQueue<MultiStageTask> tasks, int count) {
            this.tasks = tasks;
            this.count = count;
        }

        @Override
        public void run() {
            try {
                System.out.println("Developer Start");
                for (int i = 0; i <= count; i++) {
                    Thread.sleep(100);
                    tasks.put(new MultiStageTask("____MultiStageTask"));
                    System.out.println("Task was added");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("Developer End");
        }
    }

    public static void main(String[] args) {
        int countOfTasks = 3;
        int queueSize = 7;
        BlockingQueue<MultiStageTask> tasksQueue = new ArrayBlockingQueue<>(queueSize);
        int numberOfDevelopers = 3;
        int numberOfExecutors = 3;
        List<Developer> developers = new ArrayList<>();
        List<Executor> executors = new ArrayList<>();

        for (int i = 0; i < numberOfDevelopers; i++) {
            developers.add(new Developer(tasksQueue, countOfTasks));
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
                tasksQueue.put(new MultiStageTask("null"));
                Thread.sleep(100* countOfTasks);
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
