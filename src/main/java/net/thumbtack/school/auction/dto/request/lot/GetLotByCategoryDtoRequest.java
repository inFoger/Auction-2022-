package net.thumbtack.school.auction.dto.request.lot;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class GetLotByCategoryDtoRequest {
    @NotNull @NotEmpty
    private String token;
    @NotNull @NotEmpty
    private String category;

    public GetLotByCategoryDtoRequest() {
    }

    public GetLotByCategoryDtoRequest(String token, String category) {
        this.token = token;
        this.category = category;
    }

    public void validate() throws ServerException {
        SessionDao sessionDao = SettingsDatabase.getSessionDao();
        if(sessionDao.getSession(token) == null){
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
        if(category == null || category.length() < 2){
            throw new ServerException(ServerErrorCode.WRONG_ENTERED_CATEGORY);
        }
    }

    public String getToken() {
        return token;
    }

    public String getCategory() {
        return category;
    }
}
