package net.thumbtack.school.auction.server;

import net.thumbtack.school.auction.client.AuctionClient;
import net.thumbtack.school.auction.dto.request.auction.AuctionEndDtoRequest;
import net.thumbtack.school.auction.dto.request.bid.BidDtoRequest;
import net.thumbtack.school.auction.dto.request.lot.*;
import net.thumbtack.school.auction.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.TokenDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.dto.response.lot.GetLotsDtoResponse;
import net.thumbtack.school.auction.dto.response.user.GetUserInfoDtoResponse;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.server.config.Settings;
import net.thumbtack.school.database.mybatis.utils.MyBatisUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBase {
    public static AuctionServer server = new AuctionServer();
    public static AuctionClient client = new AuctionClient();
    private static String baseURL;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestBase.class);

    protected static void setBaseUrl() {
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            LOGGER.debug("Can't determine my own host name", e);
        }
        baseURL = "http://" + hostName + ":" + Settings.getRestHTTPPort();
    }

    @BeforeAll()
    public static void setUp() {
        setBaseUrl();
        server.startServer(null);
        System.out.println("Вызов сервера");
    }

    @BeforeEach
    public void clearDatabase() {
        client.delete(baseURL + "/server/clear", EmptyResponse.class);
    }

    @AfterAll
    public static void stopServer() {
        server.stopServer(null);
    }

    public void checkErrorResponse(Object response, ServerErrorCode expectedStatus) {
        assertTrue(response instanceof ErrorResponse);
        ErrorResponse errorResponseObject = (ErrorResponse) response;
        assertEquals(expectedStatus.getErrorCode(), errorResponseObject.getErrorCode());
    }

    //user
    public RegisterLoginUserDtoResponse registerSeller(String login, String password, String firstName, String lastName, String secondName, ServerErrorCode expectedStatus) {
        RegisterUserDtoRequest request = new RegisterUserDtoRequest(login, password, firstName, lastName, secondName);
        Object response = client.post(baseURL+"/users/seller", request, RegisterLoginUserDtoResponse.class);
        if (response instanceof RegisterLoginUserDtoResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            RegisterLoginUserDtoResponse registerUserResponse = (RegisterLoginUserDtoResponse) response;
            assertNotNull(registerUserResponse.getToken());
            return registerUserResponse;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public RegisterLoginUserDtoResponse registerBuyer(String login, String password, String firstName, String lastName, String secondName, ServerErrorCode expectedStatus) {
        RegisterUserDtoRequest request = new RegisterUserDtoRequest(login, password, firstName, lastName, secondName);
        Object response = client.post(baseURL+"/users/buyer", request, RegisterLoginUserDtoResponse.class);
        if (response instanceof RegisterLoginUserDtoResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            RegisterLoginUserDtoResponse registerUserResponse = (RegisterLoginUserDtoResponse) response;
            assertNotNull(registerUserResponse.getToken());
            return registerUserResponse;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public RegisterLoginUserDtoResponse login(String login, String password, ServerErrorCode expectedStatus) {
        LoginUserDtoRequest request = new LoginUserDtoRequest(login, password);
        Object response = client.post(baseURL + "/users/login", request, RegisterLoginUserDtoResponse.class);
        if (response instanceof RegisterLoginUserDtoResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            RegisterLoginUserDtoResponse loginResponse = (RegisterLoginUserDtoResponse) response;
            assertNotNull(loginResponse.getToken());
            return loginResponse;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public EmptyResponse logout(String token, ServerErrorCode expectedStatus) {
        TokenDtoRequest request = new TokenDtoRequest(token);
        Object response = client.put(baseURL + "/users/logout", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public EmptyResponse delete(String token, ServerErrorCode expectedStatus) {
        TokenDtoRequest request = new TokenDtoRequest(token);
        Object response = client.put(baseURL + "/users/delete", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public GetUserInfoDtoResponse getUserInfo(String token, ServerErrorCode expectedStatus) {
        TokenDtoRequest request = new TokenDtoRequest(token);
        Object response = client.put(baseURL + "/users/userInfo", request, GetUserInfoDtoResponse.class);
        if (response instanceof GetUserInfoDtoResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (GetUserInfoDtoResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    //lot
    public EmptyResponse addLot(String token, String name, String description, int startPrice, int minSellingPrice, int compulsorySalePrice, List<String> categories, ServerErrorCode expectedStatus) {
        AddLotDtoRequest request = new AddLotDtoRequest(token,name,description,startPrice,minSellingPrice,compulsorySalePrice,categories);
        Object response = client.post(baseURL + "/lot/add", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public EmptyResponse updateLot(String token, Integer lotId, String name, String status, String description, int minSellingPrice, int compulsorySalePrice, int startPrice, List<String> categories, ServerErrorCode expectedStatus) {
        UpdateLotDtoRequest request = new UpdateLotDtoRequest(token, lotId, name, status, description, minSellingPrice, compulsorySalePrice, startPrice, categories);
        Object response = client.put(baseURL + "/lot/update", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public EmptyResponse deleteLot(Integer id, String token, ServerErrorCode expectedStatus) {
        DeleteStopResumeLotDtoRequest request = new DeleteStopResumeLotDtoRequest(id, token);
        Object response = client.put(baseURL + "/lot/delete", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public EmptyResponse stopBids(Integer id, String token, ServerErrorCode expectedStatus) {
        DeleteStopResumeLotDtoRequest request = new DeleteStopResumeLotDtoRequest(id, token);
        Object response = client.put(baseURL + "/lot/stopBids", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public EmptyResponse resumeBids(Integer id, String token, ServerErrorCode expectedStatus) {
        DeleteStopResumeLotDtoRequest request = new DeleteStopResumeLotDtoRequest(id,token);
        Object response = client.put(baseURL + "/lot/resumeBids", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public GetLotsDtoResponse getLots(String token, ServerErrorCode expectedStatus) {
        GetAllLotsDtoRequest request = new GetAllLotsDtoRequest(token);
        Object response = client.put(baseURL+"/lot/getAll", request, GetLotsDtoResponse.class);
        if (response instanceof GetLotsDtoResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            GetLotsDtoResponse lotsResponse = (GetLotsDtoResponse) response;
            assertNotNull(lotsResponse);
            return lotsResponse;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }

    }

    public GetLotsDtoResponse getLotsByCategory(String token, String category, ServerErrorCode expectedStatus) {
        GetLotByCategoryDtoRequest request = new GetLotByCategoryDtoRequest(token, category);
        Object response = client.put(baseURL+"/lot/getLotsByCategory", request, GetLotsDtoResponse.class);
        if (response instanceof GetLotsDtoResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            GetLotsDtoResponse lotsResponse = (GetLotsDtoResponse) response;
            assertNotNull(lotsResponse);
            return lotsResponse;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public GetLotsDtoResponse getLotsByCategories(String token, ServerErrorCode expectedStatus, String... categories) {
        GetLotByCategoriesDtoRequest request = new GetLotByCategoriesDtoRequest(token, categories);
        Object response = client.put(baseURL+"/lot/getLotsByCategories", request, GetLotsDtoResponse.class);
        if (response instanceof GetLotsDtoResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            GetLotsDtoResponse lotsResponse = (GetLotsDtoResponse) response;
            assertNotNull(lotsResponse);
            return lotsResponse;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public GetLotsDtoResponse getLotsByAtLeastOneCategory(String token, ServerErrorCode expectedStatus, String... categories) {
        GetLotByCategoriesDtoRequest request = new GetLotByCategoriesDtoRequest(token, categories);
        Object response = client.put(baseURL+"/lot/getLotsByAtLeastOneCategory", request, GetLotsDtoResponse.class);
        if (response instanceof GetLotsDtoResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            GetLotsDtoResponse lotsResponse = (GetLotsDtoResponse) response;
            assertNotNull(lotsResponse);
            return lotsResponse;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }


    //bid
    public EmptyResponse makeBid(String token, Integer lotId, Integer price, ServerErrorCode expectedStatus) {
        BidDtoRequest request = new BidDtoRequest(token,lotId,price);
        Object response = client.put(baseURL + "/bid/make", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

    public EmptyResponse deleteBid(String token, Integer lotId, Integer price, ServerErrorCode expectedStatus) {
        BidDtoRequest request = new BidDtoRequest(token,lotId,price);
        Object response = client.put(baseURL + "/bid/delete", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }


    //auction
    public EmptyResponse auctionEnd(String token, Integer lotId, ServerErrorCode expectedStatus) {
        AuctionEndDtoRequest request = new AuctionEndDtoRequest(token, lotId);
        Object response = client.put(baseURL + "/auction/end", request, EmptyResponse.class);
        if (response instanceof EmptyResponse) {
            assertEquals(ServerErrorCode.SUCCESS, expectedStatus);
            return (EmptyResponse) response;
        } else {
            checkErrorResponse(response, expectedStatus);
            return null;
        }
    }

}
