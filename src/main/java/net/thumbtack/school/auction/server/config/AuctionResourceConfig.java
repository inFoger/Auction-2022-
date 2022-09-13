package net.thumbtack.school.auction.server.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class AuctionResourceConfig extends ResourceConfig {
    public AuctionResourceConfig() {
        packages("net.thumbtack.school.auction.resources",
                "net.thumbtack.school.auction.rest.mappers");
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }
}
