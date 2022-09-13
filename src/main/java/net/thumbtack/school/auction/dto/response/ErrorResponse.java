package net.thumbtack.school.auction.dto.response;

import net.thumbtack.school.auction.exception.ServerException;

import java.util.Objects;

public class ErrorResponse {
    private String errorCode;

    public ErrorResponse(ServerException exception) {
        this.errorCode = exception.getServerErrorCode().getErrorCode();
    }

    public ErrorResponse(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return errorCode.equals(that.errorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode);
    }
}
