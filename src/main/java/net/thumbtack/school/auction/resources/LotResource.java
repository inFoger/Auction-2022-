package net.thumbtack.school.auction.resources;

import net.thumbtack.school.auction.dto.request.lot.*;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.service.LotService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/lot")
public class LotResource {
    private final LotService lotService = new LotService();

    @POST
    @Path("/add")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addLot(AddLotDtoRequest request) {
        return lotService.addLot(request);
    }

    @PUT
    @Path("/update")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateLot(UpdateLotDtoRequest request) {
        return lotService.updateLot(request);
    }

    @PUT
    @Path("/delete")
    @Consumes("application/json")
    @Produces("application/json")
    public Response deleteLot(DeleteStopResumeLotDtoRequest request) {
        return lotService.deleteLot(request);
    }

    @PUT
    @Path("/stopBids")
    @Consumes("application/json")
    @Produces("application/json")
    public Response stopBids(DeleteStopResumeLotDtoRequest request) {
        return lotService.stopBids(request);
    }

    @PUT
    @Path("/resumeBids")
    @Consumes("application/json")
    @Produces("application/json")
    public Response resumeBids(DeleteStopResumeLotDtoRequest request) {
        return lotService.resumeBids(request);
    }

    @PUT
    @Path("/getAll")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getLots(GetAllLotsDtoRequest request) {
        return lotService.getLots(request);
    }

    @PUT
    @Path("/getLotsByCategory")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getLotsByCategory(GetLotByCategoryDtoRequest request) {
        return lotService.getLotsByCategory(request);
    }

    @PUT
    @Path("/getLotsByCategories")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getLotsByCategories(GetLotByCategoriesDtoRequest request) {
        return lotService.getLotsByCategories(request);
    }

    @PUT
    @Path("getLotsByAtLeastOneCategory")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getLotsByAtLeastOneCategory(GetLotByCategoriesDtoRequest request) {
        return lotService.getLotsByAtLeastOneCategory(request);
    }
}
