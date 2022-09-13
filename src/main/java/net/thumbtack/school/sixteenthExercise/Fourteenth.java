package net.thumbtack.school.sixteenthExercise;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//14. Написать класс Message, содержащий 4 текстовых поля: emailAddress, sender, subject, body,
// и класс Transport, с методом send(Message message), отсылающий письмо на некий SMTP-сервер.
// Реализовать массовую рассылку одного и того же текста, email адреса берутся из текстового файла.
// Имейте в виду, что отправка одного письма требует довольно много времени
// (установление соединения с сервером, отсылка, получение ответа),
// поэтому последовательная отправка писем не является хорошим решением.
// Порядок отправки писем произвольный и не обязан совпадать с порядком email адресов в файле.
// Примечание 1. Реальную отправку писем производить не надо, вместо
// этого достаточно выводить их в некоторый файл.
public class Fourteenth {
    private static class Message {
        private final String emailAddress;
        private final String sender;
        private final String subject;
        private final String body;

        public String getEmailAddress() {
            return emailAddress;
        }

        public String getSender() {
            return sender;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }

        public Message(String emailAddress, String sender, String subject, String body) {
            this.emailAddress = emailAddress;
            this.sender = sender;
            this.subject = subject;
            this.body = body;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "emailAddress='" + emailAddress + '\'' +
                    ", sender='" + sender + '\'' +
                    ", subject='" + subject + '\'' +
                    ", body='" + body + '\'' +
                    '}';
        }
    }

    private static class Transport {
        private static BlockingQueue<Message> messages = new ArrayBlockingQueue<>(6);

        public static void send(Message message) throws InterruptedException {
            messages.put(message);
        }

        public static Message read() throws InterruptedException {
            return messages.take();
        }
    }

    private static class MessageReader extends Thread {
        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new FileReader(
                    "src/main/java/net/thumbtack/school/sixteenthExercise/emails.txt"))) {
                String address;
                System.out.println("MessageReader Start");
                while((address = reader.readLine()) != null) {
                    Transport.send(new Message(address, "anton2801z@gmail.com",
                            "Что-то", "Что-то подробнее"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("MessageReader End");
        }
    }

    private static class MessageSender extends Thread {
        @Override
        public void run() {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "src/main/java/net/thumbtack/school/sixteenthExercise/messagesRecipient.txt"))) {
                Message message;
                System.out.println("__MessageSender Start");
                while (true) {
                    try {
                        message = Transport.read();
                        if (message.getEmailAddress() == null) {
                            System.out.println("__MessageSender End");
                            break;
                        }
                        System.out.println("__Message was sent. Email: " + message.getEmailAddress());
                        writer.write(message + "\n");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MessageReader reader = new MessageReader();
        MessageSender sender = new MessageSender();
        MessageSender sender2 = new MessageSender();
        MessageSender sender3 = new MessageSender();
        reader.start();
        sender.start();
        sender2.start();
        sender3.start();
        try {
            reader.join();
            Thread.sleep(100);
            // добавляем яд
            Transport.send(new Message(null, null, null, null));
            Transport.send(new Message(null, null, null, null));
            Transport.send(new Message(null, null, null, null));
            sender.join();
            sender2.join();
            sender3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
