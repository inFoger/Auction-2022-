package net.thumbtack.school.auction.dto.request.user;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.UserDao;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LoginUserDtoRequest {
    @NotNull @NotEmpty
    private String login;
    @NotNull @NotEmpty
    private String password;

    public LoginUserDtoRequest() {
    }

    public LoginUserDtoRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void validate() throws ServerException {
        if(login == null || login.equals(" ") || password == null || password.equals(" ")){
            throw new ServerException(ServerErrorCode.INCORRECTLY_FIELDS);
        }
        UserDao userDAO = SettingsDatabase.getUserDao();
        if(userDAO.isLoginExist(login)){
            if(userDAO.getByLogin(login).getPassword().equals(password)){
                return;
            }
            throw new ServerException(ServerErrorCode.WRONG_PASSWORD);
        }
        throw new ServerException(ServerErrorCode.USER_NOT_FOUND);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
