package net.thumbtack.school.auction.dto.request.lot;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.SessionDao;
import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetLotByCategoriesDtoRequest {
    @NotNull @NotEmpty
    private String token;
    private List<String> categories;

    public GetLotByCategoriesDtoRequest() {
    }

    public GetLotByCategoriesDtoRequest(String token, String... categories) {
        this.token = token;
        this.categories = new ArrayList<>();
        this.categories.addAll(Arrays.asList(categories));
    }

    public void validate() throws ServerException {
        SessionDao sessionDao = SettingsDatabase.getSessionDao();
        if(sessionDao.getSession(token) == null){
            throw new ServerException(ServerErrorCode.SESSION_EXPIRED);
        }
        for(String category: categories){
            if(category == null || category.length() < 2){
                throw new ServerException(ServerErrorCode.WRONG_ENTERED_CATEGORY);
            }
        }
        //Нужна ли проверка на наличие категорий в списке категорий БД ?
    }

    public String getToken() {
        return token;
    }

    public List<String> getCategories() {
        return categories;
    }
}
