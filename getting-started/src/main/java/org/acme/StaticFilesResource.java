package org.acme;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

@Path("/staticFiles")
@Consumes(MediaType.APPLICATION_JSON)
public class StaticFilesResource {

    private String resourceToString(String resource) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext()?s.next():"Error";
    }

    //HTML files
    @GET
    @Path("/html/startGame")
    @Produces(MediaType.TEXT_HTML)
    public String startGameHTML() throws IOException, URISyntaxException {
        return resourceToString("/Website/html/startGame.html");
    }

    @GET
    @Path("/html/doTurn")
    @Produces(MediaType.TEXT_HTML)
    public String doTurnHTML() throws IOException, URISyntaxException {
        return resourceToString("/Website/html/doTurn.html");
    }

    //JS files
    @GET
    @Path("/js/startGame")
    @Produces("application/javascript")
    public String startGameJS() throws IOException, URISyntaxException {
        return resourceToString("/Website/js/startGame.js");
    }

    @GET
    @Path("/js/doTurn")
    @Produces("application/javascript")
    public String doTurnJS() throws IOException, URISyntaxException {
        return resourceToString("/Website/js/doTurn.js");
    }

    @GET
    @Path("/js/doRobotTurn")
    @Produces("application/javascript")
    public String doRobotTurnJS() throws IOException, URISyntaxException {
        return resourceToString("/Website/js/doRobotTurn.js");
    }

    //CSS files
    @GET
    @Path("/css/startGame")
    @Produces("text/css")
    public String startGameCSS() throws IOException, URISyntaxException {
        return resourceToString("/Website/css/startGame.css");
    }
}