package net.thumbtack.school.auction.dto.request.user;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.UserDao;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RegisterUserDtoRequest {
    @NotNull @NotEmpty
    private String login;
    @NotNull @NotEmpty
    private String password;
    @NotNull @NotEmpty
    private String firstName;
    @NotNull @NotEmpty
    private String lastName;
    private String secondName;
    public RegisterUserDtoRequest(String login, String password, String firstName, String lastName, String secondName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
    }

    public RegisterUserDtoRequest() {
    }

    public void validate() throws ServerException {
        if(login == null || login.equals(" ") || login.length() < 2
                || password == null || password.equals(" ") || password.length() < 2
                || firstName == null || firstName.equals(" ") || firstName.length() < 2
                || lastName == null || lastName.equals(" ") || lastName.length() < 2){
            throw new ServerException(ServerErrorCode.INCORRECTLY_FIELDS);
        }
        UserDao userDAO = SettingsDatabase.getUserDao();
        if(userDAO.isLoginExist(login)){
            throw new ServerException(ServerErrorCode.LOGIN_ALREADY_EXISTS);
        }
        if(secondName != null){
            if(secondName.equals(" ") || secondName.length() < 2 ){
                throw new ServerException(ServerErrorCode.INCORRECTLY_FIELDS);
            }
        }
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
