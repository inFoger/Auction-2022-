package net.thumbtack.school.auction.threads;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.response.user.RegisterLoginUserDtoResponse;
import net.thumbtack.school.auction.server.AuctionServer;

public class NewBuyerRegistering extends Thread {
    private final AuctionServer server;
    private final RegisterUserDtoRequest request;
    private RegisterLoginUserDtoResponse response;

    public NewBuyerRegistering(AuctionServer server, RegisterUserDtoRequest request) {
        this.server = server;
        this.request = request;
    }

    public RegisterLoginUserDtoResponse getResponse() {
        return response;
    }

    @Override
    public void run() {
        System.out.println("__Регистрация покупателя начата " + request.toString());
        Gson gson = new Gson();
        String json = server.registerBuyer(gson.toJson(request));
        response = gson.fromJson(json, RegisterLoginUserDtoResponse.class);
        System.out.println("__Регистрация покупателя закончена");
    }
}
