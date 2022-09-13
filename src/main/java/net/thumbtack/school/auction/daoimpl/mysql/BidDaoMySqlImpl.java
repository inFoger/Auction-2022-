package net.thumbtack.school.auction.daoimpl.mysql;

import net.thumbtack.school.auction.dao.BidDao;
import net.thumbtack.school.auction.daoimpl.DaoImplBase;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Bid;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidDaoMySqlImpl extends DaoImplBase implements BidDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidDaoMySqlImpl.class);
    @Override
    public Bid create(Integer lotId, Bid newBid) throws ServerException {
        LOGGER.debug("DAO create bid with lotid {}, bid {}",lotId, newBid);
        try (SqlSession sqlSession = getSession()) {
            try {
                if(getBidMapper(sqlSession).isBidExist(newBid.getBuyerLogin(),lotId) != null){
                    getBidMapper(sqlSession).updateBid(newBid.getBuyerLogin(),lotId,newBid.getPrice());
                } else {
                    getBidMapper(sqlSession).create(lotId, newBid);
                }

            } catch (RuntimeException ex) {
                LOGGER.info("Can't create bid.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return newBid;
    }

    @Override
    public void delete(Integer lotId, String buyerLogin) throws ServerException {
        LOGGER.debug("DAO delete bid with lotid {}, buyerLogin {}",lotId, buyerLogin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getBidMapper(sqlSession).delete(lotId, buyerLogin);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete bid.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
