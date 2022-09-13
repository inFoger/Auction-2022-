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
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.model.enums.LotStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LotServerTestHttp extends TestBase {
    //seller 1: "Frodo98", "123456", "Frodo", "Baggins", "Ivanovich"
    //seller 2: "NotFrodo98", "123456", "NotFrodo", "NotBaggins", "NotIvanovich"
    //buyer: "Jackie01", "123456", "Jackie", "Chan", null
    //lot 1: seller1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null
    //lot 2:seller1, "Огурцы", "Огурцы обыкновенные", 20, 40, 90, null
    //lot 3:seller2, "Тыква", "Большая жёлтая тыква", 50, 70, 200, null
    //lot 4: seller1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null

    @Test
    void lotTest() {
        String tokenS1 = registerSeller("Frodo98", "123456", "Frodo",
                "Baggins", "Ivanovich", ServerErrorCode.SUCCESS).getToken();
        String tokenS2 = registerSeller("NotFrodo98", "123456", "NotFrodo",
                "NotBaggins", "NotIvanovich", ServerErrorCode.SUCCESS).getToken();
        String tokenB = registerBuyer("Jackie01", "123456", "Jackie",
                "Chan", null, ServerErrorCode.SUCCESS).getToken();
        List<Integer> lotsId;


        //добавление
        addLot(tokenS1, " ", null, -3, 3, 3,
                null, ServerErrorCode.INCORRECTLY_FIELDS);//некорректные поля
        addLot(tokenB, "Помидоры", "Помидоры обыкновенные", 30, 50,
                100, null, ServerErrorCode.WRONG_USER_TYPE); //неверный тип пользователя
        addLot("ddd", "Помидоры", "Помидоры обыкновенные", 30, 50,
                100, null, ServerErrorCode.SESSION_EXPIRED); //некорректная сессия
        addLot(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50,
                100, null, ServerErrorCode.SUCCESS); //всё верно
        addLot(tokenS1, "Огурцы", "Огурцы обыкновенные", 20, 40,
                90, null, ServerErrorCode.SUCCESS);  //всё верно
        addLot(tokenS2, "Тыква", "Большая жёлтая тыква", 50, 70,
                200, null, ServerErrorCode.SUCCESS);  //всё верно
        addLot(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50,
                100, null, ServerErrorCode.SUCCESS); //всё верно


        //получение всего списка лотов
        getLots("ddd", ServerErrorCode.SESSION_EXPIRED); //некорректная сессия
        lotsId = getLots(tokenS1, ServerErrorCode.SUCCESS).getLotsId();
        assertEquals(4,lotsId.size());


        //удаление лотов
        deleteLot(lotsId.get(0), "bbb", ServerErrorCode.SESSION_EXPIRED);                  //некорректная сессия
        deleteLot(9999, tokenS1, ServerErrorCode.LOT_NOT_FOUND);                            //неверный id
        deleteLot(lotsId.get(0) , tokenB, ServerErrorCode.WRONG_USER_TYPE);                   //неверный тип юзера
        deleteLot(lotsId.get(0), tokenS2, ServerErrorCode.LOT_BELONGS_TO_ANOTHER_SELLER);    //чужой лот
        deleteLot(lotsId.get(0), tokenS1, ServerErrorCode.SUCCESS);                         //всё верно
        lotsId = getLots(tokenB, ServerErrorCode.SUCCESS).getLotsId();
        assertEquals(3,lotsId.size());
        deleteLot(lotsId.get(0), tokenS1, ServerErrorCode.SUCCESS);                    //всё верно
        deleteLot(lotsId.get(1), tokenS2, ServerErrorCode.SUCCESS);                   //всё верно
        deleteLot(lotsId.get(2), tokenS1, ServerErrorCode.SUCCESS);                  //всё верно
        lotsId = getLots(tokenB, ServerErrorCode.SUCCESS).getLotsId();
        assertEquals(0, lotsId.size());


        //продолжение/остановка продажи лотов
        addLot(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50,
                100, null, ServerErrorCode.SUCCESS);
        addLot(tokenS1, "Огурцы", "Огурцы обыкновенные", 20, 40,
                90, null, ServerErrorCode.SUCCESS);  //всё верно
        addLot(tokenS2, "Тыква", "Большая жёлтая тыква", 50, 70,
                200, null, ServerErrorCode.SUCCESS);  //всё верно
        addLot(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50,
                100, null, ServerErrorCode.SUCCESS); //всё верно
        lotsId = getLots(tokenS1, ServerErrorCode.SUCCESS).getLotsId();
        //основная часть проверок общая с удалением, поэтому их можно опустить
        resumeBids(lotsId.get(0), tokenS1, ServerErrorCode.WRONG_LOT_STATUS); //попытка продолжить продажу продаваемого лота
        stopBids(lotsId.get(0), tokenS1, ServerErrorCode.SUCCESS); //всё верно
        String res = getLots(tokenS1, ServerErrorCode.SUCCESS).getLots();
        assertEquals("Lot{seller=Frodo98, status='NOT_FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        stopBids(lotsId.get(0), tokenS1, ServerErrorCode.WRONG_LOT_STATUS); //попытка остановить продажу того, что не продаётся
        resumeBids(lotsId.get(0), tokenS1, ServerErrorCode.SUCCESS);//всё верно
        res = getLots(tokenB, ServerErrorCode.SUCCESS).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        makeBid(tokenB, lotsId.get(0), 200, ServerErrorCode.SUCCESS);
        stopBids(lotsId.get(0), tokenS1, ServerErrorCode.LOT_WAS_SOLD);//Попытка изменения купленного лота


        //обновление лотов
        updateLot("ddd", lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(),
                "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40,
                10,null, ServerErrorCode.SESSION_EXPIRED);
        updateLot(tokenS1, 9999, "Огурцы Ерофей", LotStatus.FOR_SALE.name(),
                "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40,
                12, null, ServerErrorCode.LOT_NOT_FOUND);
        updateLot(tokenB, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(),
                "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40,
                12,null, ServerErrorCode.WRONG_USER_TYPE);
        updateLot(tokenS2, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(),
                "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40,
                10,null, ServerErrorCode.LOT_BELONGS_TO_ANOTHER_SELLER);
        updateLot(tokenS1, lotsId.get(0), "Помидоры Ерофей", LotStatus.FOR_SALE.name(),
                "Помидоры сорта Ерофей. Цена указана за киллограмм.", 51, 101,
                10,null, ServerErrorCode.LOT_WAS_SOLD);
        updateLot(tokenS1, lotsId.get(1), "", LotStatus.FOR_SALE.name(), "", 20,
                -5, 144, null, ServerErrorCode.INCORRECTLY_FIELDS);
        updateLot(tokenS1, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(),
                "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40,
                10, null, ServerErrorCode.SUCCESS);
        res = getLots(tokenS1, ServerErrorCode.SUCCESS).getLots();
        assertEquals("Lot{seller=Frodo98, status='SOLD', name='Помидоры', description='Помидоры обыкновенные', currentPrice=200, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=200;  ]}Lot{seller=Frodo98, status='FOR_SALE', name='Огурцы Ерофей', description='Огурцы сорта Ерофей. Цена указана за киллограмм.', currentPrice=10, currentBuyer=null, minSellingPrice=20, compulsorySalePrice=40, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        makeBid(tokenB, lotsId.get(1), 35, ServerErrorCode.SUCCESS);
        updateLot(tokenS1, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(),
                "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 35,
                10, null, ServerErrorCode.SUCCESS);
        res = getLots(tokenS1, ServerErrorCode.SUCCESS).getLots();
        assertEquals("Lot{seller=Frodo98, status='SOLD', name='Помидоры', description='Помидоры обыкновенные', currentPrice=200, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=200;  ]}Lot{seller=Frodo98, status='SOLD', name='Огурцы Ерофей', description='Огурцы сорта Ерофей. Цена указана за киллограмм.', currentPrice=35, currentBuyer=Jackie01, minSellingPrice=20, compulsorySalePrice=35, categories=[], bids=[buyer=Jackie01, price=35;  ]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}",
                res);
        updateLot(tokenS1, lotsId.get(1), "Огурцы Ерофей", LotStatus.FOR_SALE.name(),
                "Огурцы сорта Ерофей. Цена указана за киллограмм.", 20, 40,
                10, null, ServerErrorCode.LOT_WAS_SOLD);


        //работа с категориями
        updateLot(tokenS2, lotsId.get(2), "Мандарины", LotStatus.FOR_SALE.name(),
                "Сладкие, ароматные. Цена указана за киллограмм.", 20, 40,
                10, AddLotDtoRequest.enterCategory("продукты", "фрукты"), ServerErrorCode.SUCCESS);
        updateLot(tokenS1, lotsId.get(3), "RTX 3070", LotStatus.FOR_SALE.name(),
                "В майнинге не учавствовала(честно).", 50000, 100000,
                40000, AddLotDtoRequest.enterCategory("коплектующие пк", "видеокарты"), ServerErrorCode.SUCCESS);
        addLot(tokenS1, "Помидоры", "Помидоры обыкновенные", 30, 50,
                100, AddLotDtoRequest.enterCategory("продукты", "овощи"), ServerErrorCode.SUCCESS);
        //по одной категории
        getLotsByCategory("ddd", "продукты", ServerErrorCode.SESSION_EXPIRED);
        getLotsByCategory(tokenB, null, ServerErrorCode.WRONG_ENTERED_CATEGORY);
        res = getLotsByCategory(tokenB, "овощи", ServerErrorCode.SUCCESS).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[продукты, овощи], bids=[]}",
                res);
        res = getLotsByCategory(tokenB, "продукты", ServerErrorCode.SUCCESS).getLots();
        assertEquals("Lot{seller=NotFrodo98, status='FOR_SALE', name='Мандарины', description='Сладкие, ароматные. Цена указана за киллограмм.', currentPrice=10, currentBuyer=null, minSellingPrice=20, compulsorySalePrice=40, categories=[продукты, фрукты], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[продукты, овощи], bids=[]}"
                , res);
        res = getLotsByCategory(tokenB, "процессоры", ServerErrorCode.SUCCESS).getLots();
        assertEquals("", res);
        //по нескольким одновременно
        getLotsByCategories("ddd", ServerErrorCode.SESSION_EXPIRED,"продукты", "овощи");
        getLotsByCategories(tokenB, ServerErrorCode.WRONG_ENTERED_CATEGORY ,"п", "продукты");
        res = getLotsByCategories(tokenB, ServerErrorCode.SUCCESS, "продукты", "овощи").getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[продукты, овощи], bids=[]}"
                , res);
        res = getLotsByCategories(tokenB, ServerErrorCode.SUCCESS,"продукты", "видеокарты").getLots();
        assertEquals("", res);
        //хотя бы одна категория, Request вызывается один и тот же, поэтому проверки этой части можно опустить
        res = getLotsByAtLeastOneCategory(tokenB, ServerErrorCode.SUCCESS,"фрукты", "видеокарты").getLots();
        assertEquals("Lot{seller=NotFrodo98, status='FOR_SALE', name='Мандарины', description='Сладкие, ароматные. Цена указана за киллограмм.', currentPrice=10, currentBuyer=null, minSellingPrice=20, compulsorySalePrice=40, categories=[продукты, фрукты], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='RTX 3070', description='В майнинге не учавствовала(честно).', currentPrice=40000, currentBuyer=null, minSellingPrice=50000, compulsorySalePrice=100000, categories=[коплектующие пк, видеокарты], bids=[]}"
                , res);
        res = getLotsByAtLeastOneCategory(tokenB, ServerErrorCode.SUCCESS,"процессоры").getLots();
        assertEquals("", res);

        //состояние лотов
        String status;
        GetLotsDtoResponse lotsResp = getLots(tokenB, ServerErrorCode.SUCCESS);
        lotsId = lotsResp.getLotsId();
        status = lotsResp.getLotsStatusForBuyer();
        assertEquals("Лот:Вы купили этот лот по цене 200 рублей.Лот:Вы купили этот лот по цене 35 рублей.Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот."
                , status);
        status = lotsResp.getLotsStatusForAuctioneer();
        assertEquals("Лот:Лот был продан по обязательной цене пользователю Jackie01.Всего ставок 1.Все ставки:Jackie01 сделал ставку в размере 200 рублей.  Лот:Лот был продан по обязательной цене пользователю Jackie01.Всего ставок 1.Все ставки:Jackie01 сделал ставку в размере 35 рублей.  Лот:На лот ещё не было ставок. Лот:На лот ещё не было ставок. Лот:На лот ещё не было ставок. "
                , status);
        String tokenB2 = registerBuyer("Bruce88", "3kung_fu7", "Bruce", "Lee",
                "Fyodorovich", ServerErrorCode.SUCCESS).getToken();
        makeBid(tokenB2, lotsId.get(2), 38, ServerErrorCode.SUCCESS);
        lotsResp = getLots(tokenB2, ServerErrorCode.SUCCESS);
        status = lotsResp.getLotsStatusForBuyer();
        assertEquals("Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.Лот:Вы являетесь текущим покупателем. Ваша ставка состовляет 38 рублей.Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.",
                status);
        makeBid(tokenB2, lotsId.get(3), 60000, ServerErrorCode.SUCCESS);
        makeBid(tokenB, lotsId.get(3), 61000, ServerErrorCode.SUCCESS);
        makeBid(tokenB2, lotsId.get(4), 50, ServerErrorCode.SUCCESS);
        stopBids(lotsId.get(4), tokenS1, ServerErrorCode.SUCCESS);
        lotsResp = getLots(tokenB2, ServerErrorCode.SUCCESS);
        status = lotsResp.getLotsStatusForBuyer();
        assertEquals("Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.Лот:Вы являетесь текущим покупателем. Ваша ставка состовляет 38 рублей.Лот:Вы сделали ставку на этот лот в размере 60000 рублей.Лот:Лот пока не продаётся",
                status);
        String tokenA = login("admin", "admin", ServerErrorCode.SUCCESS).getToken();
        auctionEnd(tokenA, lotsId.get(3), ServerErrorCode.SUCCESS);
        lotsResp = getLots(tokenB2, ServerErrorCode.SUCCESS);
        status = lotsResp.getLotsStatusForBuyer();
        assertEquals("Лот:Вы не делали ставок на данный лот.Лот:Вы не делали ставок на данный лот.Лот:Вы являетесь текущим покупателем. Ваша ставка состовляет 38 рублей.Лот:Этот лот был продан по цене 61000 рублей.Лот:Лот пока не продаётся",
                status);
        lotsResp = getLots(tokenB, ServerErrorCode.SUCCESS);
        status = lotsResp.getLotsStatusForBuyer();
        assertEquals("Лот:Вы купили этот лот по цене 200 рублей.Лот:Вы купили этот лот по цене 35 рублей.Лот:Вы не делали ставок на данный лот.Лот:Вы купили этот лот по цене 61000 рублей.Лот:Вы не делали ставок на данный лот.",
                status);
        status = lotsResp.getLotsStatusForAuctioneer();
        assertEquals("Лот:Лот был продан по обязательной цене пользователю Jackie01.Всего ставок 1.Все ставки:Jackie01 сделал ставку в размере 200 рублей.  Лот:Лот был продан по обязательной цене пользователю Jackie01.Всего ставок 1.Все ставки:Jackie01 сделал ставку в размере 35 рублей.  Лот:Лот продаётся. Последняя ставка составила 38 рублей.Всего ставок 1.Все ставки:Bruce88 сделал ставку в размере 38 рублей.  Лот:Лотбыл продан за 61000 рублей пользователю Jackie01.Всего ставок 2.Все ставки:Jackie01 сделал ставку в размере 61000 рублей. Bruce88 сделал ставку в размере 60000 рублей.  Лот:Лот снят с продажи.Всего ставок 1.Все ставки:Bruce88 сделал ставку в размере 50 рублей.  ",
                status);
    }

}
