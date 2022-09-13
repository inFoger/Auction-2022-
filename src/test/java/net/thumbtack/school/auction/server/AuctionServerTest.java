package net.thumbtack.school.auction.server;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dto.request.auction.AuctionEndDtoRequest;
import net.thumbtack.school.auction.dto.request.bid.BidDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.AddLotDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.DeleteStopResumeLotDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.GetAllLotsDtoRequest;
import net.thumbtack.school.auction.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.dto.response.lot.GetLotsDtoResponse;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuctionServerTest extends TestBase{
    String res;
    AuctionServer server = new AuctionServer();
    Gson gson = new Gson();

    @Test
    void auctionEndTest() {
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
        res = server.loginUser(gson.toJson(new LoginUserDtoRequest("admin", "admin")));
        String tokenA = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenS1)));
        List<Integer> lotsId = gson.fromJson(res, GetLotsDtoResponse.class).getLotsId();

        //auctionEnd
        res = server.auctionEnd(gson.toJson(new AuctionEndDtoRequest("ddd", lotsId.get(0))));
        assertEquals("Session expired", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.auctionEnd(gson.toJson(new AuctionEndDtoRequest(tokenB1, lotsId.get(0))));
        assertEquals("Wrong user type", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.auctionEnd(gson.toJson(new AuctionEndDtoRequest(tokenA, 99999)));
        assertEquals("Lot with current id not found", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 120)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.auctionEnd(gson.toJson(new AuctionEndDtoRequest(tokenA, lotsId.get(0))));
        assertEquals("Lot was sold", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.stopBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(1), tokenS2)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.auctionEnd(gson.toJson(new AuctionEndDtoRequest(tokenA, lotsId.get(1))));
        assertEquals("Lot not selling now", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        // C этой ставкой что-то тоже не так ?
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(2), 60)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.auctionEnd(gson.toJson(new AuctionEndDtoRequest(tokenA, lotsId.get(2))));
        assertEquals("Too small current price", gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(3), 40)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(3), 55)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(3), 70)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(3), 72)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(3), 75)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.auctionEnd(gson.toJson(new AuctionEndDtoRequest(tokenA, lotsId.get(3))));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB1)));
        res = gson.fromJson(res, GetLotsDtoResponse.class).getLotsStatusForBuyer();
        assertEquals("Лот:Вы купили этот лот по цене 120 рублей.Лот:Вы не делали ставок на данный лот.Лот:Вы являетесь текущим покупателем. Ваша ставка состовляет 60 рублей.Лот:Этот лот был продан по цене 75 рублей."
                ,res);
    }
}