package net.thumbtack.school.auction.model;

import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.model.enums.LotStatus;
import net.thumbtack.school.auction.model.users.Buyer;
import net.thumbtack.school.auction.model.users.Seller;

import java.util.*;

public class Lot {
    private Integer id;
    private String sellerLogin;
    private String status;
    private String name;
    private String description;
    private int currentPrice;
    private String lastBuyerLogin;
    private int minSellingPrice;
    private int compulsorySalePrice;
    private List<String> categories;
    private Map<String, Bid> bids;

    public Lot(String sellerLogin, String name, String description, int startPrice,
               int minSellingPrice, int compulsorySalePrice, List<String> categories) {
        this.sellerLogin = sellerLogin;
        this.status = LotStatus.FOR_SALE.name();
        this.name = name;
        this.description = description;
        this.currentPrice = startPrice;
        this.minSellingPrice = minSellingPrice;
        this.compulsorySalePrice = compulsorySalePrice;
        this.categories = categories;
        bids = new HashMap<>();
    }

    public Lot(String sellerLogin, String name, String status, String description, int startPrice,
               int minSellingPrice, int compulsorySalePrice, List<String> categories) {
        this.sellerLogin = sellerLogin;
        this.status = status;
        this.name = name;
        this.description = description;
        this.currentPrice = startPrice;
        this.minSellingPrice = minSellingPrice;
        this.compulsorySalePrice = compulsorySalePrice;
        this.categories = categories;
        bids = new HashMap<>();
    }

    public String getSellerLogin() {
        return sellerLogin;
    }

    public String getLastBuyerLogin() {
        return lastBuyerLogin;
    }

    public Lot(Integer id, String sellerLogin, String status, String name, String description, int currentPrice,
               int minSellingPrice, int compulsorySalePrice, String lastBuyer) {
        this.id = id;
        this.sellerLogin = sellerLogin;
        this.status = status;
        this.name = name;
        this.lastBuyerLogin = lastBuyer;
        this.description = description;
        this.currentPrice = currentPrice;
        this.minSellingPrice = minSellingPrice;
        this.compulsorySalePrice = compulsorySalePrice;
        bids = new HashMap<>();
        categories = new ArrayList<>();

    }

    public Lot(Seller seller, String name, String description, int startPrice,
               int minSellingPrice, int compulsorySalePrice, List<String> categories) {
        this.sellerLogin = seller.getLogin();
        this.status = LotStatus.FOR_SALE.name();
        this.name = name;
        this.description = description;
        this.currentPrice = startPrice;
        this.minSellingPrice = minSellingPrice;
        this.compulsorySalePrice = compulsorySalePrice;
        this.categories = categories;
        bids = new HashMap<>();
    }

    public String getStatusForBuyer(String buyerLogin){
        Bid currentBid = bids.get(buyerLogin);
        if(currentBid == null){
            return "???? ???? ???????????? ???????????? ???? ???????????? ??????.";
        }
        if(status.equals(LotStatus.SOLD.name())){
            if(lastBuyerLogin.equals(buyerLogin)){
                return "???? ???????????? ???????? ?????? ???? ???????? " + currentPrice + " ????????????.";
            }
            if(currentPrice >= compulsorySalePrice){
                return "?????? ?????? ???????????? ???? ???????????????????????? ????????";
            }
            return "???????? ?????? ?????? ???????????? ???? ???????? " + currentPrice + " ????????????.";

        }
        if(status.equals(LotStatus.FOR_SALE.name())){
            if(lastBuyerLogin.equals(buyerLogin)){
                return "???? ?????????????????? ?????????????? ??????????????????????. ???????? ???????????? ???????????????????? " + currentBid.getPrice() + " ????????????.";
            }
            return "???? ?????????????? ???????????? ???? ???????? ?????? ?? ?????????????? " + currentBid.getPrice() + " ????????????.";
        }
        return "?????? ???????? ???? ??????????????????";
    }

