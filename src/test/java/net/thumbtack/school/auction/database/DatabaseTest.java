package net.thumbtack.school.auction.database;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dto.request.lot.GetAllLotsDtoRequest;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Bid;
import net.thumbtack.school.auction.model.Lot;
import net.thumbtack.school.auction.model.Session;
import net.thumbtack.school.auction.model.users.Auctioneer;
import net.thumbtack.school.auction.model.users.Buyer;
import net.thumbtack.school.auction.model.users.Seller;
import net.thumbtack.school.auction.model.users.User;
import net.thumbtack.school.auction.server.TestBase;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest extends TestBase {
    //тесты для проверки работоспособности методов Database. Основные проверки на корректность вынесены
    //в метод validate() у классов Request

    Seller user1 = new Seller("Frodo98", "123456", "Frodo",
            "Baggins", "Ivanovich");
    Buyer user2 = new Buyer("Jackie01", "123456",
            "Jackie", "Chan", null);
    Auctioneer admin = new Auctioneer("admin", "admin", "Jack",
            "Black", "Thomas");
    Session session1 = new Session(UUID.randomUUID().toString(), user1);
    Session session2 = new Session(UUID.randomUUID().toString(), user2);
    Lot lot1 = new Lot(user1, "Помидоры", "Помидоры обыкновенные", 30,
            50, 100, null);
    Lot lot2 = new Lot(user1, "Мандарины", "Мандарины обыкновенные", 20,
            40, 90, null);
    Bid bid1 = new Bid(user2, 1, 31);
    Bid bid2 = new Bid(user2, 2, 32);

    @Test
    void getInstanceTest() {
        assertNotEquals(null, Database.getInstance());
    }

    @Test
    void userTest() throws ServerException {
        Set<User> checkSet = new HashSet<>();
        checkSet.add(admin);
        assertEquals(checkSet, new HashSet<>(Database.getInstance().getAllUsers()));

        Database.getInstance().createUser(user1);
        Database.getInstance().createUser(user2);
        checkSet.add(user1);
        checkSet.add(user2);
        assertEquals(checkSet,new HashSet<>(Database.getInstance().getAllUsers()));

        Database.getInstance().deleteUser(user2.getLogin());
        checkSet.remove(user2);
        assertEquals(checkSet,new HashSet<>(Database.getInstance().getAllUsers()));

        assertEquals(user1, Database.getInstance().getUserByLogin(user1.getLogin()));
        assertTrue(Database.getInstance().isLoginExist(user1.getLogin()));
        Database.getInstance().clearDatabase();
    }

    @Test
    void sessionTest() throws ServerException {
        Database.getInstance().createSession(session1);
        Database.getInstance().createSession(session2);
        assertEquals(session1, Database.getInstance().getSession(session1.getToken()));
        assertEquals(session2, Database.getInstance().getSession(session2.getToken()));
        Database.getInstance().deleteSession(session2.getToken());
        try{
            Database.getInstance().getSession(session2.getToken());
        } catch (ServerException e){
            assertEquals(new ServerException(ServerErrorCode.SESSION_EXPIRED), e);
        }
        Database.getInstance().clearDatabase();
    }

    @Test
    void lotAndBidTest() throws ServerException {
        Set<Lot> checkSet = new HashSet<>();
        lot1.setId(1);
        checkSet.add(lot1);
        lot2.setId(2);
        checkSet.add(lot2);
        Database.getInstance().createLot(lot1);
        Database.getInstance().createLot(lot2);
        assertEquals(checkSet, new HashSet<>(Database.getInstance().getAllLots()));
        assertEquals(lot1, Database.getInstance().getLotById(1));
        assertEquals(lot2, Database.getInstance().getLotById(2));
        Database.getInstance().deleteLot(2);
        checkSet.remove(lot2);
        assertEquals(checkSet, new HashSet<>(Database.getInstance().getAllLots()));

        Set<Bid> bidSet = new HashSet<>();
        bidSet.add(bid2);
        Database.getInstance().makeBid(1, bid1);
        Database.getInstance().makeBid(1, bid2); //Храним последнюю ставку. Или нужно хранить все ставки ?
        assertEquals(bidSet, new HashSet<>(Database.getInstance().getLotById(1).getBids()));
        Database.getInstance().deleteBid(1, bid2.getBuyerLogin());
        bidSet.clear();
        assertEquals(bidSet, new HashSet<>(Database.getInstance().getLotById(1).getBids()));
        Database.getInstance().clearDatabase();
    }

    @Test
    void categoriesTest() throws ServerException {
        Set<Lot> checkSet = new HashSet<>();
        List<String> categories1 = new ArrayList<>();
        List<String> categories2 = new ArrayList<>();
        categories1.add("продукты");
        categories1.add("овощи");
        categories2.add("продукты");
        categories2.add("фрукты");
        lot1.setCategories(categories1);
        lot2.setCategories(categories2);
        lot1.setId(1);
        lot2.setId(2);
        Database.getInstance().createLot(lot1);
        Database.getInstance().createLot(lot2);
        checkSet.add(lot1);
        checkSet.add(lot2);
        assertEquals(checkSet, new HashSet<>(Database.getInstance().getByCategory("продукты")));
        checkSet.remove(lot2);
        assertEquals(checkSet, new HashSet<>(Database.getInstance().getByCategories(categories1)));
        checkSet.clear();
        List<String> req = new ArrayList<>();
        req.add("видеокарты");
        req.add("фрукты");
        checkSet.add(lot2);
        assertEquals(checkSet, new HashSet<>(Database.getInstance().getByAtLeastOneCategory(req)));
        checkSet.clear();
        assertEquals(checkSet, new HashSet<>(Database.getInstance().getByCategory("видеокарты")));
        Database.getInstance().clearDatabase();
    }

}