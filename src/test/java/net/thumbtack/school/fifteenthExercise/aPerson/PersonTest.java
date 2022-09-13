package net.thumbtack.school.fifteenthExercise.aPerson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    @Test
    void test() {
        Person person1 = new Person("A","A", null, null);
        Person person2 = new Person("B", "B", null, person1);
        Person person3 = new Person("C", "C", person2, null);
        Person person4 = new Person("D","D", person3, null);
        assertNull(person1.getMothersMotherFather());
        assertNull(person2.getMothersMotherFather());
        assertNull(person3.getMothersMotherFather());
        assertEquals(person1, person4.getMothersMotherFather());
    }
}