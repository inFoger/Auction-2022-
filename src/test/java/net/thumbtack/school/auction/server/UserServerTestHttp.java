package net.thumbtack.school.auction.server;

import com.google.gson.Gson;
import net.thumbtack.school.auction.database.Database;
import net.thumbtack.school.auction.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.TokenDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.dto.response.user.GetUserInfoDtoResponse;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServerTestHttp extends TestBase {
    //"Frodo98", "123456", "Frodo", "Baggins", "Ivanovich"
    //"Jackie01", "123456", "Jackie", "Chan", null
    @Test
    void sellerScriptTest() {
        //проверка registerSeller()
        RegisterLoginUserDtoResponse sellerResp = registerSeller("Frodo98", "123456", "Frodo",
                "Baggins", "Ivanovich", ServerErrorCode.SUCCESS);
        GetUserInfoDtoResponse userInfo = getUserInfo(sellerResp.getToken(), ServerErrorCode.SUCCESS);
        assertEquals("Frodo98", userInfo.getLogin());
        assertEquals("Frodo", userInfo.getFirstName());
        assertEquals("Baggins", userInfo.getLastName());
        assertEquals("Ivanovich", userInfo.getSecondName());
        registerSeller(null, "123456", "Frodo", "Baggins",
                "Ivanovich", ServerErrorCode.VALIDATION_ERROR);
        registerSeller("Frodo98", "123456", "Frodo", "Baggins",
                "Ivanovich", ServerErrorCode.LOGIN_ALREADY_EXISTS);

        RegisterLoginUserDtoResponse sellerResp2 = registerSeller("Jackie01", "123456", //нет отчества
                "Jackie", "Chan", null, ServerErrorCode.SUCCESS);
        userInfo = getUserInfo(sellerResp2.getToken(), ServerErrorCode.SUCCESS);
        assertEquals("Jackie01", userInfo.getLogin());
        assertEquals("Jackie", userInfo.getFirstName());
        assertEquals("Chan", userInfo.getLastName());
        assertNull(userInfo.getSecondName());


        //проверка loginSeller()
        login("Frodo98","55555", ServerErrorCode.WRONG_PASSWORD); //неверный пароль
        login("NotFrodo98","55555", ServerErrorCode.USER_NOT_FOUND); //неверный логин
        RegisterLoginUserDtoResponse sellerResp3 = login("Frodo98","123456", ServerErrorCode.SUCCESS);
        userInfo =  getUserInfo(sellerResp3.getToken(), ServerErrorCode.SUCCESS);
        assertEquals("Frodo98", userInfo.getLogin());
        assertEquals("Frodo", userInfo.getFirstName());
        assertEquals("Baggins", userInfo.getLastName());
        assertEquals("Ivanovich", userInfo.getSecondName());

        //проверка logoutSeller()
        logout(sellerResp3.getToken(), ServerErrorCode.SUCCESS);
        getUserInfo(sellerResp3.getToken(), ServerErrorCode.SESSION_EXPIRED);//попытка получить по старому токену
        logout(null, ServerErrorCode.VALIDATION_ERROR);

        //проверка deleteSeller()
        delete(sellerResp3.getToken(), ServerErrorCode.SESSION_EXPIRED); //отсутсвующая ссессия
        //входим в аккаунт
        sellerResp3 = login("Frodo98","123456", ServerErrorCode.SUCCESS);
        delete(sellerResp3.getToken(), ServerErrorCode.SUCCESS);
        //проверяем, сработало ли удаление
        login("Frodo98", "123456", ServerErrorCode.USER_NOT_FOUND);
    }

    @Test
    void buyerScriptTest() {
        //проверка registerbuyer()
        RegisterLoginUserDtoResponse buyerResp = registerBuyer("Frodo98", "123456", "Frodo",
                "Baggins", "Ivanovich", ServerErrorCode.SUCCESS);
        GetUserInfoDtoResponse userInfo = getUserInfo(buyerResp.getToken(), ServerErrorCode.SUCCESS);
        assertEquals("Frodo98", userInfo.getLogin());
        assertEquals("Frodo", userInfo.getFirstName());
        assertEquals("Baggins", userInfo.getLastName());
        assertEquals("Ivanovich", userInfo.getSecondName());
        registerBuyer(null, "123456", "Frodo", "Baggins",
                "Ivanovich", ServerErrorCode.VALIDATION_ERROR);
        registerBuyer("Frodo98", "123456", "Frodo", "Baggins",
                "Ivanovich", ServerErrorCode.LOGIN_ALREADY_EXISTS);

        RegisterLoginUserDtoResponse buyerResp2 = registerBuyer("Jackie01", "123456", //нет отчества
                "Jackie", "Chan", null, ServerErrorCode.SUCCESS);
        userInfo = getUserInfo(buyerResp2.getToken(), ServerErrorCode.SUCCESS);
        assertEquals("Jackie01", userInfo.getLogin());
        assertEquals("Jackie", userInfo.getFirstName());
        assertEquals("Chan", userInfo.getLastName());
        assertNull(userInfo.getSecondName());


        //проверка loginBuyer()
        login("Frodo98","55555", ServerErrorCode.WRONG_PASSWORD); //неверный пароль
        login("NotFrodo98","55555", ServerErrorCode.USER_NOT_FOUND); //неверный логин
        RegisterLoginUserDtoResponse buyerResp3 = login("Frodo98","123456", ServerErrorCode.SUCCESS);
        userInfo =  getUserInfo(buyerResp3.getToken(), ServerErrorCode.SUCCESS);
        assertEquals("Frodo98", userInfo.getLogin());
        assertEquals("Frodo", userInfo.getFirstName());
        assertEquals("Baggins", userInfo.getLastName());
        assertEquals("Ivanovich", userInfo.getSecondName());

        //проверка logoutBuyer()
        logout(buyerResp3.getToken(), ServerErrorCode.SUCCESS);
        getUserInfo(buyerResp3.getToken(), ServerErrorCode.SESSION_EXPIRED);//попытка получить по старому токену
        logout(null, ServerErrorCode.VALIDATION_ERROR);

        //проверка deleteBuyer()
        delete(buyerResp3.getToken(), ServerErrorCode.SESSION_EXPIRED); //отсутсвующая ссессия
        //входим в аккаунт
        buyerResp3 = login("Frodo98","123456", ServerErrorCode.SUCCESS);
        delete(buyerResp3.getToken(), ServerErrorCode.SUCCESS);
        //проверяем, сработало ли удаление
        login("Frodo98", "123456", ServerErrorCode.USER_NOT_FOUND);
    }

    @Test
    void auctioneerTest() {
        login("admin","admin", ServerErrorCode.SUCCESS);
    }
}
