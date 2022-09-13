package net.thumbtack.school.auction.server;

import com.google.gson.Gson;
import net.thumbtack.school.auction.database.Database;
import net.thumbtack.school.auction.dto.request.auction.AuctionEndDtoRequest;
import net.thumbtack.school.auction.dto.request.bid.BidDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.*;
import net.thumbtack.school.auction.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.dto.response.lot.GetLotsDtoResponse;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import net.thumbtack.school.auction.model.enums.LotStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LotServerTest extends TestBase{
    Gson gson = new Gson();
    Database database = Database.getInstance();
    AuctionServer server = new AuctionServer();
    String res;
    //seller 1: "Frodo98", "123456", "Frodo", "Baggins", "Ivanovich"
    //seller 2: "NotFrodo98", "123456", "NotFrodo", "NotBaggins", "NotIvanovich"
    //buyer: "Jackie01", "123456", "Jackie", "Chan", null
    //lot 1: seller1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null
    //lot 2:seller1, "Огурцы", "Огурцы обыкновенные", 20, 40, 90, null
    //lot 3:seller2, "Тыква", "Большая жёлтая тыква", 50, 70, 200, null
    //lot 4: seller1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null

    @Test
    void lotTest() {
        res = server.registerSeller(gson.toJson(
                new RegisterUserDtoRequest("Frodo98", "123456", "Frodo",
                        "Baggins", "Ivanovich")));
        String tokenS1 = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.registerSeller(gson.toJson(new RegisterUserDtoRequest("NotFrodo98", "123456",
                "NotFrodo", "NotBaggins", "NotIvanovich")));
        String tokenS2 = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.registerBuyer(gson.toJson(new RegisterUserDtoRequest("Jackie01", "123456",
                "Jackie", "Chan", null)));
        String tokenB = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        List<Integer> lotsId;


        //добавление
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1, " ", null, -3, 3,
                3,null)));//некорректные поля
        assertEquals("Fields entered incorrectly", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenB, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null))); //неверный тип пользователя
        assertEquals("Wrong user type", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.addLot(gson.toJson(new AddLotDtoRequest("ddd", "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null))); //некорректная сессия
        assertEquals("Session expired", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null))); //некорректная сессия
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1, "Огурцы", "Огурцы обыкновенные", 20, 40, 90, null)));  //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS2, "Тыква", "Большая жёлтая тыква", 50, 70, 200, null)));  //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null))); //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());


        //получение всего списка лотов
        res = server.getLots(gson.toJson(
                new GetAllLotsDtoRequest("ddd"))); //некорректная сессия
        assertEquals("Session expired",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenS1)));
        lotsId = gson.fromJson(res, GetLotsDtoResponse.class).getLotsId();
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);


        //удаление лотов
        res = server.deleteLot(gson.toJson(new DeleteStopResumeLotDtoRequest(
                lotsId.get(0), "bbb"))); //некорректная сессия
        assertEquals("Session expired",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.deleteLot(gson.toJson(new DeleteStopResumeLotDtoRequest(
                9999, tokenS1)));                                      //неверный id
        assertEquals("Lot with current id not found",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.deleteLot(gson.toJson(new DeleteStopResumeLotDtoRequest(
                lotsId.get(0) , tokenB)));                                        //неверный тип юзера
        assertEquals("Wrong user type",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.deleteLot(gson.toJson(new DeleteStopResumeLotDtoRequest(
                lotsId.get(0), tokenS2)));                                       //чужой лот
        assertEquals("Lot belongs to another seller",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.deleteLot(gson.toJson(new DeleteStopResumeLotDtoRequest(
                lotsId.get(0), tokenS1)));                                       //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        res = server.deleteLot(gson.toJson(new DeleteStopResumeLotDtoRequest(
                lotsId.get(1), tokenS1)));                                       //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.deleteLot(gson.toJson(new DeleteStopResumeLotDtoRequest(
                lotsId.get(2), tokenS2)));                                       //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.deleteLot(gson.toJson(new DeleteStopResumeLotDtoRequest(
                lotsId.get(3), tokenS1)));                                       //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("", res);


        //продолжение/остановка продажи лотов
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null))); //некорректная сессия
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1, "Огурцы", "Огурцы обыкновенные", 20, 40, 90, null)));  //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS2, "Тыква", "Большая жёлтая тыква", 50, 70, 200, null)));  //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null))); //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenS1)));
        lotsId = gson.fromJson(res, GetLotsDtoResponse.class).getLotsId();
        //основная часть проверок общая с удалением, поэтому их можно опустить
        res = server.resumeBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(0), tokenS1))); //попытка продолжить продажу продаваемого лота
        assertEquals("Wrong lot status",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.stopBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(0), tokenS1))); //всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=Frodo98, status='NOT_FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        res = server.stopBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(0), tokenS1))); //попытка остановить продажу того, что не продаётся
        assertEquals("Wrong lot status",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.resumeBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(0), tokenS1)));//всё верно
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB, lotsId.get(0), 200)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.stopBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(0), tokenS1)));//Попытка изменения купленного лота
        assertEquals("Lot was sold", gson.fromJson(res, ErrorResponse.class).getErrorCode());


        //обновление лотов
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest("ddd", lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(), "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40, 10,null)));
        assertEquals("Session expired", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenS1, 9999, "Огурцы Ерофей", LotStatus.FOR_SALE.name(), "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40, 12, null)));
        assertEquals("Lot with current id not found", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenB, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(), "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40, 12,null)));
        assertEquals("Wrong user type", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenS2, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(), "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40, 10,null)));
        assertEquals("Lot belongs to another seller", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenS1, lotsId.get(0), "Помидоры Ерофей", LotStatus.FOR_SALE.name(), "Помидоры сорта Ерофей. Цена указана за киллограмм.", 51, 101, 10,null)));
        assertEquals("Lot was sold", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenS1, lotsId.get(1), "", LotStatus.FOR_SALE.name(), "", 20, -5, 144, null)));
        assertEquals("Fields entered incorrectly", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenS1, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(), "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40, 10, null)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=Frodo98, status='SOLD', name='Помидоры', description='Помидоры обыкновенные', currentPrice=200, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=200;  ]}Lot{seller=Frodo98, status='FOR_SALE', name='Огурцы Ерофей', description='Огурцы сорта Ерофей. Цена указана за киллограмм.', currentPrice=10, currentBuyer=null, minSellingPrice=20, compulsorySalePrice=40, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB, lotsId.get(1), 35)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenS1, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(), "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 35,10, null)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
         assertEquals("Lot{seller=Frodo98, status='SOLD', name='Помидоры', description='Помидоры обыкновенные', currentPrice=200, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=200;  ]}Lot{seller=Frodo98, status='SOLD', name='Огурцы Ерофей', description='Огурцы сорта Ерофей. Цена указана за киллограмм.', currentPrice=35, currentBuyer=Jackie01, minSellingPrice=20, compulsorySalePrice=35, categories=[], bids=[buyer=Jackie01, price=35;  ]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}",
                 res);
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenS1, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(), "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40,10, null)));
        assertEquals("Lot was sold", gson.fromJson(res, ErrorResponse.class).getErrorCode());


        //работа с категориями
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenS2, lotsId.get(2), "Мандарины", LotStatus.FOR_SALE.name(), "Сладкие, ароматные. Цена указана за киллограмм.", 20, 40, 10, AddLotDtoRequest.enterCategory("продукты", "фрукты"))));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.updateLot(gson.toJson(new UpdateLotDtoRequest(tokenS1, lotsId.get(3), "RTX 3070", LotStatus.FOR_SALE.name(), "В майнинге не учавствовала(честно).", 50000, 100000,40000, AddLotDtoRequest.enterCategory("коплектующие пк", "видеокарты"))));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, AddLotDtoRequest.enterCategory("продукты", "овощи"))));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        //по одной категории
        res = server.getLotsByCategory(gson.toJson(new GetLotByCategoryDtoRequest("ddd", "продукты")));
        assertEquals("Session expired", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.getLotsByCategory(gson.toJson(new GetLotByCategoryDtoRequest(tokenB, null)));
        assertEquals("Wrong entered category", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.getLotsByCategory(gson.toJson(new GetLotByCategoryDtoRequest(tokenB, "овощи")));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[продукты, овощи], bids=[]}",
                res);
        res = server.getLotsByCategory(gson.toJson(new GetLotByCategoryDtoRequest(tokenB, "продукты")));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=NotFrodo98, status='FOR_SALE', name='Мандарины', description='Сладкие, ароматные. Цена указана за киллограмм.', currentPrice=10, currentBuyer=null, minSellingPrice=20, compulsorySalePrice=40, categories=[продукты, фрукты], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[продукты, овощи], bids=[]}"
                , res);
        res = server.getLotsByCategory(gson.toJson(new GetLotByCategoryDtoRequest(tokenB, "процессоры")));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("", res);
        //по нескольким одновременно
        res = server.getLotsByCategories(gson.toJson(new GetLotByCategoriesDtoRequest("ddd", "продукты", "овощи")));
        assertEquals("Session expired", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.getLotsByCategories(gson.toJson(new GetLotByCategoriesDtoRequest(tokenB, "п", "продукты")));
        assertEquals("Wrong entered category", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.getLotsByCategories(gson.toJson(new GetLotByCategoriesDtoRequest(tokenB, "продукты", "овощи")));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[продукты, овощи], bids=[]}"
                , res);
        res = server.getLotsByCategories(gson.toJson(new GetLotByCategoriesDtoRequest(tokenB, "продукты", "видеокарты")));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("", res);
        //хотя бы одна категория, Request вызывается один и тот же, поэтому проверки этой части можно опустить
        res = server.getLotsByAtLeastOneCategory(gson.toJson(new GetLotByCategoriesDtoRequest(tokenB, "фрукты", "видеокарты")));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=NotFrodo98, status='FOR_SALE', name='Мандарины', description='Сладкие, ароматные. Цена указана за киллограмм.', currentPrice=10, currentBuyer=null, minSellingPrice=20, compulsorySalePrice=40, categories=[продукты, фрукты], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='RTX 3070', description='В майнинге не учавствовала(честно).', currentPrice=40000, currentBuyer=null, minSellingPrice=50000, compulsorySalePrice=100000, categories=[коплектующие пк, видеокарты], bids=[]}"
                , res);
        res = server.getLotsByAtLeastOneCategory(gson.toJson(new GetLotByCategoriesDtoRequest(tokenB,"процессоры")));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("", res);

        //состояние лотов
        String status;
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB)));
        lotsId = gson.fromJson(res, GetLotsDtoResponse.class).getLotsId();
        status = gson.fromJson(res, GetLotsDtoResponse.class).getLotsStatusForBuyer();
        assertEquals("Лот:Вы купили этот лот по цене 200 рублей.Лот:Вы купили этот лот по цене 35 рублей.Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот."
            , status);
        status = gson.fromJson(res, GetLotsDtoResponse.class).getLotsStatusForAuctioneer();
        assertEquals("Лот:Лот был продан по обязательной цене пользователю Jackie01.Всего ставок 1.Все ставки:Jackie01 сделал ставку в размере 200 рублей.  Лот:Лот был продан по обязательной цене пользователю Jackie01.Всего ставок 1.Все ставки:Jackie01 сделал ставку в размере 35 рублей.  Лот:На лот ещё не было ставок. Лот:На лот ещё не было ставок. Лот:На лот ещё не было ставок. "
            , status);
        res = server.registerBuyer(gson.toJson(new RegisterUserDtoRequest("Bruce88", "3kung_fu7", "Bruce", "Lee", "Fyodorovich")));
        String tokenB2 = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(2), 38)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB2)));
        status = gson.fromJson(res, GetLotsDtoResponse.class).getLotsStatusForBuyer();
        assertEquals("Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.Лот:Вы являетесь текущим покупателем. Ваша ставка состовляет 38 рублей.Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.",
                status);
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(3), 60000)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB, lotsId.get(3), 61000)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(4), 50)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.stopBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(4), tokenS1)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB2)));
        status = gson.fromJson(res, GetLotsDtoResponse.class).getLotsStatusForBuyer();
        assertEquals("Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.Лот:Вы являетесь текущим покупателем. Ваша ставка состовляет 38 рублей.Лот:Вы сделали ставку на этот лот в размере 60000 рублей.Лот:Лот пока не продаётся",
                status);
        res = server.loginUser(gson.toJson(new LoginUserDtoRequest("admin", "admin")));
        String tokenA = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.auctionEnd(gson.toJson(new AuctionEndDtoRequest(tokenA, lotsId.get(3))));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB2)));
        status = gson.fromJson(res, GetLotsDtoResponse.class).getLotsStatusForBuyer();
        assertEquals("Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.Лот:Вы являетесь текущим покупателем. Ваша ставка состовляет 38 рублей.Лот:Этот лот был продан по цене 61000 рублей.Лот:Лот пока не продаётся",
                status);
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB)));
        status = gson.fromJson(res, GetLotsDtoResponse.class).getLotsStatusForBuyer();
        assertEquals("Лот:Вы купили этот лот по цене 200 рублей.Лот:Вы купили этот лот по цене 35 рублей.Лот:Вы не делали ставок на данный лот.Лот:Вы купили этот лот по цене 61000 рублей.Лот:Вы не делали ставок на данный лот.",
                status);
        status = gson.fromJson(res, GetLotsDtoResponse.class).getLotsStatusForAuctioneer();
        assertEquals("Лот:Лот был продан по обязательной цене пользователю Jackie01.Всего ставок 1.Все ставки:Jackie01 сделал ставку в размере 200 рублей.  Лот:Лот был продан по обязательной цене пользователю Jackie01.Всего ставок 1.Все ставки:Jackie01 сделал ставку в размере 35 рублей.  Лот:Лот продаётся. Последняя ставка составила 38 рублей.Всего ставок 1.Все ставки:Bruce88 сделал ставку в размере 38 рублей.  Лот:Лотбыл продан за 61000 рублей пользователю Jackie01.Всего ставок 2.Все ставки:Jackie01 сделал ставку в размере 61000 рублей. Bruce88 сделал ставку в размере 60000 рублей.  Лот:Лот снят с продажи.Всего ставок 1.Все ставки:Bruce88 сделал ставку в размере 50 рублей.  ",
                status);
    }

}
