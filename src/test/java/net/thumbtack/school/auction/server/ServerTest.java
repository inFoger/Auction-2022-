package net.thumbtack.school.auction.server;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dto.request.bid.BidDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.AddLotDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.DeleteStopResumeLotDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.GetAllLotsDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.lot.GetLotsDtoResponse;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest extends TestBase{
    String res;
    AuctionServer server = new AuctionServer();
    Gson gson = new Gson();

    @Test
    void stopStartServer() {
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
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1,"Помидоры", "Помидоры обыкновенные", 30, 50, 100, AddLotDtoRequest.enterCategory("продукты", "овощи"))));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS2, "Огурцы", "Огурцы обыкновенные", 20, 40, 90, AddLotDtoRequest.enterCategory("продукты", "овощи"))));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS2,"Тыква", "Большая жёлтая тыква", 50, 70, 200, null)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.addLot(gson.toJson(new AddLotDtoRequest(tokenS1,"Помидоры", "Помидоры обыкновенные", 30, 50, 100, AddLotDtoRequest.enterCategory("продукты", "овощи"))));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenS1)));
        List<Integer> lotsId = gson.fromJson(res, GetLotsDtoResponse.class).getLotsId();
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 31)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(0), 32)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(0), 200)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB1, lotsId.get(1), 35)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.makeBid(gson.toJson(new BidDtoRequest(tokenB2, lotsId.get(2), 54)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.stopBids(gson.toJson(new DeleteStopResumeLotDtoRequest(lotsId.get(3), tokenS1)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB1)));
        String stateLots = gson.fromJson(res, GetLotsDtoResponse.class).getLots();
        //Как сделать сохранение в файл и считывание из него с mysql ?
//        server.stopServer("saveData.json");
//        DatabaseDao databaseDao = Settings.getDatabaseDao();
//        databaseDao.clear();
//        server.startServer("saveData.json");
//        res = server.getLots(gson.toJson(new GetAllLotsDtoRequest(tokenB1)));
//        assertEquals(stateLots, gson.fromJson(res, GetLotsDtoResponse.class).getLots());
    }
}