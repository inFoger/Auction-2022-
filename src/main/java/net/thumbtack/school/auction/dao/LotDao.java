package net.thumbtack.school.auction.dao;

import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Lot;

import java.util.List;

public interface LotDao {
    List<Lot> getAll();
    Lot create(Lot newLot) throws ServerException;
    void delete(Integer id) throws ServerException;
    Lot getLotById(Integer id) throws ServerException;
    void updateLot(Integer lotId, String name, String status, String description, int minSellingPrice, int compulsorySalePrice, int startPrice, String lastBuyerLogin, List<String> categories) throws ServerException;
    List<Lot> getByCategory(String category);
    List<Lot> getByAtLeastOneCategory(List<String> categories);
    List<Lot> getByCategories(List<String> categories);
    void lotBuying(Integer lotId, String buyerLogin);
}
