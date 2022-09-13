package net.thumbtack.school.auction.model.users;

import net.thumbtack.school.auction.model.Lot;

import java.util.ArrayList;
import java.util.List;

public class Buyer extends User{
    public Buyer(String login, String password, String firstName, String lastName, String secondName) {
        super(login, password, firstName, lastName, secondName);
    }

    public Buyer(User user) {
        super(user);
    }

    public Buyer(Integer id, String login, String password, String firstName, String lastName, String secondName) {
        super(id, login, password, firstName, lastName, secondName);
    }
}
