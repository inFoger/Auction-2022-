package net.thumbtack.school.auction.dao;

import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Session;
import net.thumbtack.school.auction.model.users.User;

public interface SessionDao {
    void create(Session session);
    void delete(String token) throws ServerException;
    Session getSession(String token) throws ServerException;
}
