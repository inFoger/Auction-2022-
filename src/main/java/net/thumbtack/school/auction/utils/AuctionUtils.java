package net.thumbtack.school.auction.utils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import net.thumbtack.school.auction.dto.response.ErrorResponse;
import net.thumbtack.school.auction.exception.ServerException;

public class AuctionUtils {
    private static final Gson GSON = new Gson();

    public static Response failureResponse(Status status, ServerException ex) {
        return Response.status(status).entity(GSON.toJson(new ErrorResponse(ex)))
                .type(MediaType.APPLICATION_JSON).build();
    }

    public static Response failureResponse(ServerException ex) {
        return failureResponse(Status.BAD_REQUEST, ex);
    }

}
