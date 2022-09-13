package net.thumbtack.school.auction.resources;

import net.thumbtack.school.auction.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.auction.dto.request.user.TokenDtoRequest;
import net.thumbtack.school.auction.service.UserService;

import javax.ws.rs.Consumes;
import javax.validation.*;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserResource {
    private final UserService userService = new UserService();

    @POST
    @Path("/seller")
    @Consumes("application/json")
    @Produces("application/json")
    public Response registerSeller(@Valid RegisterUserDtoRequest request) {
        return userService.registerSeller(request);
    }

    @POST
    @Path("/buyer")
    @Consumes("application/json")
    @Produces("application/json")
    public Response registerBuyer(@Valid RegisterUserDtoRequest request) {
        return userService.registerBuyer(request);
    }

    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(@Valid LoginUserDtoRequest request) {
        return userService.login(request);
    }

    @PUT
    @Path("/logout")
    @Consumes("application/json")
    @Produces("application/json")
    public Response logout(@Valid TokenDtoRequest request) {
        return userService.logout(request);
    }

    @PUT
    @Path("/delete")
    @Consumes("application/json")
    @Produces("application/json")
    public Response delete(@Valid TokenDtoRequest request) {
        return userService.delete(request);
    }

    @PUT
    @Path("/userInfo")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getUserInfo(@Valid TokenDtoRequest request) {
        return userService.getUserInfo(request);
    }

}