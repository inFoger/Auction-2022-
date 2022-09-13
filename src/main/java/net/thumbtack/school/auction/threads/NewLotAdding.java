package net.thumbtack.school.auction.threads;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dto.request.lot.AddLotDtoRequest;
import net.thumbtack.school.auction.server.AuctionServer;

public class NewLotAdding extends Thread {
    private final AuctionServer server;
    private final AddLotDtoRequest request;
    private String response;

    public NewLotAdding(AuctionServer server, AddLotDtoRequest request) {
        this.server = server;
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public void run() {
        System.out.println("____Регистрация лота " + request.toString());
        Gson gson = new Gson();
        response = server.addLot(gson.toJson(request));
        System.out.println("____Регистрация лота завершена");
    }
}
