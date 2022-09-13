package net.thumbtack.school.database.model;

import java.io.Serializable;
import java.util.Objects;
//методы equal и hashCode добавлены для корректной работы тестов. Имплементируется Serializable для сериализации
public class Trainee implements Serializable {
    private int id;
    private String lastName;
    private String firstName;
    private int rating;

    public Trainee() {
    }

    public Trainee(int id, String firstName, String lastName, int rating) {
        setFirstName(firstName);
        setLastName(lastName);
        setRating(rating);
        setId(id);
    }

    public Trainee(String firstName, String lastName, int rating) {
        this(0, firstName, lastName, rating);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainee)) return false;
        Trainee trainee = (Trainee) o;
        return getId() == trainee.getId() && getRating() == trainee.getRating() && Objects.equals(getLastName(), trainee.getLastName()) && Objects.equals(getFirstName(), trainee.getFirstName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLastName(), getFirstName(), getRating());
    }
}
