package net.thumbtack.school.sixteenthExercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//18. Та же задача, что и в (17), но разработчики либо ставят в очередь Task,
// либо запускают нового разработчика, который делает то же самое, что и первичный разработчик.
// Выбор, какое из этих двух действий сделать, производится случайным образом.
public class Eighteenth {
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
        private final int ableToCreateDevelopers;

        private Developer(BlockingQueue<MultiStageTask> tasks, int count, int ableToCreateDevelopers) {
            this.tasks = tasks;
            this.count = count;
            this.ableToCreateDevelopers = ableToCreateDevelopers;
        }

        @Override
        public void run() {
            Random random = new Random();
            try {
                System.out.println("Developer Start");
                for (int i = 0; i <= count; i++) {
                    if(random.nextBoolean() && ableToCreateDevelopers > 0 && !tasks.isEmpty()) {
                        Developer newDeveloper = new Developer(tasks, count, ableToCreateDevelopers-1);
                        System.out.println("*New developer was born");
                        newDeveloper.start();
                        Thread.sleep(100L * count);
                    } else {
                        Thread.sleep(100);
                        tasks.put(new MultiStageTask("____MultiStageTask"));
                        System.out.println("Task was added");
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("Developer End");
        }
    }

    public static void main(String[] args) {
        int maxDevelopersAmount = 3;
        int countOfTasks = 3;
        int queueSize = 7;
        BlockingQueue<MultiStageTask> tasksQueue = new ArrayBlockingQueue<>(queueSize);
        int numberOfDevelopers = 3;
        int numberOfExecutors = 6;
        List<Developer> developers = new ArrayList<>();
        List<Executor> executors = new ArrayList<>();

        for (int i = 0; i < numberOfDevelopers; i++) {
            developers.add(new Developer(tasksQueue, countOfTasks, maxDevelopersAmount));
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
