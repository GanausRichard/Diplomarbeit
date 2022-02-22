package org.acme;

import io.vertx.core.http.HttpServerRequest;
import org.acme.impl.ConnectFourImpl;
import org.acme.model.ConnectFourException;
import org.acme.model.GameState;
import org.acme.model.Settings;
import org.acme.model.Turn;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/connectFour")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @Inject
    ConnectFourImpl connectFour;

    @POST
    @Path("/start")
    public Response startGame(Settings settings, @Context HttpServerRequest context) throws ConnectFourException {
        String sessionID = "";

        //if user has a cookie named session stored in the browser than safe the session ID
        if (context.getCookie("session") != null) {
            sessionID = context.getCookie("session").getValue();
        }
        //start the game and update the gameState object
        GameState gameState = connectFour.startGame(settings, sessionID);
        return Response.ok(gameState)
                .cookie(new NewCookie("session", gameState.sessionID, "/", null, null, 1200, false))
                .build();
    }

    @POST
    @Path("/doTurn")
    public GameState doTurn(Turn turn) throws ConnectFourException { return connectFour.doTurn(turn); }

    @POST
    @Path("/doRobotTurn")
    public GameState doRobotTurn() throws ConnectFourException { return connectFour.doRobotTurn(); }

    @POST
    @Path("/waitForInitialState")
    public GameState waitForAcknowledge(@Context HttpServerRequest context) throws ConnectFourException {
        //cookie gets deleted from users device
        if (context.getCookie("session") != null) {
            context.getCookie("session").setMaxAge(0);
        }
        return connectFour.waitForInitialState();
    }
}