package net.thumbtack.school.auction.resources;

import net.thumbtack.school.auction.service.ServerService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/server")
public class ServerResource {
    private final ServerService serverService = new ServerService();

    @POST
    @Path("/start")
    @Consumes("application/json")
    public void startServer(String json) {
        serverService.startServer(json);
    }

    @PUT
    @Path("/stop")
    @Consumes("application/json")
    public void stopServer(String json) {
        serverService.stopServer(json);
    }

    @DELETE
    @Path("/clear")
    public void clear() {
         serverService.clearServer();
    }

}
