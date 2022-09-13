package net.thumbtack.school.auction.daoimpl.ram;

import net.thumbtack.school.auction.dao.LotDao;
import net.thumbtack.school.auction.database.Database;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Lot;

import java.util.List;

public class LotDaoRamImpl implements LotDao {
    @Override
    public List<Lot> getAll() {
        return Database.getInstance().getAllLots();
    }

    @Override
    public Lot getLotById(Integer id) throws ServerException {
        return Database.getInstance().getLotById(id);
    }

    @Override
    public List<Lot> getByCategory(String category) {
        return Database.getInstance().getByCategory(category);
    }

    @Override
    public List<Lot> getByAtLeastOneCategory(List<String> categories) {
        return Database.getInstance().getByAtLeastOneCategory(categories);
    }

    @Override
    public List<Lot> getByCategories(List<String> categories) {
        return Database.getInstance().getByCategories(categories);
    }

    @Override
    public void lotBuying(Integer lotId, String buyerLogin) {
        Database.getInstance().addBuyedLot(lotId, buyerLogin);
    }

    @Override
    public void updateLot(Integer lotId, String name, String status, String description, int minSellingPrice, int compulsorySalePrice, int startPrice, String lastBuyerLogin, List<String> categories) throws ServerException {
        Database.getInstance().updateLot(lotId, name, status, description, minSellingPrice, compulsorySalePrice, startPrice, lastBuyerLogin, categories);
    }

    @Override
    public Lot create(Lot newLot) throws ServerException {
        return Database.getInstance().createLot(newLot);
    }

    @Override
    public void delete(Integer id) throws ServerException {
        Database.getInstance().deleteLot(id);
    }
}
