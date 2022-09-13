package net.thumbtack.school.auction.dao;

public interface DatabaseDao {
    void clear();
    void loadFromDatabase(String json);
}
