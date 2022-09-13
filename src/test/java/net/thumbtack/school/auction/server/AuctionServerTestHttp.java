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
import net.thumbtack.school.auction.exception.ServerErrorCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuctionServerTestHttp extends TestBase {

    @Test
    void auctionEndTest() {
        String tokenB1 = registerBuyer("Jackie01", "123456", "Jackie",
                "Chan", null, ServerErrorCode.SUCCESS).getToken();
        String tokenB2 = registerBuyer("NotJackie01", "123456", "NotJackie",
                "NotChan", null, ServerErrorCode.SUCCESS).getToken();
        String tokenS1 = registerSeller("Frodo98", "123456", "Frodo",
                "Baggins", "Ivanovich", ServerErrorCode.SUCCESS).getToken();
        String tokenS2 = registerSeller("NotFrodo98", "123456", "NotFrodo",
                "NotBaggins", "NotIvanovich", ServerErrorCode.SUCCESS).getToken();
        addLot(tokenS1,"Помидоры", "Помидоры обыкновенные", 30,
                50, 100, null, ServerErrorCode.SUCCESS);
        addLot(tokenS2, "Огурцы", "Огурцы обыкновенные", 20,
                40, 90, null, ServerErrorCode.SUCCESS);
        addLot(tokenS2,"Тыква", "Большая жёлтая тыква", 50,
                70, 200, null, ServerErrorCode.SUCCESS);
        addLot(tokenS1,"Помидоры", "Помидоры обыкновенные", 30,
                50, 100, null, ServerErrorCode.SUCCESS);
        String tokenA = login("admin", "admin", ServerErrorCode.SUCCESS).getToken();
        List<Integer> lotsId = getLots(tokenS1, ServerErrorCode.SUCCESS).getLotsId();

        //auctionEnd
        auctionEnd("ddd", lotsId.get(0), ServerErrorCode.SESSION_EXPIRED);
        auctionEnd(tokenB1, lotsId.get(0), ServerErrorCode.WRONG_USER_TYPE);
        auctionEnd(tokenA, 99999, ServerErrorCode.LOT_NOT_FOUND);
        makeBid(tokenB1, lotsId.get(0), 120,ServerErrorCode.SUCCESS);
        auctionEnd(tokenA, lotsId.get(0), ServerErrorCode.LOT_WAS_SOLD);
        stopBids(lotsId.get(1), tokenS2, ServerErrorCode.SUCCESS);
        auctionEnd(tokenA, lotsId.get(1), ServerErrorCode.LOT_NOT_SELLING);
        makeBid(tokenB1, lotsId.get(2), 60, ServerErrorCode.SUCCESS);
        auctionEnd(tokenA, lotsId.get(2), ServerErrorCode.TOO_SMALL_CURRENT_PRICE);
        makeBid(tokenB1, lotsId.get(3), 40, ServerErrorCode.SUCCESS);
        makeBid(tokenB2, lotsId.get(3), 55, ServerErrorCode.SUCCESS);
        makeBid(tokenB2, lotsId.get(3), 70, ServerErrorCode.SUCCESS);
        makeBid(tokenB1, lotsId.get(3), 72, ServerErrorCode.SUCCESS);
        makeBid(tokenB2, lotsId.get(3), 75, ServerErrorCode.SUCCESS);
        auctionEnd(tokenA, lotsId.get(3), ServerErrorCode.SUCCESS);
        String res = getLots(tokenB1, ServerErrorCode.SUCCESS).getLotsStatusForBuyer();
        assertEquals("Лот:Вы купили этот лот по цене 120 рублей.Лот:Вы не делали ставок на данный лот.Лот:Вы являетесь текущим покупателем. Ваша ставка состовляет 60 рублей.Лот:Этот лот был продан по цене 75 рублей."
                ,res);
    }
}
