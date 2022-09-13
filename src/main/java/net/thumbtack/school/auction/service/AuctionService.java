package net.thumbtack.school.auction.service;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dao.LotDao;
import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dto.request.auction.AuctionEndDtoRequest;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Lot;
import net.thumbtack.school.auction.model.enums.LotStatus;
import net.thumbtack.school.auction.utils.AuctionUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AuctionService {
    private Gson gson = new Gson();
    private LotDao lotDao = SettingsDatabase.getLotDao();

    //json
    public String auctionEnd(String requestJsonString){
        try {
            AuctionEndDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, AuctionEndDtoRequest.class);
            request.validate();
            Lot lot = lotDao.getLotById(request.getLotId());
            lot.setStatus(LotStatus.SOLD.name());
            lotDao.updateLot(lot.getId(),lot.getName(),lot.getStatus(),lot.getDescription(),lot.getMinSellingPrice(),lot.getCompulsorySalePrice(),lot.getCurrentPrice(),lot.getLastBuyerLogin(),lot.getCategories());
            lotDao.lotBuying(lot.getId(),lot.getLastBuyerLogin());
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    //request
    public Response auctionEnd(AuctionEndDtoRequest request){
        try {
            request.validate();
            Lot lot = lotDao.getLotById(request.getLotId());
            lot.setStatus(LotStatus.SOLD.name());
            lotDao.updateLot(lot.getId(),lot.getName(),lot.getStatus(),lot.getDescription(),lot.getMinSellingPrice(),lot.getCompulsorySalePrice(),lot.getCurrentPrice(),lot.getLastBuyerLogin(),lot.getCategories());
            lotDao.lotBuying(lot.getId(),lot.getLastBuyerLogin());
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }

}
