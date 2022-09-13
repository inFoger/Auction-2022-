package net.thumbtack.school.auction.dto.request.auction;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.LotDao;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.dao.UserDao;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Lot;
import net.thumbtack.school.auction.model.enums.LotStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AuctionEndDtoRequest {
    @NotNull @NotEmpty
    private String token;
    @NotNull @Positive @Min(1)
    private Integer lotId;

    public AuctionEndDtoRequest() {
    }

    public AuctionEndDtoRequest(String token, Integer lotId) {
        this.token = token;
        this.lotId = lotId;
    }

    public void validate() throws ServerException {
        SessionDao sessionDao = SettingsDatabase.getSessionDao();
        LotDao lotDao = SettingsDatabase.getLotDao();
        UserDao userDao = SettingsDatabase.getUserDao();
        if(sessionDao.getSession(token) == null){
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
        if(!userDao.isAuctioneer(sessionDao.getSession(token).getUser().getLogin())){
            throw new ServerException(ServerErrorCode.WRONG_USER_TYPE);
        }
        Lot lot = lotDao.getLotById(lotId);
        if(lot == null){
            throw new ServerException(ServerErrorCode.LOT_NOT_FOUND);
        }
        if(lot.getStatus().equals(LotStatus.SOLD.name())){
            throw new ServerException(ServerErrorCode.LOT_WAS_SOLD);
        }
        if(lot.getStatus().equals(LotStatus.NOT_FOR_SALE.name())){
            throw new ServerException(ServerErrorCode.LOT_NOT_SELLING);
        }
        if(lot.getCurrentPrice() < lot.getMinSellingPrice()){
            throw new ServerException(ServerErrorCode.TOO_SMALL_CURRENT_PRICE);
        }
    }

    public Integer getLotId() {
        return lotId;
    }

    public String getToken() {
        return token;
    }
}
