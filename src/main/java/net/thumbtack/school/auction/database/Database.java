package net.thumbtack.school.auction.database;

import net.thumbtack.school.auction.model.Bid;
import net.thumbtack.school.auction.model.Lot;
import net.thumbtack.school.auction.model.Session;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.users.Auctioneer;
import net.thumbtack.school.auction.model.users.Buyer;
import net.thumbtack.school.auction.model.users.Seller;
import net.thumbtack.school.auction.model.users.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Database {
    private static Database instance;
    private Map<String, User> users;
    private Map<Integer, Lot> lots;
    private List<String> categories;
    private Map<String, Session> sessions;
    private Map<Integer, String> buyedLots;
    private AtomicInteger lastUserId;
    private AtomicInteger lastLotId;
    private AtomicInteger lastBidId;
    private Database() {
//        Сделать возможность загрузки бд с файла
//        if(){
//
//        }
        users = new ConcurrentHashMap<>();
        //создание аукционера
        users.put("admin",new Auctioneer("admin", "admin", "Jack", "Black", "Thomas"));
        lots = new ConcurrentHashMap<>();
        categories = Collections.synchronizedList(new ArrayList<>());
        //FIXME убрать при написании startServer и stopServer(дабвлении загрузки списка категорий при старте соответственно)
        //в clearDatabase то же самое
        categories.add("продукты");
        categories.add("овощи");
        categories.add("фрукты");
        categories.add("коплектующие пк");
        categories.add("видеокарты");

        sessions = new ConcurrentHashMap<>();
        buyedLots = new ConcurrentHashMap<>();
        lastUserId = new AtomicInteger(1);
        lastBidId = new AtomicInteger(1);
        lastLotId = new AtomicInteger(1);
        instance = this;
    }

    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }

    public void clearDatabase(){
        users = new ConcurrentHashMap<>();
        users.put("admin",new Auctioneer("admin", "admin", "Jack", "Black", "Thomas"));
        lots = new ConcurrentHashMap<>();
        categories = Collections.synchronizedList(new ArrayList<>());
        //FIXME убрать при написании startServer и stopServer(дабвлении загрузки списка категорий при старте соответственно)
        categories.add("продукты");
        categories.add("овощи");
        categories.add("фрукты");
        categories.add("коплектующие пк");
        categories.add("видеокарты");
        sessions = new ConcurrentHashMap<>();
        lastUserId = new AtomicInteger(1);
        lastBidId = new AtomicInteger(1);
        lastLotId = new AtomicInteger(1);
    }

    public void loadFromDatabase(Database newDatabase){
        instance = newDatabase;
    }

    //для User
    public List<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    public User createUser(User newUser) throws ServerException {
        synchronized (lastUserId) {
            newUser.setId(lastUserId.get());
            if(users.putIfAbsent(newUser.getLogin(), newUser) != null){
                newUser.setId(null);
                throw new ServerException(ServerErrorCode.LOGIN_ALREADY_EXISTS);
            }
            lastUserId.incrementAndGet();
        }
        return newUser;
    }

    public void deleteUser(String login) throws ServerException {
        if (users.remove(login, getUserByLogin(login))) {
            return;
        }
        throw new ServerException(ServerErrorCode.USER_NOT_FOUND);
    }

    public User getUserByLogin(String login) throws ServerException {
        User result = users.get(login);
        if(result == null){
            throw new ServerException(ServerErrorCode.USER_NOT_FOUND);
        }
        return result;
    }

    public boolean isLoginExist(String login) {
        return users.containsKey(login);
    }

    public boolean isAuctioneer(String login) throws ServerException {
        return getUserByLogin(login).getClass() == Auctioneer.class;
    }

    public boolean isSeller(String login) throws ServerException {
        return getUserByLogin(login).getClass() == Seller.class;
    }

    public boolean isBuyer(String login) throws ServerException {
        return getUserByLogin(login).getClass() == Buyer.class;
    }

    //для Session
    public void createSession(Session session){
        sessions.put(session.getToken(), session); //замена user.login на token
    }

    public void deleteSession(String token) throws ServerException {
        if(sessions.containsKey(token)){
            sessions.remove(token);
            return;
        }
        throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
    }

    public Session getSession(String token) throws ServerException {
        Session result = sessions.get(token);
        if(result == null){
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
        return result;
    }

    //для Lot
    public Lot createLot(Lot newLot) throws ServerException {
        synchronized (lastLotId) {
            newLot.setId(lastLotId.get());
            if(lots.putIfAbsent(newLot.getId(), newLot) != null){
                newLot.setId(null);
                throw new ServerException(ServerErrorCode.LOT_ID_ALREADY_EXIST);
            }
            lastLotId.incrementAndGet();
        }
        return newLot;
    }

    public Lot getLotById(Integer id) throws ServerException {
        Lot result = lots.get(id);
        if(result == null){
            throw new ServerException(ServerErrorCode.LOT_NOT_FOUND);
        }
        return result;
    }

    public void updateLot(Integer lotId, String name, String status, String description, int minSellingPrice, int compulsorySalePrice, int startPrice , String lastBuyerLogin, List<String> categories) throws ServerException {
        Lot lot = getLotById(lotId);
        lot.setName(name);
        lot.setStatus(status);
        lot.setDescription(description);
        lot.setMinSellingPrice(minSellingPrice);
        lot.setCompulsorySalePrice(compulsorySalePrice);
        lot.setCategories(categories);
        lot.setStartPrice(startPrice);
        lot.setLastBuyerLogin(lastBuyerLogin);
    }

    public void deleteLot(Integer id) throws ServerException {
        if(lots.remove(id, getLotById(id))){
            return;
        }
        throw new ServerException(ServerErrorCode.LOT_NOT_FOUND);
    }

    public ArrayList<Lot> getAllLots(){
        return new ArrayList<>(lots.values());
    }

    public List<Lot> getByCategory(String category) {
        List<Lot> res = new ArrayList<>();
        for(Lot elem: lots.values()){
            if(elem.getCategories() != null){
                if(elem.getCategories().contains(category)){
                    res.add(elem);
                }
            }
        }
        return res;
    }

    public List<Lot> getByAtLeastOneCategory(List<String> categories) {
        List<Lot> res = new ArrayList<>();
        for(Lot elem: lots.values()){
            for(String category: categories){
                if(elem.getCategories() != null && elem.getCategories().contains(category)){
                    res.add(elem);
                }
            }
        }
        return res;
    }

    public List<Lot> getByCategories(List<String> categories) {
        List<Lot> res = new ArrayList<>();
        for(Lot elem: lots.values()){
            if(elem.getCategories() != null && elem.getCategories().containsAll(categories)){
                res.add(elem);
            }
        }
        return res;
    }

    //для Bid
    public Bid makeBid(Integer lotId, Bid bid) throws ServerException {
        getLotById(lotId).addBid(bid);
        return bid;
    }

    public void deleteBid(Integer lotId, String buyerLogin) throws ServerException{
        getLotById(lotId).deleteBid(buyerLogin);
    }

    //для купленных лотов
    public void addBuyedLot(Integer lotId, String buyerLogin) {
        buyedLots.put(lotId,buyerLogin);
    }

    public String getLotBuyer(Integer lotId) {
        return buyedLots.get(lotId);
    }


}
