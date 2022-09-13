package net.thumbtack.school.auction.dao;

import net.thumbtack.school.auction.model.users.User;
import net.thumbtack.school.auction.exception.ServerException;

import java.util.List;

public interface UserDao {
    List<User> getAll();
    User create(User newUser) throws ServerException; //Возвращает User с id
    void delete(String login) throws ServerException;
    User getByLogin(String login) throws ServerException;
    boolean isLoginExist(String login) throws ServerException;
    boolean isAuctioneer(String login) throws ServerException;
    boolean isSeller(String login) throws ServerException;
    boolean isBuyer(String login) throws ServerException;
}
