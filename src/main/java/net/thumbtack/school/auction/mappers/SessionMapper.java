package net.thumbtack.school.auction.mappers;

import net.thumbtack.school.auction.database.Database;
import net.thumbtack.school.auction.model.Session;
import org.apache.ibatis.annotations.*;

public interface SessionMapper {
    @Insert("INSERT INTO `session`(token,userid) VALUES(#{session.token},#{session.user.id})")
    void create(@Param("session")Session session);

    @Delete("DELETE FROM `session` WHERE token=#{token}")
    void delete(@Param("token")String token);

    @Select("SELECT userid FROM `session` WHERE token=#{token}")
    int getSession(@Param("token")String token);
}
