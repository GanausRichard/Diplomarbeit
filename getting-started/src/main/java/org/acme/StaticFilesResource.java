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

//this class is responsible for file handling (html, javascript, css)

@Path("/staticFiles")
@Consumes(MediaType.APPLICATION_JSON)
public class StaticFilesResource {

    //converts a resource (e.g. js file path) into a string
    private String resourceToString(String resource) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext()?s.next(): "Error";
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

    //image files
    /*
    @GET
    @Path("/img/background")
    @Produces("image/png")
    public File backgroundPNG() throws IOException, URISyntaxException {
        return new File("/Website/img/background.png");
    }

    @GET
    @Path("/img/emptyPlayingField")
    @Produces("image/png")
    public File emptyPlayingFieldPNG() throws IOException, URISyntaxException {
        return new File("/Website/img/emptyPlayingField.png");
    }

    @GET
    @Path("/img/playstoneRed")
    @Produces("image/png")
    public File playstoneRedPNG() throws IOException, URISyntaxException {
        return new File("/Website/img/playstoneRed.png");
    }

    @GET
    @Path("/img/playstoneYellow")
    @Produces("image/png")
    public File playstoneYellowPNG() throws IOException, URISyntaxException {
        return new File("/Website/img/playstoneYellow.png");
    }

    @GET
    @Path("/img/connectFourIcon")
    @Produces("image/ico")
    public File connectFourICON() throws IOException, URISyntaxException {
        return new File("/Website/img/connectFourIcon.ico");
    }
    */

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

    //CSS files

    @GET
    @Path("/css/startGame")
    @Produces("text/css")
    public String startGameCSS() throws IOException, URISyntaxException {
        return resourceToString("/Website/css/startGame.css");
    }

    @GET
    @Path("/css/doTurn")
    @Produces("text/css")
    public String doTurnCSS() throws IOException, URISyntaxException {
        return resourceToString("/Website/css/doTurn.css");
    }
}