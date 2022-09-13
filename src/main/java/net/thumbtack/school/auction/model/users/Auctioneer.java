package net.thumbtack.school.auction.model.users;

public class Auctioneer extends User{
    public Auctioneer(String login, String password, String firstName, String lastName, String secondName) {
        super(login, password, firstName, lastName, secondName);
    }

    public Auctioneer(User user) {
        super(user);
    }
}
