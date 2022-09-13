package net.thumbtack.school.auction.daoimpl;

import net.thumbtack.school.auction.mappers.*;
import net.thumbtack.school.database.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class DaoImplBase {
    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected BidMapper getBidMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(BidMapper.class);
    }

    protected LotMapper getLotMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(LotMapper.class);
    }

    protected SessionMapper getSessionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(SessionMapper.class);
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected DatabaseMapper getDatabaseMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(DatabaseMapper.class);
    }
}
