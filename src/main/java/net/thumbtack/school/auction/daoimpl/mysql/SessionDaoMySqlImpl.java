package net.thumbtack.school.auction.daoimpl.mysql;

import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.daoimpl.DaoImplBase;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Session;
import net.thumbtack.school.auction.model.users.Auctioneer;
import net.thumbtack.school.auction.model.users.Buyer;
import net.thumbtack.school.auction.model.users.Seller;
import net.thumbtack.school.auction.model.users.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionDaoMySqlImpl extends DaoImplBase implements SessionDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDaoMySqlImpl.class);
    @Override
    public void create(Session session) {
        LOGGER.debug("DAO create session {}", session);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).create(session);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't create session.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(String token) throws ServerException {
        LOGGER.debug("DAO delete session by token {}", token);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).delete(token);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete session by token.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }

    }

    @Override
    public Session getSession(String token) throws ServerException {
        LOGGER.debug("DAO get session by token {}", token);
        Session result = null;
        try (SqlSession sqlSession = getSession()) {
            int userid = getSessionMapper(sqlSession).getSession(token);
            if(userid > 0) {
                User user = getUserMapper(sqlSession).getById(userid);
                if(getUserMapper(sqlSession).isAuctioneer(user.getLogin()) != null){
                    user = new Auctioneer(user);
                } else if(getUserMapper(sqlSession).isBuyer(user.getLogin()) != null) {
                    user = new Buyer(user);
                } else if(getUserMapper(sqlSession).isSeller(user.getLogin()) != null) {
                    user = new Seller(user);
                } else {
                    throw new ServerException(ServerErrorCode.WRONG_USER_TYPE);
                }
                result = new Session(token, user);
            }
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get session by token.\n{}", ex.getMessage());
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
        return result;
    }
}
