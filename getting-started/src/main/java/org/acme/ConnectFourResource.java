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

// resource for all connect four functions
@Path("/connectFour")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConnectFourResource {

    @Inject
    ConnectFourImpl connectFour;


    // the startGame POST-method assigns every gameState variable a value,
    // the users settings are inherited and a session ID is set
    @POST
    @Path("/startGame")
    public Response startGame(Settings settings, @Context HttpServerRequest context) throws ConnectFourException {
        String sessionID = "";

        // if user has a cookie named session stored in the browser => safe the session ID
        if (context.getCookie("session") != null) {
            sessionID = context.getCookie("session").getValue();
        }
        // start the game and update the gameState object
        GameState gameState = connectFour.startGame(settings, sessionID);
        // if no error occurs => create a new cookie and return gameState as a Response
        return Response.ok(gameState)
                .cookie(new NewCookie("session", gameState.sessionID, "/", null, null, 1200, false))
                .build();
    }

    // the doTurn POST-method handles the users input (when the player moves)
    @POST
    @Path("/doTurn")
    public GameState doTurn(Turn turn) throws ConnectFourException { return connectFour.doTurn(turn); }

    // the doRobotTurn POST-method determines the robots next move
    @POST
    @Path("/doRobotTurn")
    public GameState doRobotTurn() throws ConnectFourException { return connectFour.doRobotTurn(); }


    // the waitForInitialState POST-method reverts everything to it's initialState
    // (returns every gaming piece to it's original place and deletes the session ID)
    @POST
    @Path("/waitForInitialState")
    public Response waitForAcknowledge(@Context HttpServerRequest context) throws ConnectFourException {
        // update the gameState and end the game
        GameState gameState = connectFour.waitForInitialState();
        // cookie gets deleted from users device
        return Response.ok(gameState)
                .cookie(new NewCookie("session", gameState.sessionID, "/", null, null, 0, false))
                .build();
    }
}