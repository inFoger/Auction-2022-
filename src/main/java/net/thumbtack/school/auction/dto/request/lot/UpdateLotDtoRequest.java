package net.thumbtack.school.auction.dto.request.lot;

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
import java.util.List;

public class UpdateLotDtoRequest {
    @NotNull @NotEmpty
    private String token;
    @NotNull @Positive @Min(1)
    private Integer lotId;
    @NotNull @NotEmpty
    private String name;
    @NotNull @NotEmpty
    private String status;
    @NotNull @NotEmpty
    private String description;
    @Positive @Min(1)
    private int minSellingPrice;
    @Positive @Min(1)
    private int compulsorySalePrice;
    @Positive @Min(1)
    private int startPrice;
    private List<String> categories;

    public UpdateLotDtoRequest() {
    }

    public UpdateLotDtoRequest(String token, Integer lotId, String name, String status, String description, int minSellingPrice, int compulsorySalePrice, int startPrice, List<String> categories) {
        this.token = token;
        this.lotId = lotId;
        this.name = name;
        this.status = status;
        this.description = description;
        this.minSellingPrice = minSellingPrice;
        this.compulsorySalePrice = compulsorySalePrice;
        this.startPrice = startPrice;
        this.categories = categories;
    }

    public void validate() throws ServerException {
        SessionDao sessionDao = SettingsDatabase.getSessionDao();
        LotDao lotDao = SettingsDatabase.getLotDao();
        UserDao userDao = SettingsDatabase.getUserDao();
        Session session = sessionDao.getSession(token);
        Lot lot = lotDao.getLotById(lotId);
        if(session == null){
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
        if(lot == null){
            throw new ServerException(ServerErrorCode.LOT_NOT_FOUND);
        }
        if(!userDao.isSeller(session.getUser().getLogin())){
            throw new ServerException(ServerErrorCode.WRONG_USER_TYPE);
        }
        if(!session.getUser().getLogin().equals(lot.getSellerLogin())){
            throw new ServerException(ServerErrorCode.LOT_BELONGS_TO_ANOTHER_SELLER);
        }
        if(lot.getStatus().equals(LotStatus.SOLD.name())){
            throw new ServerException(ServerErrorCode.LOT_WAS_SOLD);
        }
        if(name == null || name.equals("") || name.length() < 2 ||
                description == null || startPrice < 0 || minSellingPrice < 0 ||
                compulsorySalePrice < 0 || startPrice >= compulsorySalePrice ||
                minSellingPrice > compulsorySalePrice || minSellingPrice <= startPrice || status == null ||
                status.equals(LotStatus.SOLD.name())){
            throw new ServerException(ServerErrorCode.INCORRECTLY_FIELDS);
        }
    }

    public String getStatus() {
        return status;
    }

    public Integer getLotId() {
        return lotId;
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

    public int getMinSellingPrice() {
        return minSellingPrice;
    }

    public int getCompulsorySalePrice() {
        return compulsorySalePrice;
    }

    public List<String> getCategories() {
        return categories;
    }

    public int getStartPrice() {
        return startPrice;
    }
}
