package net.thumbtack.school.fifteenthExercise;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Streams {
    //13. Напишите метод IntStream transform(IntStream stream, IntUnaryOperator op),
    //  трансформирующий каждый элемент при помощи операции op. Выведите результат на консоль.
    public static IntStream transform(IntStream stream, IntUnaryOperator op) {
        IntStream intStream = stream.map(op);
        intStream.forEach(System.out::println);
        return intStream;
    }

    //14. Задача аналогичная предыдущей, только теперь нужно трансформировать входящий Stream в параллельный,
    // обратите внимание на изменившийся вывод на консоль.
    public static IntStream transformToParallel(IntStream stream, IntUnaryOperator op) {
        IntStream intStream = stream.parallel().map(op);
        intStream.parallel().forEach(System.out::println);
        return intStream;
    }

    //15. Реализуйте класс Person(String name, int age). Имея список List<Person>,
    // при помощи Stream API необходимо вернуть уникальные имена для всех людей старше 30 лет,
    // отсортированные по длине имени.
    public static List<String> uniqueNamesForAllPeopleOlderThanThirtySortedByNamesLength(List<Person> list) {
        return list.stream().filter(person -> person.getAge() > 30).map(Person::getName).distinct().
                sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
    }

    //16. Имея список List<Person>, при помощи Stream API необходимо вернуть уникальные имена для всех людей
    // старше 30 лет, отсортированные по количеству людей с одинаковым именем. Используйте Collectors.groupingBy
    public static List<String> uniqueNamesForAllPeopleOlderThanThirtySortedByFrequency(List<Person> list) {
        return list.stream().filter(person -> person.getAge() > 30).
                collect(Collectors.groupingBy(Person::getName, Collectors.counting())).entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    //17. Реализуйте sum(List<Integer> list) (сумма элементов списка) и product(List<Integer> list) (их произведение)
    // через Stream.reduce
    public static Integer sum(List<Integer> list){
        return list.stream().reduce(Integer::sum).orElse(0);
    }

    public static Integer product(List<Integer> list){
        return list.stream().reduce((a,b) -> a*b).orElse(0);
    }

}
