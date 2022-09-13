package net.thumbtack.school.auction.model.users;

import java.util.Objects;

//обновить в конце equals и hashCode
public class User {
    //Сделать потом int; id == 0 - несохраненный экземпляр,  id > 0 - сохраненный
    private Integer id; //В DAO прописывается, может быть null
    private String login;
    private String password;
    final private String firstName;
    final private String lastName;
    private String secondName = null; //Отчество не обязательно, может быть null
    public User(String login, String password, String firstName, String lastName, String secondName) {
        id = null;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
    }

    public User(Integer id, String login, String password, String firstName, String lastName, String secondName) {
        this(login, password, firstName, lastName, secondName);
        this.id = id;
    }

    public User(String login, String password, String firstName, String lastName) {
        this(login, password, firstName, lastName, null);
    }

    public User(User user) {
        this(user.getId(), user.getLogin(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getSecondName());
    }

    public Integer getId() {
        return id;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSecondName() {
        return secondName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && getLogin().equals(user.getLogin()) && getPassword().equals(user.getPassword()) && getFirstName().equals(user.getFirstName()) && getLastName().equals(user.getLastName()) && Objects.equals(getSecondName(), user.getSecondName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin(), getPassword(), getFirstName(), getLastName(), getSecondName());
    }

    public String print() {
        return "login='" + login + '\'';
    }
}
