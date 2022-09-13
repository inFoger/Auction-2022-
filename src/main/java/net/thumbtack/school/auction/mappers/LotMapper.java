package net.thumbtack.school.auction.mappers;

import net.thumbtack.school.auction.model.Lot;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface LotMapper {
    @Select("SELECT * FROM `lot`")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "category", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.auction.mappers.LotMapper.getCategoryByLotId", fetchType = FetchType.EAGER))
    })
    List<Lot> getAll();

    @Select("SELECT * FROM `lot` WHERE id=#{id}")
    Lot getLotById(Integer id);

    @Select("SELECT * FROM `lot` WHERE id IN (SELECT lotid FROM `lot_category` WHERE category=#{category})")
    List<Lot> getByCategory(@Param("category")String category);

    @Update("UPDATE `lot` SET `name` = #{name}, `status`= #{status}, description = #{description}, minSellingPrice = #{minSellingPrice}, compulsorySalePrice=#{compulsorySalePrice}, currentPrice=#{currentPrice}, lastBuyer=#{lastBuyerLogin} WHERE id = #{lotId}")
    void updateLot(@Param("lotId")Integer lotId, @Param("name")String name, @Param("status")String stats, @Param("description")String description, @Param("minSellingPrice")int minSellingPrice, @Param("compulsorySalePrice")int compulsorySalePrice, @Param("currentPrice")int currentPrice, @Param("lastBuyerLogin")String lastBuyerLogin);

    @Insert("INSERT INTO `lot`(sellerLogin,`status`,`name`,description,currentPrice,minSellingPrice,compulsorySalePrice) " +
            "VALUES(#{newLot.sellerLogin},#{newLot.status},#{newLot.name},#{newLot.description},#{newLot.currentPrice},#{newLot.minSellingPrice},#{newLot.compulsorySalePrice})")
    @Options(useGeneratedKeys = true, keyProperty = "newLot.id")
    void create(@Param("newLot")Lot newLot);

    @Insert("INSERT `lot_category` VALUES(null,#{lotid}, #{category})")
    void insertCategory(@Param("lotid")Integer lotid, @Param("category")String category);

    @Delete("DELETE FROM `lot` WHERE id=#{id}")
    void delete(Integer id);

    @Select("SELECT category FROM `lot_category` WHERE lotid=#{id}")
    List<String> getCategories(@Param("id")int id);

    @Select("SELECT category FROM `lot_category` WHERE lotid=#{id}")
    String getCategoryByLotId(int id);

    @Select("DELETE FROM `lot_category` WHERE lotid=#{id}")
    void deleteCategoriesByLotId(int id);

    @Insert("INSERT `buyer_lot` VALUES(#{lotid},#{buyerLogin})")
    void insertLotIntoBuyingTable(@Param("lotid")Integer lotid, @Param("buyerLogin")String buyerLogin);

}
