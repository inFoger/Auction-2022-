package net.thumbtack.school.sixteenthExercise;

import java.util.concurrent.Semaphore;

//8. Система читатель-писатель.
public class Eighth {
    private static class ReaderWriter {
        private final Semaphore created;
        private final Semaphore taken;
        private int bookId;

        public ReaderWriter(Semaphore created, Semaphore taken) {
            this.created = created;
            this.taken = taken;
            bookId = 0;
        }

        private void write() {
            try {
                created.acquire();
                bookId++;
                System.out.println("Book " + bookId + " was written");
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                taken.release();
            }
        }

        private void read() {
            try {
                taken.acquire();
                System.out.println("Book " + bookId + " was taken");
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                created.release();
            }
        }
    }

    private static class Reader extends Thread {
        private final ReaderWriter readerWriter;

        public Reader(ReaderWriter readerWriter) {
            this.readerWriter = readerWriter;
        }

        @Override
        public void run() {
            readerWriter.read();
        }
    }

    private static class Writer extends Thread {
        private final ReaderWriter readerWriter;

        public Writer(ReaderWriter readerWriter) {
            this.readerWriter = readerWriter;
        }

        @Override
        public void run() {
            readerWriter.write();
        }
    }



    public static void eighth() {
        ReaderWriter readerWriter = new ReaderWriter(new Semaphore(1), new Semaphore(0));
        Reader reader = new Reader(readerWriter);
        Writer writer = new Writer(readerWriter);
        writer.start();
        reader.start();
        for(int i = 0; i < 10; i++) {
            writer.run();
            reader.run();
        }
//        while (true) {
//            writer.run();
//            reader.run();
//        }
        try {
            writer.join();
            reader.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
