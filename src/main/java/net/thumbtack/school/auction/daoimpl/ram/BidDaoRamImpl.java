package net.thumbtack.school.auction.daoimpl.ram;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dao.BidDao;
import net.thumbtack.school.auction.database.Database;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Bid;
import net.thumbtack.school.auction.model.users.Buyer;

import java.util.List;

public class BidDaoRamImpl implements BidDao {
    @Override
    public Bid create(Integer lotId, Bid newBid) throws ServerException {
        return Database.getInstance().makeBid(lotId, newBid);
    }


    @Override
    public void delete(Integer lotId, String buyerLogin) throws ServerException {
        Database.getInstance().deleteBid(lotId, buyerLogin);
    }
}
