package net.thumbtack.school.fifteenthExercise;

//10. Создайте интерфейс MyFunction, декларирующий единственный метод K apply(T arg).
// Замените Function на MyFunction.

//11. Предположим вы решили использовать функцию с двумя аргументами.
// Что произойдет когда вы добавите K apply(T arg1, T arg2)?
// Задекларируйте MyFunction как функциональный интерфейс. Попробуйте скомпилировать.
// Ответ: При попытке скомпилировать выдаёт следующее:
// java: Unexpected @FunctionalInterface annotation
//  net.thumbtack.school.fifteenthExercise.MyFunction is not a functional interface
//    multiple non-overriding abstract methods found in interface net.thumbtack.school.fifteenthExercise.MyFunction
// Функциональный интерфейс должен иметь один метод
@FunctionalInterface
public interface MyFunction<T, K> {
    //K apply(T arg1, T arg2);
    K apply(T arg);
}
