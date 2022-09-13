package net.thumbtack.school.fifteenthExercise;

import java.util.Arrays;
import java.util.List;

public class MyLambda {
    //10. Создайте интерфейс MyFunction, декларирующий единственный метод K apply(T arg).
    // Замените Function на MyFunction.
    public static MyFunction<String, List<String>> split = s -> Arrays.asList(s.split(" "));
    public static MyFunction<List<?>, Integer> count = List::size;
    public static MyFunction<String, Person> create2 = Person::new;
}
