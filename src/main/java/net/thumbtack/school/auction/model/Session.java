package net.thumbtack.school.auction.model;

import net.thumbtack.school.auction.model.users.User;

import java.util.Objects;

public class Session {
    private String token;
    private User user;


    public Session(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session = (Session) o;
        return Objects.equals(getToken(), session.getToken()) && Objects.equals(getUser(), session.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken(), getUser());
    }

    private boolean userEquals(User o){
        if (user == o) return true;
        if (o == null) return false;
        return o.getLogin().equals(user.getLogin()) && o.getPassword().equals(user.getPassword()) && o.getFirstName().equals(user.getFirstName()) && o.getLastName().equals(user.getLastName()) && Objects.equals(o.getSecondName(), user.getSecondName());
    }
}

