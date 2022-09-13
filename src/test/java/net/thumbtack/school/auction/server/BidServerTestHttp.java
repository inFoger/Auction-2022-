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
import net.thumbtack.school.auction.exception.ServerErrorCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BidServerTestHttp extends TestBase {
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
        List<Integer> lotsId = getLots(tokenS1, ServerErrorCode.SUCCESS).getLotsId();


        //makeBid
        makeBid("ddd", lotsId.get(0) , 30, ServerErrorCode.SESSION_EXPIRED); //Сессия истекла
        makeBid(tokenS1, lotsId.get(0), 30, ServerErrorCode.WRONG_USER_TYPE); //Неверный тип пользователя
        makeBid(tokenB1, 99999, 30, ServerErrorCode.LOT_NOT_FOUND);//Неверный id лота
        makeBid(tokenB1, lotsId.get(0), 29, ServerErrorCode.TOO_SMALL_BID);//Слишком маленькая ставка
        makeBid(tokenB1, lotsId.get(0), 30, ServerErrorCode.SUCCESS);//Всё хорошо
        String res = getLots(tokenB1, ServerErrorCode.SUCCESS).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=30;  ]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        makeBid(tokenB1, lotsId.get(0), 30, ServerErrorCode.TOO_SMALL_BID);//Слишком маленькая ставка
        makeBid(tokenB2, lotsId.get(0), 30, ServerErrorCode.TOO_SMALL_BID);//Слишком маленькая ставка
        makeBid(tokenB1, lotsId.get(0), 31, ServerErrorCode.SUCCESS);//Всё хорошо
        makeBid(tokenB2, lotsId.get(0), 32, ServerErrorCode.SUCCESS);//Всё хорошо
        makeBid(tokenB1, lotsId.get(0), 40, ServerErrorCode.SUCCESS);//Всё хорошо
        res = getLots(tokenB1, ServerErrorCode.SUCCESS).getLots();
        assertEquals("Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=40, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=40;  buyer=NotJackie01, price=32;  ]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Огурцы', description='Огурцы обыкновенные', currentPrice=20, currentBuyer=null, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=50, currentBuyer=null, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);
        stopBids(lotsId.get(0), tokenS1, ServerErrorCode.SUCCESS);//остановили ставки
        makeBid(tokenB1, lotsId.get(0), 41, ServerErrorCode.LOT_NOT_SELLING);//попытка ставки на непродаваемый лот
        makeBid(tokenB1, lotsId.get(1), 41, ServerErrorCode.SUCCESS);//всё хорошо, ставка на другой лот
        resumeBids(lotsId.get(0), tokenS1, ServerErrorCode.SUCCESS);//продолжили принимать ставки
        makeBid(tokenB1, lotsId.get(0), 41, ServerErrorCode.SUCCESS);
        stopBids(lotsId.get(0), tokenS1, ServerErrorCode.SUCCESS);//остановили ставки
        makeBid(tokenB2, lotsId.get(0), 42, ServerErrorCode.LOT_NOT_SELLING);//попытка ставки на непродаваемый лот
        resumeBids(lotsId.get(0), tokenS1, ServerErrorCode.SUCCESS);//продолжили принимать ставки
        makeBid(tokenB2, lotsId.get(0), 99, ServerErrorCode.SUCCESS);
        makeBid(tokenB1, lotsId.get(0), 100, ServerErrorCode.SUCCESS);//предложение цены абсолютной продажи
        makeBid(tokenB1, lotsId.get(0), 100, ServerErrorCode.LOT_WAS_SOLD);//Попытка ставки на проданный лот этим же пользователем
        makeBid(tokenB2, lotsId.get(0), 100, ServerErrorCode.LOT_WAS_SOLD);//Попытка ставки на проданный лот другим пользователем
        makeBid(tokenB1, lotsId.get(1), 10000, ServerErrorCode.SUCCESS);//всё хорошо
        makeBid(tokenB1, lotsId.get(1), 100, ServerErrorCode.LOT_WAS_SOLD); //Попытка ставки на проданный лот

        //deleteBid
        makeBid(tokenB1, lotsId.get(2), 100, ServerErrorCode.SUCCESS);
        deleteBid(tokenB1, lotsId.get(2), 100, ServerErrorCode.SUCCESS);
        res = getLots(tokenB1, ServerErrorCode.SUCCESS).getLots();
        assertEquals("Lot{seller=Frodo98, status='SOLD', name='Помидоры', description='Помидоры обыкновенные', currentPrice=100, currentBuyer=Jackie01, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[buyer=Jackie01, price=100;  buyer=NotJackie01, price=99;  ]}Lot{seller=NotFrodo98, status='SOLD', name='Огурцы', description='Огурцы обыкновенные', currentPrice=10000, currentBuyer=Jackie01, minSellingPrice=40, compulsorySalePrice=90, categories=[], bids=[buyer=Jackie01, price=10000;  ]}Lot{seller=NotFrodo98, status='FOR_SALE', name='Тыква', description='Большая жёлтая тыква', currentPrice=100, currentBuyer=Jackie01, minSellingPrice=70, compulsorySalePrice=200, categories=[], bids=[]}Lot{seller=Frodo98, status='FOR_SALE', name='Помидоры', description='Помидоры обыкновенные', currentPrice=30, currentBuyer=null, minSellingPrice=50, compulsorySalePrice=100, categories=[], bids=[]}"
                , res);

    }
}
