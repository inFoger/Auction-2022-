package net.thumbtack.school.auction.server;

import com.google.gson.Gson;
import net.thumbtack.school.auction.database.Database; //используется только для отчистки БД
import net.thumbtack.school.auction.dto.request.user.TokenDtoRequest;
import net.thumbtack.school.auction.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.dto.response.user.GetUserInfoDtoResponse;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServerTest extends TestBase {
    Database database = Database.getInstance();
    Gson gson = new Gson();
    AuctionServer server = new AuctionServer();
    //"Frodo98", "123456", "Frodo", "Baggins", "Ivanovich"
    //"Jackie01", "123456", "Jackie", "Chan", null
    @Test
    void sellerScriptTest() {
        clearDatabase();
        //проверка registerSeller()
        String res = server.registerSeller(gson.toJson(
                new RegisterUserDtoRequest("Frodo98", "123456", "Frodo",
                        "Baggins", "Ivanovich")));
        String token = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.getUserInfo(gson.toJson(new TokenDtoRequest(token)));
        GetUserInfoDtoResponse userInfo =  gson.fromJson(res, GetUserInfoDtoResponse.class);
        assertEquals("Frodo98", userInfo.getLogin());
        assertEquals("Frodo", userInfo.getFirstName());
        assertEquals("Baggins", userInfo.getLastName());
        assertEquals("Ivanovich", userInfo.getSecondName());
        res = server.registerSeller(gson.toJson(
                new RegisterUserDtoRequest(null, "123456",
                        "Frodo", "Baggins", "Ivanovich")));
        assertEquals("Fields entered incorrectly",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.registerSeller(gson.toJson(
                new RegisterUserDtoRequest("Frodo98", "123456", //такой логин уже существует
                        "Frodo", "Baggins", "Ivanovich")));
        assertEquals("User with this login already exists",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.registerSeller(gson.toJson(
                new RegisterUserDtoRequest("Jackie01", "123456", //нет отчества
                        "Jackie", "Chan", null)));
        token = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.getUserInfo(gson.toJson(new TokenDtoRequest(token)));
        userInfo =  gson.fromJson(res, GetUserInfoDtoResponse.class);
        assertEquals("Jackie01", userInfo.getLogin());
        assertEquals("Jackie", userInfo.getFirstName());
        assertEquals("Chan", userInfo.getLastName());
        assertNull(userInfo.getSecondName());


        //проверка loginSeller()
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("Frodo98","55555"))); //неверный пароль
        assertEquals("Wrong password",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("NotFrodo98","55555"))); //неверный логин
        assertEquals("User not found",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("Frodo98","123456")));
        token = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.getUserInfo(gson.toJson(new TokenDtoRequest(token)));
        userInfo =  gson.fromJson(res, GetUserInfoDtoResponse.class);
        assertEquals("Frodo98", userInfo.getLogin());
        assertEquals("Frodo", userInfo.getFirstName());
        assertEquals("Baggins", userInfo.getLastName());
        assertEquals("Ivanovich", userInfo.getSecondName());

        //проверка logoutSeller()
        res = server.logoutUser(gson.toJson(
                new TokenDtoRequest(token)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getUserInfo(gson.toJson(new TokenDtoRequest(token)));//попытка получить по старому токену
        assertEquals("Session expired",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.logoutUser(gson.toJson(
                new TokenDtoRequest(null)));
        assertEquals("Session expired",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());

        //проверка deleteSeller()
        res = server.deleteUser(gson.toJson(
                new TokenDtoRequest(token))); //отсутсвующая ссессия
        assertEquals("Session expired",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        //входим в аккаунт
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("Frodo98","123456")));
        token = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.deleteUser(gson.toJson(
                new TokenDtoRequest(token)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck()); //всё хорошо
        //проверяем, сработало ли удаление
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("Frodo98", "123456")));
        assertEquals("User not found",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());

    }

    @Test
    void buyerScriptTest() {
        //проверка registerBuyer()
        clearDatabase();
        String res = server.registerBuyer(gson.toJson(
                new RegisterUserDtoRequest("Frodo98", "123456", "Frodo",
                        "Baggins", "Ivanovich")));
        String token = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.getUserInfo(gson.toJson(new TokenDtoRequest(token)));
        GetUserInfoDtoResponse userInfo =  gson.fromJson(res, GetUserInfoDtoResponse.class);
        assertEquals("Frodo98", userInfo.getLogin());
        assertEquals("Frodo", userInfo.getFirstName());
        assertEquals("Baggins", userInfo.getLastName());
        assertEquals("Ivanovich", userInfo.getSecondName());
        res = server.registerBuyer(gson.toJson(
                new RegisterUserDtoRequest(null, "123456",
                        "Frodo", "Baggins", "Ivanovich")));
        assertEquals("Fields entered incorrectly",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.registerBuyer(gson.toJson(
                new RegisterUserDtoRequest("Frodo98", "123456", //такой логин уже существует
                        "Frodo", "Baggins", "Ivanovich")));
        assertEquals("User with this login already exists",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.registerBuyer(gson.toJson(
                new RegisterUserDtoRequest("Jackie01", "123456", //нет отчества
                        "Jackie", "Chan", null)));
        token = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.getUserInfo(gson.toJson(new TokenDtoRequest(token)));
        userInfo =  gson.fromJson(res, GetUserInfoDtoResponse.class);
        assertEquals("Jackie01", userInfo.getLogin());
        assertEquals("Jackie", userInfo.getFirstName());
        assertEquals("Chan", userInfo.getLastName());
        assertNull(userInfo.getSecondName());


        //проверка loginBuyer()
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("Frodo98","55555"))); //неверный пароль
        assertEquals("Wrong password",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("NotFrodo98","55555"))); //неверный логин
        assertEquals("User not found",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("Frodo98","123456")));
        token = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.getUserInfo(gson.toJson(new TokenDtoRequest(token)));
        userInfo =  gson.fromJson(res, GetUserInfoDtoResponse.class);
        assertEquals("Frodo98", userInfo.getLogin());
        assertEquals("Frodo", userInfo.getFirstName());
        assertEquals("Baggins", userInfo.getLastName());
        assertEquals("Ivanovich", userInfo.getSecondName());

        //проверка logoutBuyer()
        res = server.logoutUser(gson.toJson(
                new TokenDtoRequest(token)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck());
        res = server.getUserInfo(gson.toJson(new TokenDtoRequest(token)));//попытка получить по старому токену
        assertEquals("Session expired",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        res = server.logoutUser(gson.toJson(
                new TokenDtoRequest(null)));
        assertEquals("Session expired",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());

        //проверка deleteBuyer()
        res = server.deleteUser(gson.toJson(
                new TokenDtoRequest(token))); //отсутсвующая ссессия
        assertEquals("Session expired",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
        //входим в аккаунт
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("Frodo98","123456")));
        token = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        res = server.deleteUser(gson.toJson(
                new TokenDtoRequest(token)));
        assertTrue(gson.fromJson(res, EmptyResponse.class).getCheck()); //всё хорошо
        //проверяем, сработало ли удаление
        res = server.loginUser(gson.toJson(
                new LoginUserDtoRequest("Frodo98", "123456")));
        assertEquals("User not found",
                gson.fromJson(res, ErrorResponse.class).getErrorCode());
    }

    @Test
    void auctioneerTest() {
        clearDatabase();
        String res = server.loginUser(gson.toJson(new LoginUserDtoRequest("admin","admin")));
        String token = gson.fromJson(res, RegisterLoginUserDtoResponse.class).getToken();
        assertNotNull(token);
    }

}