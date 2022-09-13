package net.thumbtack.school.auction.dao;

import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Bid;
import net.thumbtack.school.auction.model.users.Buyer;

import java.util.List;

public interface BidDao {
    Bid create(Integer lotId, Bid newBid) throws ServerException;
    void delete(Integer lotId, String buyerLogin) throws ServerException;
}
