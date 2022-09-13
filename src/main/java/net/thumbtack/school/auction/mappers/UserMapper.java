package net.thumbtack.school.auction.mappers;

import net.thumbtack.school.auction.model.users.Buyer;
import net.thumbtack.school.auction.model.users.Seller;
import net.thumbtack.school.auction.model.users.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    @Select("SELECT * FROM `user`")
    List<User> getAll();

    @Insert("INSERT INTO `user`(`login`,`password`,firstName,lastName,secondName) " +
            "VALUES(#{user.login},#{user.password},#{user.firstName},#{user.lastName},#{user.secondName})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    void createUser(@Param("user") User user);

    @Insert("INSERT INTO `seller`(userLogin) VALUES (#{user.login})")
    void insertSeller(@Param("user")User user);

    @Insert("INSERT INTO `buyer`(userLogin) VALUES (#{user.login})")
    void insertBuyer(@Param("user")User user);

    @Delete("DELETE FROM `user` WHERE login=#{login}")
    void deleteUser(String login);

    @Select("SELECT * FROM `user` WHERE login=#{login}")
    User getByLogin(String login);

    @Select("SELECT * FROM `user` WHERE id=#{id}")
    User getById(int id);

    @Select("SELECT * FROM `auctioneer` WHERE userLogin=#{login}")
    String isAuctioneer(String login);

    @Select("SELECT * FROM `seller` WHERE userLogin=#{login}")
    String isSeller(String login);

    @Select("SELECT * FROM `buyer` WHERE userLogin=#{login}")
    String isBuyer(String login);
}
