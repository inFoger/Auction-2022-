package net.thumbtack.school.auction.server;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dto.request.bid.BidDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.AddLotDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.DeleteStopResumeLotDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.GetAllLotsDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.dto.response.lot.GetLotsDtoResponse;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BidServerTest extends TestBase{
    Gson gson = new Gson();
    AuctionServer server = new AuctionServer();
    String res;
    //buyer 1: "Jackie01", "123456", "Jackie", "Chan", null
    //buyer 2: "NotJackie01", "123456", "NotJackie", "NotChan", null
    //seller 1: "Frodo98", "123456", "Frodo", "Baggins", "Ivanovich"
    //seller 2: "NotFrodo98", "123456", "NotFrodo", "NotBaggins", "NotIvanovich"
    //lot 1: seller1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null
    //lot 2: seller2, "Огурцы", "Огурцы обыкновенные", 20, 40, 90, null
    //lot 3: seller2, "Тыква", "Большая жёлтая тыква", 50, 70, 200, null
    //lot 4: seller1, "Помидоры", "Помидоры обыкновенные", 30, 50, 100, null

    @Test
    void bidTest() {
        res = server.registerBuyer(gson.toJson(new RegisterUserDtoRequest("Jackie01", "123456",
                "Jackie", "Chan", null)));
        String tokenB1 = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.registerBuyer(gson.toJson(new RegisterUserDtoRequest("NotJackie01", "123456",
                "NotJackie", "NotChan", null)));
        String tokenB2 = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.registerSeller(gson.toJson(new RegisterUserDtoRequest("Frodo98", "123456",
                "Frodo", "Baggins", "Ivanovich")));
        String tokenS1 = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.registerSeller(gson.toJson(new RegisterUserDtoRequest("NotFrodo98", "123456",
                "NotFrodo", "NotBaggins", "NotIvanovich")));
        String tokenS2 = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1,"Помидоры", "Помидоры обыкновенные", 30, 50, 100, null)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS2, "Огурцы", "Огурцы обыкновенные", 20, 40, 90, null)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS2,"Тыква", "Большая жёлтая тыква", 50, 70, 200, null)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1,"Помидоры", "Помидоры обыкновенные", 30, 50, 100, null)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenS1)));
        List<Integer> lotsId = gson.fromJson(res, GetLotsDtoResponse.class).getLotsId();


        //makeBid
        res = server.makeBid(gson.toJson(new BidDtoRequest("ddd", lotsId.get(0) , 30))); //Сессия истекла
        assertEquals("Session expired", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenS1, lotsId.get(0), 30))); //Неверный тип пользователя
        assertEquals("Wrong user type", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, 99999, 30)));//Неверный id лота
        assertEquals("Lot with current id not found", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 29)));//Слишком маленькая ставка
        assertEquals("Too small bid", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 30)));//Всё хорошо
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB1)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=30;  ]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 30)));//Слишком маленькая ставка
        assertEquals("Too small bid", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(0), 30)));//Слишком маленькая ставка
        assertEquals("Too small bid", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 31)));//Всё хорошо
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(0), 32)));//Всё хорошо
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 40)));//Всё хорошо
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB1)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=40, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=40;  buyer=NotJackie01, price=32;  ]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        res = server.stopBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(0), tokenS1)));//остановили ставки
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 41)));//попытка ставки на непродаваемый лот
        assertEquals("Lot not selling now", gson.fromJson(res,ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(1), 41)));//всё хорошо, ставка на другой лот
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.resumeBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(0), tokenS1)));//продолжили принимать ставки
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 41)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.stopBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(0), tokenS1)));//остановили ставки
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(0), 42)));//попытка ставки на непродаваемый лот
        assertEquals("Lot not selling now", gson.fromJson(res,ErrorResponse.class).getErrorCode());
        res = server.resumeBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(0), tokenS1)));//продолжили принимать ставки
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(0), 99)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 100)));//предложение цены абсолютной продажи
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 100)));//Попытка ставки на проданный лот этим же пользователем
        assertEquals("Lot was sold", gson.fromJson(res,ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(0), 100)));//Попытка ставки на проданный лот другим пользователем
        assertEquals("Lot was sold", gson.fromJson(res,ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(1), 10000)));//всё хорошо
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(1), 100))); //Попытка ставки на проданный лот
        assertEquals("Lot was sold", gson.fromJson(res,ErrorResponse.class).getErrorCode());


        //deleteBid
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(2), 100)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.deleteBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(2), 100)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB1)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        // почему у помидоров возникает 10к ?
        assertEquals("Lot{seller=Frodo98, status='SOLD', name='Помидоры', description='Помидоры обыкновенные', currentPrice=100, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=100;  buyer=NotJackie01, price=99;  ]}Lot{seller=NotFrodo98, status='SOLD', name='Огурцы', description='Огурцы обыкновенные', currentPrice=10000, currentBuyer=Jackie01, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[buyer=Jackie01, price=10000;  ]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=100, currentBuyer=Jackie01, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);

    }
}