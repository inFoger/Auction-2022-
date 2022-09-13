package net.thumbtack.school.auction;

import net.thumbtack.school.auction.dao.*;
import net.thumbtack.school.auction.daoimpl.mysql.*;
import net.thumbtack.school.auction.daoimpl.ram.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;



public class SettingsDatabase {
    //mysql - mysql, остальное - ram
    private static final Logger LOGGER = LoggerFactory.getLogger(BidDaoMySqlImpl.class);
    private static String typeOfImpl = null;
    public static String getTypeOfImpl() {
        if(typeOfImpl == null) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader("src/main/java/net/thumbtack/school/auction/settings.txt"))) {
                typeOfImpl = fileReader.readLine();
            } catch (Exception e) {
                LOGGER.debug("Type of implementation was NOT read successfully. {}", e.getMessage());
                return e.getMessage();
            }
            LOGGER.debug("Type of implementation was read successfully. Type is {}", typeOfImpl);
        }
        return typeOfImpl;
    }

    public static BidDao getBidDao() {
        if(getTypeOfImpl().equals("mysql")){
            return new BidDaoMySqlImpl();
        }
        return new BidDaoRamImpl();
    }

    public static LotDao getLotDao() {
        if(getTypeOfImpl().equals("mysql")){
            return new LotDaoMySqlImpl();
        }
        return new LotDaoRamImpl();
    }

    public static SessionDao getSessionDao() {
        if(getTypeOfImpl().equals("mysql")){
            return new SessionDaoMySqlImpl();
        }
        return new SessionDaoRamImpl();
    }

    public static UserDao getUserDao() {
        if(getTypeOfImpl().equals("mysql")){
            return new UserDaoMySqlImpl();
        }
        return new UserDaoRamImpl();
    }

    public static DatabaseDao getDatabaseDao() {
        if(getTypeOfImpl().equals("mysql")){
            return new DatabaseDaoMySqlImpl();
        }
        return new DatabaseDaoRamImpl();
    }



}
