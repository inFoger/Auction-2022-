package net.thumbtack.school.fifteenthExercise.bPerson;

import java.util.Objects;
import java.util.Optional;

//12.b Реализовать класс Person с двумя полями Optional<Person> father,
// Optional<Person> mother (передавать в конструктор Person или null,
// оборачивать в Optional через Optional#ofNullable).
// Метод getMothersMotherFather должен вернуть Optional<Person> и быть реализованным только на базе
// вызова цепочки Optional.flatMap.
public class Person {
    private String firstname;
    private String lastname;
    private Optional<Person> mother;
    private Optional<Person> father;

    public Person(String firstname, String lastname, Person mother, Person father) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mother = Optional.ofNullable(mother);
        this.father = Optional.ofNullable(father);
    }

    public Optional<Person> getMothersMotherFather() {
        return getMother().flatMap(person -> person.mother).flatMap(person -> person.father);
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Optional<Person> getMother() {
        return mother;
    }

    public Optional<Person> getFather() {
        return father;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstname, person.firstname) && Objects.equals(lastname, person.lastname) && Objects.equals(mother, person.mother) && Objects.equals(father, person.father);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, mother, father);
    }

}
