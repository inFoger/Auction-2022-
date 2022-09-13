package net.thumbtack.school.auction.service;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dao.BidDao;
import net.thumbtack.school.auction.dao.LotDao;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dto.request.lot.*;
import net.thumbtack.school.auction.dto.response.EmptyResponse;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.dto.response.lot.GetLotsDtoResponse;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Lot;
import net.thumbtack.school.auction.model.enums.LotStatus;
import net.thumbtack.school.auction.utils.AuctionUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class LotService {
    private Gson gson = new Gson();
    private LotDao lotDao = SettingsDatabase.getLotDao();
    private SessionDao sessionDao = SettingsDatabase.getSessionDao();

    //json
    public String addLot(String requestJsonString){
        try {
            AddLotDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, AddLotDtoRequest.class);
            request.validate();
            Lot lot = new Lot(sessionDao.getSession(request.getToken()).getUser().getLogin(), request.getName(),
                    request.getDescription(), request.getStartPrice(), request.getMinSellingPrice(),
                    request.getCompulsorySalePrice(), request.getCategories());
            lotDao.create(lot);

        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    public String updateLot(String requestJsonString) {
        try {
            UpdateLotDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, UpdateLotDtoRequest.class);
            request.validate();
            Lot lot = lotDao.getLotById(request.getLotId());
            if(lot.getLastBuyerLogin() != null && request.getCompulsorySalePrice() <= lot.getCurrentPrice()){
                lot.setStatus(LotStatus.SOLD.name());
            }
            lotDao.updateLot(request.getLotId(),request.getName(),lot.getStatus().equals(LotStatus.SOLD.name())? lot.getStatus() :request.getStatus(),request.getDescription(),request.getMinSellingPrice(),
                    request.getCompulsorySalePrice(),lot.getLastBuyerLogin() == null ? request.getStartPrice() : lot.getCurrentPrice(),lot.getLastBuyerLogin(),request.getCategories());
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    public String deleteLot(String requestJsonString){
        try {
            DeleteStopResumeLotDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, DeleteStopResumeLotDtoRequest.class);
            request.validate();
            lotDao.delete(request.getId());
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    public String stopBids(String requestJsonString){
        try {
            DeleteStopResumeLotDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, DeleteStopResumeLotDtoRequest.class);
            request.validate();
            Lot lot = lotDao.getLotById(request.getId());
            if(!lot.getStatus().equals(LotStatus.FOR_SALE.name())){
                return gson.toJson(new ErrorResponse(new ServerException(ServerErrorCode.WRONG_LOT_STATUS)));
            }
            lot.setStatus(LotStatus.NOT_FOR_SALE.name());
            lotDao.updateLot(lot.getId(),lot.getName(),lot.getStatus(),lot.getDescription(),lot.getMinSellingPrice(),lot.getCompulsorySalePrice(),lot.getCurrentPrice(),lot.getLastBuyerLogin(),lot.getCategories());
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    public String resumeBids(String requestJsonString){
        try {
            DeleteStopResumeLotDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, DeleteStopResumeLotDtoRequest.class);
            request.validate();
            Lot lot = lotDao.getLotById(request.getId());
            if(!lot.getStatus().equals(LotStatus.NOT_FOR_SALE.name())){
                return gson.toJson(new ErrorResponse(new ServerException(ServerErrorCode.WRONG_LOT_STATUS)));
            }
            lot.setStatus(LotStatus.FOR_SALE.name());
            lotDao.updateLot(lot.getId(),lot.getName(),lot.getStatus(),lot.getDescription(),lot.getMinSellingPrice(),lot.getCompulsorySalePrice(),lot.getCurrentPrice(),lot.getLastBuyerLogin(),lot.getCategories());
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
        return gson.toJson(new EmptyResponse(true));
    }

    public String getLots(String requestJsonString){
        try {
            GetAllLotsDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, GetAllLotsDtoRequest.class);
            request.validate();
            return gson.toJson(new GetLotsDtoResponse(lotDao.getAll(), sessionDao.getSession(request.getToken()).getUser().getLogin()));
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
    }


    public String getLotsByCategory(String requestJsonString){
        try {
            GetLotByCategoryDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, GetLotByCategoryDtoRequest.class);
            request.validate();
            return gson.toJson(new GetLotsDtoResponse(lotDao.getByCategory(request.getCategory()), sessionDao.getSession(request.getToken()).getUser().getLogin()));
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
    }

    public String getLotsByCategories(String requestJsonString){
        try {
            GetLotByCategoriesDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, GetLotByCategoriesDtoRequest.class);
            request.validate();
            return gson.toJson(new GetLotsDtoResponse(lotDao.getByCategories(request.getCategories()), sessionDao.getSession(request.getToken()).getUser().getLogin()));
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
    }

    public String getLotsByAtLeastOneCategory(String requestJsonString){
        try {
            GetLotByCategoriesDtoRequest request = UserService.getClassInstanceFromJson(requestJsonString, GetLotByCategoriesDtoRequest.class);
            request.validate();
            return gson.toJson(new GetLotsDtoResponse(lotDao.getByAtLeastOneCategory(request.getCategories()), sessionDao.getSession(request.getToken()).getUser().getLogin()));
        } catch (ServerException e){
            return gson.toJson(new ErrorResponse(e));
        }
    }

    //request
    public Response addLot(AddLotDtoRequest request){
        try {
            request.validate();
            Lot lot = new Lot(sessionDao.getSession(request.getToken()).getUser().getLogin(), request.getName(),
                    request.getDescription(), request.getStartPrice(), request.getMinSellingPrice(),
                    request.getCompulsorySalePrice(), request.getCategories());
            lotDao.create(lot);

        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }

    public Response updateLot(UpdateLotDtoRequest request) {
        try {
            request.validate();
            Lot lot = lotDao.getLotById(request.getLotId());
            if(lot.getLastBuyerLogin() != null && request.getCompulsorySalePrice() <= lot.getCurrentPrice()){
                lot.setStatus(LotStatus.SOLD.name());
            }
            lotDao.updateLot(request.getLotId(),request.getName(),lot.getStatus().equals(LotStatus.SOLD.name())? lot.getStatus() :request.getStatus(),request.getDescription(),request.getMinSellingPrice(),
                    request.getCompulsorySalePrice(),lot.getLastBuyerLogin() == null ? request.getStartPrice() : lot.getCurrentPrice(),lot.getLastBuyerLogin(),request.getCategories());
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }

    public Response deleteLot(DeleteStopResumeLotDtoRequest request){
        try {
            request.validate();
            lotDao.delete(request.getId());
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }

    public Response stopBids(DeleteStopResumeLotDtoRequest request){
        try {
            request.validate();
            Lot lot = lotDao.getLotById(request.getId());
            if(!lot.getStatus().equals(LotStatus.FOR_SALE.name())){
                return AuctionUtils.failureResponse(new ServerException(ServerErrorCode.WRONG_LOT_STATUS));
            }
            lot.setStatus(LotStatus.NOT_FOR_SALE.name());
            lotDao.updateLot(lot.getId(),lot.getName(),lot.getStatus(),lot.getDescription(),lot.getMinSellingPrice(),lot.getCompulsorySalePrice(),lot.getCurrentPrice(),lot.getLastBuyerLogin(),lot.getCategories());
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }

    public Response resumeBids(DeleteStopResumeLotDtoRequest request){
        try {
            request.validate();
            Lot lot = lotDao.getLotById(request.getId());
            if(!lot.getStatus().equals(LotStatus.NOT_FOR_SALE.name())){
                return AuctionUtils.failureResponse(new ServerException(ServerErrorCode.WRONG_LOT_STATUS));
            }
            lot.setStatus(LotStatus.FOR_SALE.name());
            lotDao.updateLot(lot.getId(),lot.getName(),lot.getStatus(),lot.getDescription(),lot.getMinSellingPrice(),lot.getCompulsorySalePrice(),lot.getCurrentPrice(),lot.getLastBuyerLogin(),lot.getCategories());
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
        return Response.ok(new EmptyResponse(true), MediaType.APPLICATION_JSON).build();
    }

    public Response getLots(GetAllLotsDtoRequest request){
        try {
            request.validate();
            return Response.ok(new GetLotsDtoResponse(lotDao.getAll(),
                    sessionDao.getSession(request.getToken()).getUser().getLogin()), MediaType.APPLICATION_JSON).build();
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
    }


    public Response getLotsByCategory(GetLotByCategoryDtoRequest request){
        try {
            request.validate();
            return Response.ok(new GetLotsDtoResponse(lotDao.getByCategory(request.getCategory()),
                            sessionDao.getSession(request.getToken()).getUser().getLogin()), MediaType.APPLICATION_JSON).build();
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
    }

    public Response getLotsByCategories(GetLotByCategoriesDtoRequest request){
        try {
            request.validate();
            return Response.ok(new GetLotsDtoResponse(lotDao.getByCategories(request.getCategories()),
                            sessionDao.getSession(request.getToken()).getUser().getLogin()), MediaType.APPLICATION_JSON).build();
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
    }

    public Response getLotsByAtLeastOneCategory(GetLotByCategoriesDtoRequest request){
        try {
            request.validate();
            return Response.ok(new GetLotsDtoResponse(lotDao.getByAtLeastOneCategory(request.getCategories()),
                    sessionDao.getSession(request.getToken()).getUser().getLogin()), MediaType.APPLICATION_JSON).build();
        } catch (ServerException e){
            return AuctionUtils.failureResponse(e);
        }
    }

}
