package net.thumbtack.school.auction.daoimpl.mysql;

import net.thumbtack.school.auction.dao.LotDao;
import net.thumbtack.school.auction.daoimpl.DaoImplBase;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Bid;
import net.thumbtack.school.auction.model.Lot;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LotDaoMySqlImpl extends DaoImplBase implements LotDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(LotDaoMySqlImpl.class);

    private void lotFulling(Lot lot, SqlSession sqlSession) {
        if(lot != null) {
            List<Bid> bids = getBidMapper(sqlSession).getAllLotBids(lot.getId());
            if(bids != null) {
                Map<String, Bid> map = new HashMap<>();
                for(Bid bid : bids) {
                    map.put(bid.getBuyerLogin(), bid);
                }
                lot.setBids(map);
            }
            List<String> categories = getLotMapper(sqlSession).getCategories(lot.getId());
            if(categories != null) {
                lot.setCategories(categories);
            }
        }
    }

    @Override
    public List<Lot> getAll() {
        LOGGER.debug("DAO get all Lots");
        try (SqlSession sqlSession = getSession()) {
            List<Lot> list = getLotMapper(sqlSession).getAll();
            for(Lot lot : list) {
                lotFulling(lot, sqlSession);
            }
            return list;
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all Lots.\n{}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Lot create(Lot newLot) throws ServerException {
        LOGGER.debug("DAO create Lot {}", newLot);
        try (SqlSession sqlSession = getSession()){
            try {
                getLotMapper(sqlSession).create(newLot);
                if(newLot.getCategories() != null){
                    for(String category : newLot.getCategories()){
                        getLotMapper(sqlSession).insertCategory(newLot.getId(), category);
                    }
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't create Lot.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return newLot;
    }

    @Override
    public void delete(Integer id) throws ServerException {
        LOGGER.debug("DAO delete Lot by id {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                getLotMapper(sqlSession).delete(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete Lot by id.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }

    }

    @Override
    public Lot getLotById(Integer id) throws ServerException {
        LOGGER.debug("DAO get Lot by id {}", id);
        try (SqlSession sqlSession = getSession()) {
            Lot lot = getLotMapper(sqlSession).getLotById(id);
            lotFulling(lot, sqlSession);
            return lot;
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Lot by id.\n{}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void updateLot(Integer lotId, String name, String status, String description, int minSellingPrice, int compulsorySalePrice, int currentPrice, String lastBuyerLogin, List<String> categories) throws ServerException {
        LOGGER.debug("DAO update Lot. LotId {}, name {}, description {}, minSellingPrice {}, compulsorySalePrice {}, currentPrice {}, lastBuyerLogin {}, categories {}",
                lotId, name, description, minSellingPrice, compulsorySalePrice, currentPrice, lastBuyerLogin, categories);
        try (SqlSession sqlSession = getSession()) {
            try {
                getLotMapper(sqlSession).updateLot(lotId, name, status, description, minSellingPrice, compulsorySalePrice, currentPrice, lastBuyerLogin);
                getLotMapper(sqlSession).deleteCategoriesByLotId(lotId); //отчистка старых категорий
                if(categories != null) {
                    for(String category : categories) {
                        getLotMapper(sqlSession).insertCategory(lotId, category);
                    }
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't update Lot.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Lot> getByCategory(String category) {
        LOGGER.debug("DAO get Lot by category {}", category);
        try (SqlSession sqlSession = getSession()) {
            List<Lot> list = getLotMapper(sqlSession).getByCategory(category);
            for(Lot lot : list) {
                lotFulling(lot, sqlSession);
            }
            return list;
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Lot by category.\n{}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Lot> getByAtLeastOneCategory(List<String> categories) {
        LOGGER.debug("DAO get Lot By At Least One Category {}", categories);
        Set<Lot> set = new HashSet<>();
        for(String category : categories){
            set.addAll(getByCategory(category));
        }
        return new ArrayList<>(set);
    }

    @Override
    public List<Lot> getByCategories(List<String> categories) {
        LOGGER.debug("DAO get Lot By Categories {}", categories);
        Set<Lot> lotsWithAllCategories = new HashSet<>();
        boolean firstRequest = true;
        for(String category : categories){
            if(firstRequest) {
                // Сначала добавляем все лоты из первого запрос. Дальше их количество будет только уменьшаться
                lotsWithAllCategories.addAll(getByCategory(category));
                firstRequest = false;
            } else {
                // Потом убираем те, которые не встречаются в следующих запросах
                List<Lot> newSet = getByCategory(category);
                lotsWithAllCategories.removeIf(element -> !newSet.contains(element));
            }
        }
        return new ArrayList<>(lotsWithAllCategories);
    }

    @Override
    public void lotBuying(Integer lotId, String buyerLogin) {
        LOGGER.debug("DAO buying lotId {}, buyerLogin {}", lotId, buyerLogin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getLotMapper(sqlSession).insertLotIntoBuyingTable(lotId,buyerLogin);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't buying Lot.\n{}", ex.getMessage());
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
