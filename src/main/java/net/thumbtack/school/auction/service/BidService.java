package net.thumbtack.school.auction.service;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dao.BidDao;
import net.thumbtack.school.auction.dao.LotDao;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dto.request.bid.BidDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Bid;
import net.thumbtack.school.auction.model.Lot;
import net.thumbtack.school.auction.model.enums.LotStatus;
import net.thumbtack.school.auction.model.users.Buyer;
import net.thumbtack.school.auction.model.users.User;
import net.thumbtack.school.auction.utils.AuctionUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BidService {
    private Gson gson = new Gson();
    private BidDao bidDao = SettingsDatabase.getBidDao();
    private SessionDao sessionDao = SettingsDatabase.getSessionDao();
    private LotDao lotDao = SettingsDatabase.getLotDao();

    //json
    public String makeBid(String requestJsonString){
        try {
            BidDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, BidDtoRequest.class);
            request.validate();
            Lot lot = lotDao.getLotById(request.getLotId());
            if((lot.getLastBuyerLogin() == null && request.getPrice() < lot.getCurrentPrice()) ||
                    (lot.getLastBuyerLogin() != null && request.getPrice() <= lot.getCurrentPrice())){
                throw new ServerException(ServerErrorCode.TOO_SMALL_BID);
            }
            User buyer = sessionDao.getSession(request.getToken()).getUser();
            lot.setCurrentPrice(request.getPrice());
            lot.setLastBuyerLogin(buyer.getLogin());
            if(request.getPrice() >= lot.getCompulsorySalePrice()){
                lot.setStatus(LotStatus.SOLD.name());
            }
            lotDao.updateLot(lot.getId(), lot.getName(),lot.getStatus(),lot.getDescription(),lot.getMinSellingPrice(),lot.getCompulsorySalePrice(),lot.getCurrentPrice(), lot.getLastBuyerLogin(),lot.getCategories());
            bidDao.create(request.getLotId(), new Bid((Buyer)buyer, request.getLotId(), request.getPrice()));
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    public String deleteBid(String requestJsonString){
        try {
            BidDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, BidDtoRequest.class);
            request.validate();

            bidDao.delete(request.getLotId(), sessionDao.getSession(request.getToken()).getUser().getLogin());
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    //request
    public Response makeBid(BidDtoRequest request){
        try {
            request.validate();
            Lot lot = lotDao.getLotById(request.getLotId());
            if((lot.getLastBuyerLogin() == null && request.getPrice() < lot.getCurrentPrice()) ||
                    (lot.getLastBuyerLogin() != null && request.getPrice() <= lot.getCurrentPrice())){
                throw new ServerException(ServerErrorCode.TOO_SMALL_BID);
            }
            User buyer = sessionDao.getSession(request.getToken()).getUser();
            lot.setCurrentPrice(request.getPrice());
            lot.setLastBuyerLogin(buyer.getLogin());
            if(request.getPrice() >= lot.getCompulsorySalePrice()){
                lot.setStatus(LotStatus.SOLD.name());
            }
            lotDao.updateLot(lot.getId(), lot.getName(),lot.getStatus(),lot.getDescription(),lot.getMinSellingPrice(),lot.getCompulsorySalePrice(),lot.getCurrentPrice(), lot.getLastBuyerLogin(),lot.getCategories());
            bidDao.create(request.getLotId(), new Bid((Buyer)buyer, request.getLotId(), request.getPrice()));
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }

    public Response deleteBid(BidDtoRequest request){
        try {
            request.validate();

            bidDao.delete(request.getLotId(), sessionDao.getSession(request.getToken()).getUser().getLogin());
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }
}
