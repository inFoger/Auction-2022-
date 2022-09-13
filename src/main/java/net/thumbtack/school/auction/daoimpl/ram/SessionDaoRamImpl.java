package net.thumbtack.school.auction.daoimpl.ram;

import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.database.Database;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Session;
import net.thumbtack.school.auction.model.users.User;

public class SessionDaoRamImpl implements SessionDao {
    @Override
    public void create(Session session) {
        Database.getInstance().createSession(session);
    }

    @Override
    public void delete(String token) throws ServerException {
        Database.getInstance().deleteSession(token);
    }

    @Override
    public Session getSession(String token) throws ServerException {
        return Database.getInstance().getSession(token);
    }
}
