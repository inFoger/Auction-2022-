package net.thumbtack.school.fifteenthExercise.aPerson;

import java.util.Objects;

//12.a Реализовать класс Person с двумя полями Person father, Person mother (задавать их значения в конструкторе).
// Метод getMothersMotherFather должен либо вернуть экземпляр класса Person, либо null.
// Должна быть защита от NPE в цепочке условий.
public class Person {
    private String firstname;
    private String lastname;
    private Person mother;
    private Person father;

    public Person(String firstname, String lastname, Person mother, Person father) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mother = mother;
        this.father = father;
    }

    public Person getMothersMotherFather() {
        if(getMother() != null && getMother().getMother() != null) {
            return getMother().getMother().getFather();
        }
        return null;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Person getMother() {
        return mother;
    }

    public Person getFather() {
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
