package org.acme;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// resource for all the static files (html, js, css, png)
@Path("/staticFiles")
@Consumes(MediaType.APPLICATION_JSON)
public class StaticFilesResource {

    // converts a resource (e.g. js file path) into a string (can be handled by the browser)
    private String resourceToString(String resource) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
        Scanner s = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A");
        return s.hasNext()?s.next():"Error";
    }

    // converts a resource (e.g. png file path) into a byte array (can be handled by the browser)
    private byte[] resourceToByteArray(String resource) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
        byte[] bytes = is.readAllBytes();
        return bytes;
    }

    // HTML files

    @GET
    @Path("/html/startGame") // path to file if it has to be invoked by js, html, etc.
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

    // image files

    @GET
    @Path("/img/avatar.png") // path to file if it has to be invoked by js, html, etc.
    @Produces("image/png")
    public byte[] avatarPNG() throws IOException, URISyntaxException {
        return resourceToByteArray("/Website/img/avatar.png");
    }

    @GET
    @Path("/img/background.png")
    @Produces("image/png")
    public byte[] backgroundPNG() throws IOException, URISyntaxException {
        return resourceToByteArray("/Website/img/background.png");
    }

    @GET
    @Path("/img/emptyPlayingField.png")
    @Produces("image/png")
    public byte[] emptyPlayingFieldPNG() throws IOException, URISyntaxException {
        return resourceToByteArray("/Website/img/emptyPlayingField.png");
    }

    @GET
    @Path("/img/multiplayer.png")
    @Produces("image/png")
    public byte[] multiplayerSVG() throws IOException, URISyntaxException {
        return resourceToByteArray("/Website/img/multiplayer.png");
    }

    @GET
    @Path("/img/playstoneRed.png")
    @Produces("image/png")
    public byte[] playstoneRedPNG() throws IOException, URISyntaxException {
        return resourceToByteArray("/Website/img/playstoneRed.png");
    }

    @GET
    @Path("/img/playstoneYellow.png")
    @Produces("image/png")
    public byte[] playstoneYellowPNG() throws IOException, URISyntaxException {
        return resourceToByteArray("/Website/img/playstoneYellow.png");
    }

    @GET
    @Path("/img/singlePlayer.png")
    @Produces("image/png")
    public byte[] singlePlayerSVG() throws IOException, URISyntaxException {
        return resourceToByteArray("/Website/img/singlePlayer.png");
    }

    @GET
    @Path("/img/connectFourIcon.ico")
    @Produces("image/ico")
    public byte[] connectFourIconICO() throws IOException, URISyntaxException {
        return resourceToByteArray("/Website/img/connectFourIcon.ico");
    }

    @GET
    @Path("/img/userIcon.ico")
    @Produces("image/ico")
    public byte[] userIconICO() throws IOException, URISyntaxException {
        return resourceToByteArray("/Website/img/userIcon.ico");
    }

    // JS files

    @GET
    @Path("/js/startGame") // path to file if it has to be invoked by js, html, etc.
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

    // CSS files

    @GET
    @Path("/css/startGame") // path to file if it has to be invoked by js, html, etc.
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