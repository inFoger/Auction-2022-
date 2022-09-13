package net.thumbtack.school.auction.dto.request.lot;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.dao.UserDao;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.users.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddLotDtoRequest{
    @NotNull @NotEmpty
    private String token;
    @NotNull @NotEmpty
    private String name;
    @NotNull @NotEmpty
    private String description;
    @Positive @Min(1)
    private int startPrice;
    @Positive @Min(1)
    private int minSellingPrice;
    @Positive @Min(1)
    private int compulsorySalePrice;
    private List<String> categories;

    public AddLotDtoRequest() {
    }

    public AddLotDtoRequest(String token, String name, String description, int startPrice, int minSellingPrice, int compulsorySalePrice, List<String> categories) {
        this.token = token;
        this.name = name;
        this.description = description;
        this.startPrice = startPrice;
        this.minSellingPrice = minSellingPrice;
        this.compulsorySalePrice = compulsorySalePrice;
        this.categories = categories;
    }

    public void validate() throws ServerException {
        SessionDao sessionDao = SettingsDatabase.getSessionDao();
        UserDao userDao = SettingsDatabase.getUserDao();
        if(sessionDao.getSession(token) == null){
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
        User user = sessionDao.getSession(token).getUser();
        if(!userDao.isSeller(user.getLogin())){
            throw new ServerException(ServerErrorCode.WRONG_USER_TYPE);
        }
        if(name == null || name.equals("") || name.length() < 2 ||
        description == null || startPrice < 0 || minSellingPrice < 0 ||
        compulsorySalePrice < 0 || startPrice >= compulsorySalePrice ||
        minSellingPrice > compulsorySalePrice || minSellingPrice <= startPrice){
            throw new ServerException(ServerErrorCode.INCORRECTLY_FIELDS);
        }
    }

    public static List<String> enterCategory(String... categories){
        return new ArrayList<>(Arrays.asList(categories));
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public int getMinSellingPrice() {
        return minSellingPrice;
    }

    public int getCompulsorySalePrice() {
        return compulsorySalePrice;
    }

    public List<String> getCategories() {
        return categories;
    }
}
