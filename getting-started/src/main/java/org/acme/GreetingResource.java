package org.acme;

import org.acme.impl.ConnectFourImpl;
import org.acme.impl.ConnectFourImplV2;
import org.acme.impl.ConnectFourImplV3;
import org.acme.model.GameState;
import org.acme.model.Settings;
import org.acme.model.Turn;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/connectFour")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @Inject
    ConnectFourImplV3 connectFour;

    @POST
    @Path("/start")
    public GameState startGame(Settings settings) {
        return connectFour.startGame(settings);
    }

    @POST
    @Path("/doTurn")
    public GameState doTurn(Turn turn) { return connectFour.doTurn(turn); }

    @POST
    @Path("/doRobotTurn")
    public GameState doRobotTurn() { return connectFour.doRobotTurn(); }
}