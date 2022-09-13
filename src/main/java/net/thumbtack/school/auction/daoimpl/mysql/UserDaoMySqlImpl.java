package net.thumbtack.school.auction.daoimpl.mysql;

import net.thumbtack.school.auction.dao.UserDao;
import net.thumbtack.school.auction.daoimpl.DaoImplBase;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.users.Auctioneer;
import net.thumbtack.school.auction.model.users.Buyer;
import net.thumbtack.school.auction.model.users.Seller;
import net.thumbtack.school.auction.model.users.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoMySqlImpl extends DaoImplBase implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoMySqlImpl.class);
    @Override
    public List<User> getAll() {
        LOGGER.debug("DAO get all Users");
        try (SqlSession sqlSession = getSession()) {
            return getUserMapper(sqlSession).getAll();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all Users.\n{}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public User create(User newUser) throws ServerException {
        LOGGER.debug("DAO insert User {}", newUser);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).createUser(newUser);
                if(newUser instanceof Seller){
                    getUserMapper(sqlSession).insertSeller(newUser);
                } else {
                    //т.е. Buyer
                    getUserMapper(sqlSession).insertBuyer(newUser);
                }

            } catch (RuntimeException ex) {
                //TODO заменить все экзепшены новые в mysql имплементации на серверные
                LOGGER.info("Can't insert User.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw new ServerException(ServerErrorCode.MYSQL_CANT_INSERT_USER);
            }
            sqlSession.commit();
        }
        return newUser;
    }

    @Override
    public void delete(String login) throws ServerException {
        LOGGER.debug("DAO delete User with login {}", login);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).deleteUser(login);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete User.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public User getByLogin(String login) throws ServerException {
        LOGGER.debug("DAO get User with login {}", login);
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getByLogin(login);
            if(getUserMapper(sqlSession).isAuctioneer(user.getLogin()) != null){
                user = new Auctioneer(user);
            } else if(getUserMapper(sqlSession).isBuyer(user.getLogin()) != null) {
                user = new Buyer(user);
            } else if(getUserMapper(sqlSession).isSeller(user.getLogin()) != null) {
                user = new Seller(user);
            } else {
                throw new ServerException(ServerErrorCode.WRONG_USER_TYPE);
            }
            return user;
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get User.\n{}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean isLoginExist(String login) {
        LOGGER.debug("DAO is User with login {} exist", login);
        try (SqlSession sqlSession = getSession()) {
            if(getUserMapper(sqlSession).getByLogin(login) != null) {
                return true;
            }
        } catch (RuntimeException ex) {
            LOGGER.info("DAO User with login {} doesn't exist", login);
            throw ex;
        }
        return false;
    }

    @Override
    public boolean isAuctioneer(String login) {
        LOGGER.debug("DAO is Auctioneer {}", login);
        try (SqlSession sqlSession = getSession()) {
            return getUserMapper(sqlSession).isAuctioneer(login) != null ;
        } catch (RuntimeException ex) {
            LOGGER.info("DAO isn't Auctioneer");
            throw ex;
        }
    }

    @Override
    public boolean isSeller(String login) {
        LOGGER.debug("DAO is Seller {}", login);
        try (SqlSession sqlSession = getSession()) {
            return getUserMapper(sqlSession).isSeller(login) != null ;
        } catch (RuntimeException ex) {
            LOGGER.info("DAO isn't Seller");
            throw ex;
        }
    }

    @Override
    public boolean isBuyer(String login) {
        LOGGER.debug("DAO is Buyer {}", login);
        try (SqlSession sqlSession = getSession()) {
            return getUserMapper(sqlSession).isBuyer(login) != null ;
        } catch (RuntimeException ex) {
            LOGGER.info("DAO isn't Buyer");
            throw ex;
        }
    }


}
