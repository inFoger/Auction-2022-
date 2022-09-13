package net.thumbtack.school.auction.daoimpl.mysql;

import net.thumbtack.school.auction.dao.DatabaseDao;
import net.thumbtack.school.auction.daoimpl.DaoImplBase;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseDaoMySqlImpl extends DaoImplBase implements DatabaseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoMySqlImpl.class);

    @Override
    public void clear() {
        LOGGER.debug("DAO clear database");
        try (SqlSession sqlSession = getSession()) {
            try {
                getDatabaseMapper(sqlSession).clear();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't clear database.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }

    }

    @Override
    public void loadFromDatabase(String json) {
        //TODO
    }
}
