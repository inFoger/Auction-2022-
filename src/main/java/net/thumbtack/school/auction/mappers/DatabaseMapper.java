package net.thumbtack.school.auction.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

public interface DatabaseMapper {
    @Delete("DELETE FROM `user` WHERE NOT id = 1;")
    void clear();
}
