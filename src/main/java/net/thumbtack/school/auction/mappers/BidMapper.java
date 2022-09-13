package net.thumbtack.school.auction.mappers;

import net.thumbtack.school.auction.model.Bid;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BidMapper {
    @Insert("INSERT INTO `bid`(buyerlogin, lotid, price) " +
            "VALUES(#{newBid.buyerLogin},#{lotId},#{newBid.price})")
    void create(@Param("lotId")Integer lotId, @Param("newBid")Bid newBid);

    @Select("SELECT lotid FROM `bid` WHERE buyerlogin=#{buyerLogin} AND lotid=#{lotId}")
    Integer isBidExist(@Param("buyerLogin")String buyerLogin, @Param("lotId")Integer lotId);

    @Update("UPDATE `bid` SET `price`=#{price} WHERE buyerlogin=#{buyerLogin} AND lotid=#{lotId}")
    void updateBid(@Param("buyerLogin")String buyerLogin, @Param("lotId")Integer lotId, @Param("price")Integer price);

    @Select("SELECT buyerlogin, lotid, price FROM `bid` WHERE lotid=#{lotid}")
    List<Bid> getAllLotBids(int lotid);

    @Delete("DELETE FROM `bid` WHERE buyerlogin=#{buyerLogin} AND lotid=#{lotId}")
    void delete(@Param("lotId")Integer lotId, @Param("buyerLogin")String buyerLogin);
}
