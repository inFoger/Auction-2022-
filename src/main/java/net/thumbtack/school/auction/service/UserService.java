package net.thumbtack.school.auction.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.dao.UserDao;
import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dto.request.user.TokenDtoRequest;
import net.thumbtack.school.auction.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.dto.response.user.GetUserInfoDtoResponse;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.model.Session;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.users.Buyer;
import net.thumbtack.school.auction.model.users.Seller;
import net.thumbtack.school.auction.model.users.User;
import net.thumbtack.school.auction.utils.AuctionUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

public class UserService{
    private UserDao userDAO = SettingsDatabase.getUserDao();
    private SessionDao sessionDao = SettingsDatabase.getSessionDao();
    private Gson gson = new Gson();

    public static <T> T getClassInstanceFromJson(String json, Class<T> clazz) throws ServerException{
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e){
            throw new ServerException(ServerErrorCode.WRONG_JSON);
        }
    }
    //json
    public String registerSeller(String jsonString) {
        try{
            RegisterUserDtoRequest request = getClassInstanceFromJson(jsonString, RegisterUserDtoRequest.class);
            request.validate();
            Seller seller = new Seller(request.getLogin(), request.getPassword(), request.getFirstName()
                    , request.getLastName(), request.getSecondName());
            userDAO.create(seller);
            Session session = new Session(UUID.randomUUID().toString(), userDAO.getByLogin(request.getLogin()));
            sessionDao.create(session);
            return gson.toJson(new RegisterLoginUserDtoResponse(session.getToken()));
        }catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
    }

    public String registerBuyer(String jsonString) {
        try{
            RegisterUserDtoRequest request = getClassInstanceFromJson(jsonString, RegisterUserDtoRequest.class);
            request.validate();
            Buyer buyer = new Buyer(request.getLogin(), request.getPassword(), request.getFirstName()
                    , request.getLastName(), request.getSecondName());
            userDAO.create(buyer);
            Session session = new Session(UUID.randomUUID().toString(), userDAO.getByLogin(request.getLogin()));
            sessionDao.create(session);
            return gson.toJson(new RegisterLoginUserDtoResponse(session.getToken()));
        }catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }

    }

    public String login(String jsonString) {
        try{
            LoginUserDtoRequest request = getClassInstanceFromJson(jsonString, LoginUserDtoRequest.class);
            request.validate();
            Session session = new Session(UUID.randomUUID().toString(), userDAO.getByLogin(request.getLogin()));
            sessionDao.create(session);
            return gson.toJson(new RegisterLoginUserDtoResponse(session.getToken()));
        }catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
    }

    public String logout(String jsonString) {
        try{
            TokenDtoRequest request = getClassInstanceFromJson(jsonString, TokenDtoRequest.class);
            request.validate();
            sessionDao.delete(request.getToken());
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    public String delete(String jsonString) {
        try{
            TokenDtoRequest request = getClassInstanceFromJson(jsonString, TokenDtoRequest.class);
            request.validate();
            userDAO.delete(sessionDao.getSession(request.getToken()).getUser().getLogin());
            sessionDao.delete(request.getToken());
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    public String getUserInfo(String jsonString){
        try{
            TokenDtoRequest request = getClassInstanceFromJson(jsonString, TokenDtoRequest.class);
            request.validate();
            User user = sessionDao.getSession(request.getToken()).getUser();
            return gson.toJson(new GetUserInfoDtoResponse(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(), user.getSecondName()));
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
    }

    //request
    public Response registerSeller(RegisterUserDtoRequest request) {
        try{
            request.validate();
            Seller seller = new Seller(request.getLogin(), request.getPassword(), request.getFirstName()
                    , request.getLastName(), request.getSecondName());
            userDAO.create(seller);
            Session session = new Session(UUID.randomUUID().toString(), userDAO.getByLogin(request.getLogin()));
            sessionDao.create(session);
            return Response.ok(new RegisterLoginUserDtoResponse(session.getToken()),
                    MediaType.APPLICATION_JSON).build();
        }catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
    }

    public Response registerBuyer(RegisterUserDtoRequest request) {
        try{
            request.validate();
            Buyer buyer = new Buyer(request.getLogin(), request.getPassword(), request.getFirstName()
                    , request.getLastName(), request.getSecondName());
            userDAO.create(buyer);
            Session session = new Session(UUID.randomUUID().toString(), userDAO.getByLogin(request.getLogin()));
            sessionDao.create(session);
            return Response.ok(new RegisterLoginUserDtoResponse(session.getToken()),
                    MediaType.APPLICATION_JSON).build();
        }catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
    }

    public Response login(LoginUserDtoRequest request) {
        try{
            request.validate();
            Session session = new Session(UUID.randomUUID().toString(), userDAO.getByLogin(request.getLogin()));
            sessionDao.create(session);
            return Response.ok(new RegisterLoginUserDtoResponse(session.getToken()),
                    MediaType.APPLICATION_JSON).build();
        }catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
    }

    public Response logout(TokenDtoRequest request) {
        try{
            request.validate();
            sessionDao.delete(request.getToken());
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }

    public Response delete(TokenDtoRequest request) {
        try{
            request.validate();
            userDAO.delete(sessionDao.getSession(request.getToken()).getUser().getLogin());
            sessionDao.delete(request.getToken());
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }

    public Response getUserInfo(TokenDtoRequest request){
        try{
            request.validate();
            User user = sessionDao.getSession(request.getToken()).getUser();
            return Response.ok(new GetUserInfoDtoResponse(user.getId(), user.getLogin(), user.getFirstName(),
                            user.getLastName(), user.getSecondName()), MediaType.APPLICATION_JSON).build();
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
    }
}
