package org.acme;

import de.re.easymodbus.exceptions.ModbusException;
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
import java.io.IOException;

@Path("/connectFour")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    //create class instance
    @Inject
    ConnectFourImpl connectFour;

    //this Response function gets called by the .../start post request
    @POST
    @Path("/start")
    public Response startGame(Settings settings, @Context HttpServerRequest context) throws ConnectFourException, IOException {
        String sessionID = "";

        //if user has a cookie named session stored in the browser than save its value as session ID
        if (context.getCookie("session") != null) {
            sessionID = context.getCookie("session").getValue();
        }
        //start the game and update the gameState object
        GameState gameState = connectFour.startGame(settings, sessionID);
        //build a Response object consisting of gameState and a new cookie => return the Response object
        return Response.ok(gameState)
                .cookie(new NewCookie("session", gameState.sessionID, "/", null, null, 1200, false))
                .build();
    }

    //this GameState function gets called by the .../doTurn post request
    @POST
    @Path("/doTurn")
    public GameState doTurn(Turn turn) throws ConnectFourException, IOException, ModbusException { return connectFour.doTurn(turn); }

    //this GameState function gets called by the .../doRobotTurn post request
    @POST
    @Path("/doRobotTurn")
    public GameState doRobotTurn() throws ConnectFourException, IOException, ModbusException { return connectFour.doRobotTurn(); }

    //this Response function gets called by the .../waitForAcknowledge post request
    @POST
    @Path("/waitForInitialState")
    public Response waitForAcknowledge(@Context HttpServerRequest context) throws ConnectFourException, IOException, ModbusException {
        //wait until all cubes are in the starting position and update the gameState object
        GameState gameState = connectFour.waitForInitialState();
        //build a Response object consisting of gameState and delete the cookie => return the Response object
        return Response.ok(gameState)
                .cookie(new NewCookie("session", gameState.sessionID, "/", null, null, 0, false))
                .build();
    }
}