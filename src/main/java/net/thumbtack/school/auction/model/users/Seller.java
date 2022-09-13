package net.thumbtack.school.auction.model.users;

public class Seller extends User{
    public Seller(String login, String password, String firstName, String lastName, String secondName) {
        super(login, password, firstName, lastName, secondName);
    }

    public Seller(Integer id, String login, String password, String firstName, String lastName, String secondName) {
        super(id, login, password, firstName, lastName, secondName);
    }

    public Seller(User user) {
        super(user);
    }
}
