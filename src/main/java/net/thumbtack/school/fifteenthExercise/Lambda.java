package net.thumbtack.school.fifteenthExercise;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.*;

class Lambda {
    //1. Используя функциональный интерфейс java.util.function.Function и лямбда-выражения, создайте:
    //функцию split (String s) -> List<String>, разбивающую строку по пробелам
    //функцию count (List<?> list) -> Integer, считающую количество элементов в любом списке
    //Примените split к строке, содержащей пробелы, а после этого примените count к ее результату .
    public static Function<String, List<String>> split = s -> Arrays.asList(s.split(" "));
    public static Function<List<?>, Integer> count = List::size;

    //2. Попробуйте избавиться от декларации типов в параметрах функций из пункта 1. Почему это возможно?
    // Ответ: Из-за типа параметра

    //3. Попробуйте заменить лямбда-выражение на method reference, в каких случаях это возможно и почему?
    // Ответ: На сколько я понял, только тогда, когда нужный метод не вызывается без параметров(помимо this),
    // А в "записи" method reference аргументы передать нельзя, поэтому он и не будет знать, что делать

    //4. Перепишите решение из п. 1, композируя функции split и count при помощи default-методов интерфейса Function,
    // в новую функцию splitAndCount:
    //используйте andThen
    //используйте compose
    //    Чем данный подход отличается от count.apply(split.apply(str)) ?
    // Ответ: Читабельностью кода (?)
    public static Function<String, Integer> splitAndCount1 = split.andThen(count);
    public static Function<String, Integer> splitAndCount2 = count.compose(split);

    //5. Напишите функцию create, принимающую в качестве аргумента строку и возвращающую Person
    // с именем равным переданной строке. Перепишите при помощи method reference.
    public static Function<String, Person> create1 = str -> new Person(str);
    public static Function<String, Person> create2 = Person::new;

    //6. Реализуйте функцию max, используя method reference к Math.max.
    // Какой интерфейс из java.util.function подойдет для функции с двумя параметрами?
    // Ответ: BiFunction
    public static BiFunction<Integer, Integer, Integer> max = Math::max;

    //7. Реализуйте функцию getCurrentDate, возвращающую текущую дату () -> java.util.Date.
    // Какой интерфейс из java.util.function подойдет для функции без параметров?
    // Ответ Supplier
    public static Supplier<Date> getCurrentDate = Date::new;

    //8. Реализуйте функцию isEven (Integer a) -> Boolean.
    // Какой интерфейс из java.util.function для этого подойдет?
    // Ответ Predicate
    public static Predicate<Integer> isEven = a -> a % 2 == 0;

    //9. Реализуйте функцию areEqual (Integer a, Integer b) -> Boolean.
    // Какой интерфейс из java.util.function для этого подойдет?
    // Ответ BiPredicate
    public static BiPredicate<Integer, Integer> areEqual  = Integer::equals;

}



