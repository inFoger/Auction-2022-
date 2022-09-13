package net.thumbtack.school.auction.daoimpl.ram;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dao.DatabaseDao;
import net.thumbtack.school.auction.database.Database;

public class DatabaseDaoRamImpl implements DatabaseDao {
    @Override
    public void clear() {
        Database.getInstance().clearDatabase();
    }

    @Override
    public void loadFromDatabase(String json) {
        Database.getInstance().loadFromDatabase(new Gson().fromJson(json, Database.class));
    }
}
