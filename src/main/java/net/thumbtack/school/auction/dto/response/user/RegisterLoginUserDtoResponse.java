package net.thumbtack.school.auction.dto.response.user;

import net.thumbtack.school.auction.model.Session;
import net.thumbtack.school.auction.model.users.User;

public class RegisterLoginUserDtoResponse {
    private String token;

    public RegisterLoginUserDtoResponse() {
    }

    public RegisterLoginUserDtoResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
