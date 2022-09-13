package net.thumbtack.school.auction.dto.request.bid;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.LotDao;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.dao.UserDao;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Lot;
import net.thumbtack.school.auction.model.Session;
import net.thumbtack.school.auction.model.enums.LotStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class BidDtoRequest {
    @NotNull @NotEmpty
    private String token;
    @NotNull @Positive @Min(1)
    private Integer lotId;
    @NotNull @Positive @Min(1)
    private Integer price;

    public BidDtoRequest() {
    }

    public BidDtoRequest(String token, Integer lotId, Integer price) {
        this.token = token;
        this.lotId = lotId;
        this.price = price;
    }

    public void validate() throws ServerException {
        LotDao lotDao = SettingsDatabase.getLotDao();
        SessionDao sessionDao = SettingsDatabase.getSessionDao();
        UserDao userDao = SettingsDatabase.getUserDao();
        Session session = sessionDao.getSession(token);
        Lot lot = lotDao.getLotById(lotId);
        if (session == null) {
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
        if (!userDao.isBuyer(session.getUser().getLogin())) {
            throw new ServerException(ServerErrorCode.WRONG_USER_TYPE);
        }
        if (lot == null) {
            throw new ServerException(ServerErrorCode.LOT_NOT_FOUND);
        }
        if (lot.getStatus().equals(LotStatus.SOLD.name())) {
            throw new ServerException(ServerErrorCode.LOT_WAS_SOLD);
        }
        if (lot.getStatus().equals(LotStatus.NOT_FOR_SALE.name())) {
            throw new ServerException(ServerErrorCode.LOT_NOT_SELLING);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BidDtoRequest request = (BidDtoRequest) o;
        return Objects.equals(token, request.token) && Objects.equals(lotId, request.lotId) && Objects.equals(price, request.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, lotId, price);
    }

    public String getToken() {
        return token;
    }

    public Integer getLotId() {
        return lotId;
    }

    public Integer getPrice() {
        return price;
    }
}
