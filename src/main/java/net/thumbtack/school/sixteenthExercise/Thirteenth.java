package net.thumbtack.school.sixteenthExercise;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

//13. Написать класс Formatter, с методом format(Date date), форматирующим дату-время.
// Для форматирования возьмите класс SimpleDateFormat.
// В основном потоке создать единственный экземпляр класса Formatter и 5 потоков,
// каждому потоку передается при инициализации этот экземпляр.
// Потоки должны корректно форматировать дату-время, синхронизация не разрешается. Использовать ThreadLocal.
public class Thirteenth {
    private static class Formatter {
        private static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<>();

        public String format(Date date) {
            dateFormat.set(new SimpleDateFormat("EEE, MMM d, ''yy"));
            return dateFormat.get().format(date);
        }
    }
    private static class FormatThread extends Thread {
        private final Formatter formatter;
        private final Date date;

        public FormatThread(Formatter formatter, Date date) {
            this.formatter = formatter;
            this.date = date;
        }

        @Override
        public void run() {
            System.out.println(formatter.format(date));
        }
    }

    public static void thirteenth() {
        Formatter formatter = new Formatter();
        Thread t1 = new FormatThread(formatter, Date.from(Instant.parse("1994-04-05T00:00:00.00Z")));
        Thread t2 = new FormatThread(formatter, Date.from(Instant.parse("1996-08-31T11:11:11.00Z")));
        Thread t3 = new FormatThread(formatter, Date.from(Instant.parse("2002-02-01T22:22:22.00Z")));
        Thread t4 = new FormatThread(formatter, Date.from(Instant.parse("2006-03-20T23:33:33.00Z")));
        Thread t5 = new FormatThread(formatter, Date.from(Instant.parse("2011-11-11T21:44:44.00Z")));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
