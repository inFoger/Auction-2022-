package net.thumbtack.school.auction.dto.request.lot;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.LotDao;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.dao.UserDao;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.Session;
import net.thumbtack.school.auction.model.enums.LotStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class DeleteStopResumeLotDtoRequest {
    @NotNull @Positive @Min(1)
    private Integer id;
    @NotNull @NotEmpty
    private String token;

    public DeleteStopResumeLotDtoRequest() {
    }

    public DeleteStopResumeLotDtoRequest(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    public void validate() throws ServerException {
        SessionDao sessionDao = SettingsDatabase.getSessionDao();
        LotDao lotDao = SettingsDatabase.getLotDao();
        UserDao userDao = SettingsDatabase.getUserDao();
        Session session = sessionDao.getSession(token);
        if(session == null){
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
        if(lotDao.getLotById(id) == null){
            throw new ServerException(ServerErrorCode.LOT_NOT_FOUND);
        }
        if(!userDao.isSeller(session.getUser().getLogin())){
            throw new ServerException(ServerErrorCode.WRONG_USER_TYPE);
        }
        if(!session.getUser().getLogin().equals(lotDao.getLotById(id).getSellerLogin())){
            throw new ServerException(ServerErrorCode.LOT_BELONGS_TO_ANOTHER_SELLER);
        }
        if(lotDao.getLotById(id).getStatus().equals(LotStatus.SOLD.name())){
            throw new ServerException(ServerErrorCode.LOT_WAS_SOLD);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
