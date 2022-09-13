package net.thumbtack.school.auction.server;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dto.request.bid.BidDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.AddLotDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.threads.NewBidAdding;
import net.thumbtack.school.auction.threads.NewBuyerRegistering;
import net.thumbtack.school.auction.threads.NewLotAdding;
import net.thumbtack.school.auction.threads.NewSellerRegistering;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiThreadsTestHttp extends TestBase {
    AuctionServer server = new AuctionServer();

    @Test
    void buyerRegistering() {

        NewBuyerRegistering buyerRegistering1 = new NewBuyerRegistering(server, new RegisterUserDtoRequest
                ("Jackie01", "123456", "Jackie1", "Chan", null));
        NewBuyerRegistering buyerRegistering2 = new NewBuyerRegistering(server, new RegisterUserDtoRequest
                ("Jackie02", "1234567", "Jackie2", "Chan", null));
        NewBuyerRegistering buyerRegistering3 = new NewBuyerRegistering(server, new RegisterUserDtoRequest
                ("Jackie03", "12345678", "Jackie3", "Chan", null));
        NewBuyerRegistering buyerRegistering4 = new NewBuyerRegistering(server, new RegisterUserDtoRequest
                ("Jackie04", "123456789", "Jackie4", "Chan", null));
        NewBuyerRegistering buyerRegistering5 = new NewBuyerRegistering(server, new RegisterUserDtoRequest
                ("Jackie05", "1234567890", "Jackie5", "Chan", null));

        buyerRegistering1.start();
        buyerRegistering2.start();
        buyerRegistering3.start();
        buyerRegistering4.start();
        buyerRegistering5.start();

        try {
            buyerRegistering1.join();
            buyerRegistering2.join();
            buyerRegistering3.join();
            buyerRegistering4.join();
            buyerRegistering5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        assertNotEquals(0, getUserInfo(buyerRegistering1.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());
        assertNotEquals(0, getUserInfo(buyerRegistering2.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());
        assertNotEquals(0, getUserInfo(buyerRegistering3.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());
        assertNotEquals(0, getUserInfo(buyerRegistering4.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());
        assertNotEquals(0, getUserInfo(buyerRegistering5.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());

    }

    @Test
    void sellerRegistering() {

        NewSellerRegistering sellerRegistering1 = new NewSellerRegistering(server, new RegisterUserDtoRequest
                ("Jackie01", "123456", "Jackie1", "Chan", null));
        NewSellerRegistering sellerRegistering2 = new NewSellerRegistering(server, new RegisterUserDtoRequest
                ("Jackie02", "1234567", "Jackie2", "Chan", null));
        NewSellerRegistering sellerRegistering3 = new NewSellerRegistering(server, new RegisterUserDtoRequest
                ("Jackie03", "12345678", "Jackie3", "Chan", null));
        NewSellerRegistering sellerRegistering4 = new NewSellerRegistering(server, new RegisterUserDtoRequest
                ("Jackie04", "123456789", "Jackie4", "Chan", null));
        NewSellerRegistering sellerRegistering5 = new NewSellerRegistering(server, new RegisterUserDtoRequest
                ("Jackie05", "1234567890", "Jackie5", "Chan", null));

        sellerRegistering1.start();
        sellerRegistering2.start();
        sellerRegistering3.start();
        sellerRegistering4.start();
        sellerRegistering5.start();

        try {
            sellerRegistering1.join();
            sellerRegistering2.join();
            sellerRegistering3.join();
            sellerRegistering4.join();
            sellerRegistering5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        assertNotEquals(0, getUserInfo(sellerRegistering1.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());
        assertNotEquals(0, getUserInfo(sellerRegistering2.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());
        assertNotEquals(0, getUserInfo(sellerRegistering3.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());
        assertNotEquals(0, getUserInfo(sellerRegistering4.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());
        assertNotEquals(0, getUserInfo(sellerRegistering5.getResponse().getToken(),
                ServerErrorCode.SUCCESS).getId());

    }

    @Test
    void lotAdding() {
        String tokenS1 = registerSeller("Frodo98", "123456", "Frodo",
                "Baggins", "Ivanovich", ServerErrorCode.SUCCESS).getToken();
        String tokenS2 = registerSeller("Frodo99", "123456", "Frodo2",
                "Baggins", "Ivanovich", ServerErrorCode.SUCCESS).getToken();
        String tokenS3 = registerSeller("Frodo100", "123456", "Frodo3",
                "Baggins", "Ivanovich", ServerErrorCode.SUCCESS).getToken();

        NewLotAdding lotAdding1 = new NewLotAdding(server, new AddLotDtoRequest(
                tokenS1,"Помидоры1", "Помидоры обыкновенные1", 31, 51,
                101, AddLotDtoRequest.enterCategory("продукты", "овощи")));
        NewLotAdding lotAdding2 = new NewLotAdding(server, new AddLotDtoRequest(
                tokenS1,"Помидоры2", "Помидоры обыкновенные2", 32, 52,
                102, AddLotDtoRequest.enterCategory("продукты", "овощи")));
        NewLotAdding lotAdding3 = new NewLotAdding(server, new AddLotDtoRequest(
                tokenS1,"Помидоры3", "Помидоры обыкновенные3", 33, 53,
                103, AddLotDtoRequest.enterCategory("продукты", "овощи")));
        NewLotAdding lotAdding4 = new NewLotAdding(server, new AddLotDtoRequest(
                tokenS2,"Помидоры4", "Помидоры обыкновенные1", 34, 54,
                104, AddLotDtoRequest.enterCategory("продукты", "овощи")));
        NewLotAdding lotAdding5 = new NewLotAdding(server, new AddLotDtoRequest(
                tokenS2,"Помидоры5", "Помидоры обыкновенные2", 35, 55,
                105, AddLotDtoRequest.enterCategory("продукты", "овощи")));
        NewLotAdding lotAdding6 = new NewLotAdding(server, new AddLotDtoRequest(
                tokenS3,"Помидоры6", "Помидоры обыкновенные3", 36, 56,
                106, AddLotDtoRequest.enterCategory("продукты", "овощи")));

        lotAdding1.start();
        lotAdding2.start();
        lotAdding3.start();
        lotAdding4.start();
        lotAdding5.start();
        lotAdding6.start();

        try {
            lotAdding1.join();
            lotAdding2.join();
            lotAdding3.join();
            lotAdding4.join();
            lotAdding5.join();
            lotAdding6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(6, getLots(tokenS1, ServerErrorCode.SUCCESS).getLotsId().size());

    }

    @Test
    void makeBid() {
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

        List<Integer> lotsId = getLots(tokenS1, ServerErrorCode.SUCCESS).getLotsId();

        NewBidAdding bidAddingOne1 = new NewBidAdding(server, new BidDtoRequest(tokenB1, lotsId.get(0), 31));
        NewBidAdding bidAddingOne2 = new NewBidAdding(server, new BidDtoRequest(tokenB1, lotsId.get(0), 33));
        NewBidAdding bidAddingOne3 = new NewBidAdding(server, new BidDtoRequest(tokenB2, lotsId.get(0), 34));
        NewBidAdding bidAddingOne4 = new NewBidAdding(server, new BidDtoRequest(tokenB2, lotsId.get(0), 37));
        NewBidAdding bidAddingOne5 = new NewBidAdding(server, new BidDtoRequest(tokenB1, lotsId.get(0), 40));
        NewBidAdding bidAddingOne6 = new NewBidAdding(server, new BidDtoRequest(tokenB1, lotsId.get(0), 50));
        NewBidAdding bidAddingOne7 = new NewBidAdding(server, new BidDtoRequest(tokenB2, lotsId.get(0), 51));
        NewBidAdding bidAddingOne8 = new NewBidAdding(server, new BidDtoRequest(tokenB2, lotsId.get(0), 120));

        NewBidAdding bidAddingTwo1 = new NewBidAdding(server, new BidDtoRequest(tokenB1, lotsId.get(1), 31));
        NewBidAdding bidAddingTwo2 = new NewBidAdding(server, new BidDtoRequest(tokenB1, lotsId.get(1), 33));
        NewBidAdding bidAddingTwo3 = new NewBidAdding(server, new BidDtoRequest(tokenB2, lotsId.get(1), 34));
        NewBidAdding bidAddingTwo4 = new NewBidAdding(server, new BidDtoRequest(tokenB2, lotsId.get(1), 37));
        NewBidAdding bidAddingTwo5 = new NewBidAdding(server, new BidDtoRequest(tokenB1, lotsId.get(1), 40));
        NewBidAdding bidAddingTwo6 = new NewBidAdding(server, new BidDtoRequest(tokenB1, lotsId.get(1), 50));
        NewBidAdding bidAddingTwo7 = new NewBidAdding(server, new BidDtoRequest(tokenB2, lotsId.get(1), 51));
        NewBidAdding bidAddingTwo8 = new NewBidAdding(server, new BidDtoRequest(tokenB1, lotsId.get(1), 120));



        try {
            bidAddingOne1.start();
            bidAddingOne2.start();
            bidAddingTwo1.start();
            bidAddingTwo2.start();
            Thread.sleep(100);
            bidAddingOne3.start();
            bidAddingOne4.start();
            bidAddingTwo3.start();
            bidAddingTwo4.start();
            Thread.sleep(100);
            bidAddingOne5.start();
            bidAddingOne6.start();
            bidAddingOne7.start();
            bidAddingTwo5.start();
            bidAddingTwo6.start();
            bidAddingTwo7.start();
            Thread.sleep(100);
            bidAddingOne8.start();
            bidAddingTwo8.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        try {
            bidAddingOne1.join();
            bidAddingOne2.join();
            bidAddingOne3.join();
            bidAddingOne4.join();
            bidAddingOne5.join();
            bidAddingOne6.join();
            bidAddingOne7.join();
            bidAddingOne8.join();
            Thread.sleep(1000);
            bidAddingTwo1.join();
            bidAddingTwo2.join();
            bidAddingTwo3.join();
            bidAddingTwo4.join();
            bidAddingTwo5.join();
            bidAddingTwo6.join();
            bidAddingTwo7.join();
            bidAddingTwo8.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals("Лот:Лот был продан по обязательной ценеЛот:Вы купили этот лот по цене 120 рублей."
                , getLots(tokenB1, ServerErrorCode.SUCCESS).getLotsStatusForBuyer());
        assertEquals("Лот:Вы купили этот лот по цене 120 рублей.Лот:Лот был продан по обязательной цене"
                , getLots(tokenB2, ServerErrorCode.SUCCESS).getLotsStatusForBuyer());

    }
}
