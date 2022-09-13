package net.thumbtack.school.auction.dto.response.user;

public class GetUserInfoDtoResponse {
    private Integer id;
    private String login;
    private String firstName;
    private String lastName;
    private String secondName;

    public GetUserInfoDtoResponse() {
    }

    public GetUserInfoDtoResponse(Integer id, String login, String firstName, String lastName, String secondName) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
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
}
