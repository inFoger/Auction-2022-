package net.thumbtack.school.auction.dto.response;

public class EmptyResponse {
    boolean check;//для проверки

    public EmptyResponse() {
    }

    public EmptyResponse(boolean check){
        this.check = check;
    }

    public boolean getCheck(){
        return check;
    }

}
