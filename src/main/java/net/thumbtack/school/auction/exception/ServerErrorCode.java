package net.thumbtack.school.auction.exception;

public enum ServerErrorCode {
    AUCTIONEER_CANT_BE_DELETED("Auctioneer can't be deleted"),
    BID_NOT_FOUND("Bid not found"),
    INCORRECTLY_FIELDS("Fields entered incorrectly"),
    LOT_ID_ALREADY_EXIST("Lot id already exist"),
    LOT_BELONGS_TO_ANOTHER_SELLER("Lot belongs to another seller"),
    LOT_WAS_SOLD("Lot was sold"),
    LOT_NOT_SELLING("Lot not selling now"),
    LOT_NOT_FOUND("Lot with current id not found"),
    LOGIN_ALREADY_EXISTS("User with this login already exists"),

    SUCCESS("Success"),
    WRONG_URL("Wrong URL"),
    VALIDATION_ERROR("Validation error"),
    METHOD_NOT_ALLOWED("Method not allowed"),
    MYSQL_CANT_INSERT_USER("MySql: Can't insert User"),

    SESSION_EXPIRED("Session expired"),
    TOO_SMALL_BID("Too small bid"),
    TOO_SMALL_CURRENT_PRICE("Too small current price"),
    USER_NOT_FOUND("User not found"),
    WRONG_ENTERED_CATEGORY("Wrong entered category"),
    WRONG_LOT_STATUS("Wrong lot status"),
    WRONG_USER_TYPE("Wrong user type"),
    WRONG_PASSWORD("Wrong password"),
    WRONG_JSON("Wrong Json");

    private final String errorCode;
    private ServerErrorCode(String errorCode){
        this.errorCode = errorCode;
    }
    public String getErrorCode(){
        return errorCode;
    }
}
