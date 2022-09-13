package net.thumbtack.school.auction.dto.response.lot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.thumbtack.school.auction.model.Bid;
import net.thumbtack.school.auction.model.Lot;

import java.beans.Transient;
import java.util.*;

public class GetLotsDtoResponse {
    private List<LotDtoInternal> list;
    private String buyerLogin;

    public List<LotDtoInternal> getList() {
        return list;
    }

    public String getBuyerLogin() {
        return buyerLogin;
    }

    public GetLotsDtoResponse() {
    }

    public GetLotsDtoResponse(List<Lot> list, String buyerLogin) {
        this.buyerLogin = buyerLogin;
        this.list = new ArrayList<>();

        if(list != null) {
            for(Lot lot : list){
                Map<String,BidDtoInternal> bids = new HashMap<>();
                for(Bid bid : lot.getBids()) {
                    bids.put(bid.getBuyerLogin(),new BidDtoInternal(bid.getBuyerLogin(),bid.getLotId(),bid.getPrice()));
                }
                this.list.add(new LotDtoInternal(lot.getId(), lot.getSellerLogin(), lot.getStatus(), lot.getName(),
                        lot.getDescription(), lot.getCurrentPrice(), lot.getLastBuyerLogin(), lot.getMinSellingPrice(),
                        lot.getCompulsorySalePrice(), lot.getCategories(), bids));
            }
        }
    }

    @Transient
    public List<Integer> getLotsId() {
        List<Integer> res = new ArrayList<>();
        for(LotDtoInternal lot : list) {
            res.add(lot.getId());
        }
        res.sort((a1,a2) -> a1>a2 ? a1 : a2);
        return res;
    }

    @Transient
    public String getLots(){
        StringBuilder res = new StringBuilder();
        list.sort(Comparator.comparingInt(LotDtoInternal::getId));
        for(LotDtoInternal elem: list){
            res.append(elem.print()) ;
        }
        return res.toString();
    }

    @Transient
    public String getLotsStatusForBuyer(){
        StringBuilder res = new StringBuilder();
        list.sort(Comparator.comparingInt(LotDtoInternal::getId));
        for(LotDtoInternal elem: list){
            res.append("Лот").append(":");
            res.append(elem.getStatusForBuyer(buyerLogin));
        }
        return res.toString();
    }

    @Transient
    public String getLotsStatusForAuctioneer(){
        StringBuilder res = new StringBuilder();
        list.sort(Comparator.comparingInt(LotDtoInternal::getId));
        for(LotDtoInternal elem: list){
            res.append("Лот").append(":");
            res.append(elem.getStatusForAuctioneer()).append(" ");
        }
        return res.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetLotsDtoResponse that = (GetLotsDtoResponse) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
