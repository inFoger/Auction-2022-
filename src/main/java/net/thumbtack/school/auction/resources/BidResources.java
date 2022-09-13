package net.thumbtack.school.auction.resources;

import net.thumbtack.school.auction.dto.request.bid.BidDtoRequest;
import net.thumbtack.school.auction.service.BidService;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/bid")
public class BidResources {
    private final BidService bidService = new BidService();

    @PUT
    @Path("/make")
    @Consumes("application/json")
    @Produces("application/json")
    public Response makeBid(BidDtoRequest request) {
        return bidService.makeBid(request);
    }

    @PUT
    @Path("/delete")
    @Consumes("application/json")
    @Produces("application/json")
    public Response deleteBid(BidDtoRequest request) {
        return bidService.deleteBid(request);
    }
}
