package net.thumbtack.school.database.mybatis.mappers;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.Trainee;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TraineeMapper {
    @Insert("INSERT INTO trainee VALUES (#{trainee.id}, #{trainee.firstName}, #{trainee.lastName}, #{trainee.rating}, #{group.id})")
    @Options(useGeneratedKeys = true, keyProperty = "trainee.id")
    Integer insert(@Param("group") Group group, @Param("trainee") Trainee trainee);

    @Select("SELECT * FROM trainee WHERE id = #{id}")
    Trainee getById(int id);

    @Select("SELECT * FROM trainee")
    List<Trainee> getAll();

    @Update("UPDATE trainee SET firstName = #{trainee.firstName}, lastName = #{trainee.lastName}, rating = #{trainee.rating} WHERE id = #{trainee.id}")
    void update(@Param("trainee") Trainee trainee);

    @Select({"<script>",
                "SELECT * FROM trainee",
                "<where>",
                    "<if test='firstName != null'> firstName LIKE #{firstName}",
                    "</if>",
                    "<if test='lastName != null'> lastName LIKE #{lastName}",
                    "</if>",
                    "<if test='rating != null'> AND rating = #{rating}",
                    "</if>",
                "</where>",
            "</script>"})
    List<Trainee> getAllWithParams(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("rating") Integer rating);

    @Insert({"<script>",
                "INSERT INTO trainee (firstName, lastName, rating) VALUES",
                    "<foreach item='item' collection='trainees' separator=','>",
                    "( #{item.firstName}, #{item.lastName}, #{item.rating} )",
                "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "trainees.id")
    void batchInsert(@Param("trainees") List<Trainee> trainees);

    @Delete("DELETE FROM trainee WHERE id = #{trainee.id}")
    int delete(@Param("trainee") Trainee trainee);

    @Select("SELECT * FROM trainee WHERE groupid = #{id}")
    List<Trainee> getByGroupId(int id);

    @Delete("DELETE FROM trainee")
    void deleteAll();
}
