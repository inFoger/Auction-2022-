package net.thumbtack.school.auction.daoimpl.ram;

import net.thumbtack.school.auction.dao.UserDao;
import net.thumbtack.school.auction.database.Database;
import net.thumbtack.school.auction.model.users.User;
import net.thumbtack.school.auction.exception.ServerException;

import java.util.List;

public class UserDaoRamImpl implements UserDao {
    @Override
    public List<User> getAll() {
        return Database.getInstance().getAllUsers();
    }

    @Override
    public User create(User newUser) throws ServerException {
        return Database.getInstance().createUser(newUser);
    }

    @Override
    public void delete(String login) throws ServerException {
        Database.getInstance().deleteUser(login);
    }


    @Override
    public User getByLogin(String login) throws ServerException {
        return Database.getInstance().getUserByLogin(login);
    }

    @Override
    public boolean isLoginExist(String login) {
        return Database.getInstance().isLoginExist(login);
    }

    @Override
    public boolean isAuctioneer(String login) throws ServerException {
        return Database.getInstance().isAuctioneer(login);
    }

    @Override
    public boolean isSeller(String login) throws ServerException {
        return Database.getInstance().isSeller(login);
    }

    @Override
    public boolean isBuyer(String login) throws ServerException {
        return Database.getInstance().isBuyer(login);
    }


}
