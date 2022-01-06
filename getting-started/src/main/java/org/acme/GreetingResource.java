package org.acme;

import io.vertx.core.http.HttpServerRequest;
import org.acme.impl.ConnectFourImplV3;
import org.acme.model.ConnectFourExeption;
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
    ConnectFourImplV3 connectFour;

    @POST
    @Path("/start")
    public Response startGame(Settings settings, @Context HttpServerRequest context) throws ConnectFourExeption {
        String sessionID = "";
        if (context.getCookie("session") != null) {
            sessionID = context.getCookie("session").getValue();
        }
        GameState gameState = connectFour.startGame(settings, sessionID);
        return Response.ok(gameState)
                .cookie(new NewCookie("session", gameState.sessionID, "/", null, null, 1800, false))
                .build();
    }

    @POST
    @Path("/doTurn")
    public GameState doTurn(Turn turn) { return connectFour.doTurn(turn); }

    @POST
    @Path("/doRobotTurn")
    public GameState doRobotTurn() { return connectFour.doRobotTurn(); }
}