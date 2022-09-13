package net.thumbtack.school.fifteenthExercise.bPerson;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    @Test
    void test() {
        Person person1 = new Person("A","A", null, null);
        Person person2 = new Person("B", "B", null, person1);
        Person person3 = new Person("C", "C", person2, null);
        Person person4 = new Person("D","D", person3, null);
        assertTrue(person1.getMothersMotherFather().isEmpty());
        assertTrue(person2.getMothersMotherFather().isEmpty());
        assertTrue(person3.getMothersMotherFather().isEmpty());
        assertEquals(Optional.of(person1), person4.getMothersMotherFather());
    }
}