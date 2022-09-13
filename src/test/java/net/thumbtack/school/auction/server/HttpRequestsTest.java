package net.thumbtack.school.auction.server;

import net.thumbtack.school.auction.dto.response.lot.GetLotsDtoResponse;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HttpRequestsTest extends TestBase{

    @Test
    void test() {
        RegisterLoginUserDtoResponse sellerResponse = registerSeller("Frodo99","my_precious", "Frodo", "Baggins",
                "Ivanovich", ServerErrorCode.SUCCESS);
        RegisterLoginUserDtoResponse buyerResponse = registerBuyer("NotFrodo99","my_precious", "NotFrodo", "NotBaggins",
                "Petrovich", ServerErrorCode.SUCCESS);
        logout(sellerResponse.getToken(), ServerErrorCode.SUCCESS);
        logout(buyerResponse.getToken(), ServerErrorCode.SUCCESS);
        sellerResponse = login("Frodo99","my_precious", ServerErrorCode.SUCCESS);
        buyerResponse = login("NotFrodo99","my_precious", ServerErrorCode.SUCCESS);
        addLot(sellerResponse.getToken(), "Помидоры", "Помидоры обыкновенные", 30,
                50, 100, null, ServerErrorCode.SUCCESS);
        addLot(sellerResponse.getToken(), "НеПомидоры", "НеПомидоры обыкновенные", 50,
                60, 160, null, ServerErrorCode.SUCCESS);

        GetLotsDtoResponse getLots = getLots(buyerResponse.getToken(), ServerErrorCode.SUCCESS);
        getLots.getLotsId();

        makeBid(buyerResponse.getToken(), 1, 120, ServerErrorCode.SUCCESS);
        makeBid(buyerResponse.getToken(), 2, 121, ServerErrorCode.SUCCESS);
    }
}
