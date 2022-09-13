package net.thumbtack.school.auction.model;

import net.thumbtack.school.auction.model.users.Buyer;

import java.util.Objects;

public class Bid {
    private String buyerLogin;
    private Integer lotId;
    private Integer price;

    public Bid(Buyer buyer, Integer lotId, Integer price) {
        this.buyerLogin = buyer.getLogin();
        this.lotId = lotId;
        this.price = price;
    }

    public Bid(String buyerLogin, Integer lotId, Integer price) {
        this.buyerLogin = buyerLogin;
        this.lotId = lotId;
        this.price = price;
    }

    public String getBuyerLogin() {
        return buyerLogin;
    }

    public void setBuyerLogin(String buyerLogin) {
        this.buyerLogin = buyerLogin;
    }

    public Integer getLotId() {
        return lotId;
    }

    public void setLotId(Integer lotId) {
        this.lotId = lotId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bid)) return false;
        Bid bid = (Bid) o;
        return buyerLogin.equals(bid.buyerLogin) && getLotId().equals(bid.getLotId()) && getPrice().equals(bid.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyerLogin, getLotId(), getPrice());
    }

    public String print() {
        return "buyer=" + buyerLogin +
                ", price=" + price + "; ";
    }
}
