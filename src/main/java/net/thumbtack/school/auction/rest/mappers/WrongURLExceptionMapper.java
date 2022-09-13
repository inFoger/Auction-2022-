package net.thumbtack.school.auction.rest.mappers;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.utils.AuctionUtils;


@Provider
public class WrongURLExceptionMapper implements	ExceptionMapper<NotFoundException> {

    @Override
	public Response toResponse(NotFoundException exception) {
		return AuctionUtils.failureResponse(Status.NOT_FOUND, new ServerException(ServerErrorCode.WRONG_URL));
	}
}