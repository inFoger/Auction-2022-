package net.thumbtack.school.auction.server;

import net.thumbtack.school.auction.dto.request.lot.AddLotDtoRequest;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import org.junit.jupiter.api.Test;

import java.util.List;


public class ServerTestHttp extends TestBase {

    @Test
    void stopStartServer() {

        String tokenB1 = registerBuyer("Jackie01", "123456", "Jackie",
                "Chan", null, ServerErrorCode.SUCCESS).getToken();
        String tokenB2 = registerBuyer("NotJackie01", "123456", "NotJackie",
                "NotChan", null, ServerErrorCode.SUCCESS).getToken();
        String tokenS1 = registerSeller("Frodo98", "123456", "Frodo",
                "Baggins", "Ivanovich", ServerErrorCode.SUCCESS).getToken();
        String tokenS2 = registerSeller("NotFrodo98", "123456", "NotFrodo",
                "NotBaggins", "NotIvanovich", ServerErrorCode.SUCCESS).getToken();
        addLot(tokenS1,"Помидоры", "Помидоры обыкновенные", 30,
                50, 100,
                AddLotDtoRequest.enterCategory("продукты", "овощи"), ServerErrorCode.SUCCESS);
        addLot(tokenS2, "Огурцы", "Огурцы обыкновенные", 20,
                40, 90,
                AddLotDtoRequest.enterCategory("продукты", "овощи"), ServerErrorCode.SUCCESS);
        addLot(tokenS2,"Тыква", "Большая жёлтая тыква", 50,
                70, 200, null, ServerErrorCode.SUCCESS);
        addLot(tokenS1,"Помидоры", "Помидоры обыкновенные", 30,
                50, 100,
                AddLotDtoRequest.enterCategory("продукты", "овощи"), ServerErrorCode.SUCCESS);
        List<Integer> lotsId = getLots(tokenS1, ServerErrorCode.SUCCESS).getLotsId();
        makeBid(tokenB1, lotsId.get(0), 31, ServerErrorCode.SUCCESS);
        makeBid(tokenB2, lotsId.get(0), 32, ServerErrorCode.SUCCESS);
        makeBid(tokenB1, lotsId.get(0), 200, ServerErrorCode.SUCCESS);
        makeBid(tokenB1, lotsId.get(1), 35, ServerErrorCode.SUCCESS);
        makeBid(tokenB2, lotsId.get(2), 54, ServerErrorCode.SUCCESS);
        stopBids(lotsId.get(3), tokenS1, ServerErrorCode.SUCCESS);
        getLots(tokenB1, ServerErrorCode.SUCCESS);
    }
}
