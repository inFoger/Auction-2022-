package net.thumbtack.school.auction.dto.request.user;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TokenDtoRequest {
    @NotNull @NotEmpty
    private String token;

    public TokenDtoRequest() {
    }

    public TokenDtoRequest(String token) {
        this.token = token;
    }

    public void validate() throws ServerException {
        SessionDao sessionDao = SettingsDatabase.getSessionDao();
        if(token == null || sessionDao.getSession(token) == null){
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
