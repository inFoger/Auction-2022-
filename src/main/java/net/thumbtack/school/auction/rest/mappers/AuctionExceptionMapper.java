package net.thumbtack.school.auction.rest.mappers;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import net.thumbtack.school.auction.exception.ServerErrorCode;
import net.thumbtack.school.auction.exception.ServerException;
import net.thumbtack.school.auction.utils.AuctionUtils;

@Provider
public class AuctionExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
	
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            message.append(cv.getPropertyPath() + " " + cv.getMessage() + "\n");
        }

		return AuctionUtils.failureResponse(Status.BAD_REQUEST,
				new ServerException(ServerErrorCode.VALIDATION_ERROR, message.toString()));
    }
}