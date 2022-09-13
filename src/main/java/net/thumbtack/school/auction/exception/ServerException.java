// REVU исключения не относятся к модели
// модель - это набор классов данных
// а исключения - это особые ситуации при работе с ними
// мы могли бы написать и без исключений
// перенесите в пакет net.thumbtack.school.auction.exceptions
package net.thumbtack.school.auction.exception;

import java.util.Objects;

public class ServerException extends Exception{
    private final ServerErrorCode serverErrorCode;
    public ServerException(ServerErrorCode serverErrorCode){
        super(serverErrorCode.getErrorCode());
        this.serverErrorCode = serverErrorCode;
    }
    public ServerException(ServerErrorCode serverErrorCode, String param) {
        super(param);
        this.serverErrorCode = serverErrorCode;
    }

    public ServerErrorCode getServerErrorCode(){
        return serverErrorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerException that = (ServerException) o;
        return serverErrorCode == that.serverErrorCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverErrorCode);
    }
}
