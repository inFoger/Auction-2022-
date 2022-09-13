package net.thumbtack.school.auction.rest.mappers;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.utils.AuctionUtils;


@Provider
public class MethodNotAllowedExceptionMapper implements	ExceptionMapper<NotAllowedException> {

    @Override
	public Response toResponse(NotAllowedException exception) {
		return AuctionUtils.failureResponse(Status.METHOD_NOT_ALLOWED, new ServerException(ServerErrorCode.METHOD_NOT_ALLOWED));
	}
}