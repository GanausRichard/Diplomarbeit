package org.acme;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/staticFiles")
@Consumes(MediaType.APPLICATION_JSON)
public class StaticFilesResource {

    //HTML files
    @GET
    @Path("/html/startGame")
    @Produces(MediaType.TEXT_HTML)
    public String startGameHTML() throws IOException, URISyntaxException {
        return Files.readString(Paths.get(StaticFilesResource.class.getResource("/Website/html/startGame.html").toURI()));
    }

    @GET
    @Path("/html/doTurn")
    @Produces(MediaType.TEXT_HTML)
    public String doTurnHTML() throws IOException, URISyntaxException {
        return Files.readString(Paths.get(StaticFilesResource.class.getResource("/Website/html/doTurn.html").toURI()));
    }

    //JS files
    @GET
    @Path("/js/startGame")
    @Produces("application/javascript")
    public String startGameJS() throws IOException, URISyntaxException {
        return Files.readString(Paths.get(StaticFilesResource.class.getResource("/Website/js/startGame.js").toURI()));
    }

    @GET
    @Path("/js/doTurn")
    @Produces("application/javascript")
    public String doTurnJS() throws IOException, URISyntaxException {
        return Files.readString(Paths.get(StaticFilesResource.class.getResource("/Website/js/doTurn.js").toURI()));
    }

    @GET
    @Path("/js/doRobotTurn")
    @Produces("application/javascript")
    public String doRobotTurnJS() throws IOException, URISyntaxException {
        return Files.readString(Paths.get(StaticFilesResource.class.getResource("/Website/js/doRobotTurn.js").toURI()));
    }

    //CSS files
    @GET
    @Path("/css/startGame")
    @Produces("text/css")
    public String startGameCSS() throws IOException, URISyntaxException {
        return Files.readString(Paths.get(StaticFilesResource.class.getResource("/Website/css/startGame.css").toURI()));
    }

}