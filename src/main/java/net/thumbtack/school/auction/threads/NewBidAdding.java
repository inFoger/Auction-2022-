package net.thumbtack.school.auction.threads;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dto.request.bid.BidDtoRequest;
import net.thumbtack.school.auction.server.AuctionServer;

public class NewBidAdding extends Thread {
    private final AuctionServer server;
    private final BidDtoRequest request;
    private String response;

    public NewBidAdding(AuctionServer server, BidDtoRequest request) {
        this.server = server;
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public void run() {
        System.out.println("______Регистрация ставки " + request.toString());
        Gson gson = new Gson();
        response = server.makeBid(gson.toJson(request));
        System.out.println("______Регистрация ставки завершена");
    }
}
