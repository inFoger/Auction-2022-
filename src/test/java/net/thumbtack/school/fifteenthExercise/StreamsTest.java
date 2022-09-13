package net.thumbtack.school.fifteenthExercise;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class StreamsTest {

    @Test
    void transform() {
        IntStream intStream = IntStream.of(1, 2, 3, 4, 5);
        IntUnaryOperator operator = a -> -a;
        IntStream output = Streams.transform(intStream, operator);
    }

    @Test
    void transformToParallel() {
        IntStream intStream = IntStream.of(1, 2, 3, 4, 5);
        IntUnaryOperator operator = a -> -a;
        IntStream output = Streams.transformToParallel(intStream, operator);
    }

    @Test
    void uniqueNamesForAllPeopleOlderThanThirtySortedByNamesLength() {
        Person person1 = new Person("Frodo", 31);
        Person person2 = new Person("Frodo", 31);
        Person person3 = new Person("Frodo", 32);
        Person person4 = new Person("Itachi", 29);
        Person person5 = new Person("Ivan", 33);
        Person person7 = new Person("Hao", 35);
        Person person8 = new Person("Ivan", 32);
        List<Person> list1 = new ArrayList<>();
        Collections.addAll(list1, person1, person2, person3, person4, person5, person7, person8);
        List<String> list2 = new ArrayList<>();
        Collections.addAll(list2, "Hao", "Ivan", "Frodo");

        List<String> result = Streams.uniqueNamesForAllPeopleOlderThanThirtySortedByNamesLength(list1);
        assertEquals(list2, result);
    }

    @Test
    void uniqueNamesForAllPeopleOlderThanThirtySortedByFrequency() {
        Person person1 = new Person("Frodo", 31);
        Person person2 = new Person("Frodo", 31);
        Person person3 = new Person("Frodo", 32);
        Person person4 = new Person("Itachi", 29);
        Person person5 = new Person("Ivan", 33);
        Person person7 = new Person("Hao", 35);
        Person person8 = new Person("Ivan", 32);
        List<Person> list1 = new ArrayList<>();
        Collections.addAll(list1, person1, person2, person3, person4, person5, person7, person8);
        List<String> list2 = new ArrayList<>();
        Collections.addAll(list2, "Hao", "Ivan", "Frodo");

        List<String> result = Streams.uniqueNamesForAllPeopleOlderThanThirtySortedByFrequency(list1);
        assertEquals(list2, result);
    }

    @Test
    void sum() {
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 1,1,1);
        assertEquals(3, Streams.sum(list));
    }

    @Test
    void product() {
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 1,1,1);
        assertEquals(1,Streams.product(list));
    }
}