    public String getStatusForAuctioneer(){
        if(bids.isEmpty()){
            return "???? ?????? ?????? ???? ???????? ????????????.";
        }
        String statusStr;
        if(status.equals(LotStatus.FOR_SALE.name())){
            statusStr = "?????? ??????????????????. ?????????????????? ???????????? ?????????????????? " + currentPrice + " ????????????.";
        }else if(status.equals(LotStatus.SOLD.name())){
            if(currentPrice >= compulsorySalePrice){
                statusStr = "?????? ?????? ???????????? ???? ???????????????????????? ???????? ???????????????????????? " + lastBuyerLogin + ".";
            }else {
                statusStr = "???????????? ???????????? ???? " + currentPrice + " ???????????? ???????????????????????? " + lastBuyerLogin + ".";
            }
        }else {
            statusStr = "?????? ???????? ?? ??????????????.";
        }
        StringBuilder result = new StringBuilder(statusStr + "?????????? ???????????? " + bids.size() + ".?????? ????????????:");
        for(Bid bid: bids.values()){
            result.append(bid.getBuyerLogin());
            result.append(" ???????????? ???????????? ?? ?????????????? ");
            result.append(bid.getPrice());
            result.append(" ????????????. ");
        }
        return result.toString();
    }

    public void setBids(Map<String, Bid> bids) {
        this.bids = bids;
    }

    public Bid addBid(Bid bid) throws ServerException {
        bids.put(bid.getBuyerLogin(), bid);
        currentPrice = bid.getPrice();
        lastBuyerLogin = bid.getBuyerLogin();
        if(bid.getPrice() >= compulsorySalePrice){
            status = LotStatus.SOLD.name();
        }
        return bid;
    }

    public void deleteBid(String buyerLogin) throws ServerException{
        if(bids.remove(buyerLogin) == null){
            throw new ServerException(ServerErrorCode.BID_NOT_FOUND);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinSellingPrice(int minSellingPrice) {
        this.minSellingPrice = minSellingPrice;
    }

    public void setCompulsorySalePrice(int compulsorySalePrice) {
        this.compulsorySalePrice = compulsorySalePrice;
    }

    public void setStartPrice(int startPrice){
        if(bids.isEmpty()){
            currentPrice = startPrice;
        }
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setLastBuyerLogin(String lastBuyerLogin) {
        this.lastBuyerLogin = lastBuyerLogin;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Bid> getBids(){
        return new ArrayList<>(bids.values());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMinSellingPrice() {
        return minSellingPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCompulsorySalePrice() {
        return compulsorySalePrice;
    }

    public List<String> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lot)) return false;
        Lot lot = (Lot) o;
        return getCurrentPrice() == lot.getCurrentPrice() && getMinSellingPrice() == lot.getMinSellingPrice() && getCompulsorySalePrice() == lot.getCompulsorySalePrice() && Objects.equals(getId(), lot.getId()) && sellerLogin.equals(lot.sellerLogin) && Objects.equals(getStatus(), lot.getStatus()) && getName().equals(lot.getName()) && Objects.equals(getDescription(), lot.getDescription()) && Objects.equals(lastBuyerLogin, lot.lastBuyerLogin) && Objects.equals(getCategories(), lot.getCategories()) && Objects.equals(getBids(), lot.getBids());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), sellerLogin, getStatus(), getName(), getDescription(), getCurrentPrice(), lastBuyerLogin, getMinSellingPrice(), getCompulsorySalePrice(), getCategories(), getBids());
    }

    public String print() {
        StringBuilder bidsPrint = new StringBuilder();
        for(Bid elem: bids.values()){
            bidsPrint.append(elem.print());
            bidsPrint.append(" ");
        }
        if(categories == null) {
            categories = new ArrayList<>();
        }
        return "Lot{" +
                "seller=" + sellerLogin +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", currentPrice=" + currentPrice +
                ", currentBuyer=" + lastBuyerLogin +
                ", minSellingPrice=" + minSellingPrice +
                ", compulsorySalePrice=" + compulsorySalePrice +
                ", categories=" + categories +
                ", bids=[" + bidsPrint +
                "]}";
    }
}
