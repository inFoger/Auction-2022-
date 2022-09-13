package net.thumbtack.school.auction.resources;

import net.thumbtack.school.auction.dto.request.auction.AuctionEndDtoRequest;
import net.thumbtack.school.auction.service.AuctionService;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/auction")
public class AuctionResources {
    private final AuctionService auctionService = new AuctionService();

    @PUT
    @Path("/end")
    @Consumes("application/json")
    @Produces("application/json")
    public Response auctionEnd(AuctionEndDtoRequest request) {
        return auctionService.auctionEnd(request);
    }
}
