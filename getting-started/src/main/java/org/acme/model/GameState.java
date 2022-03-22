package org.acme.model;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped //ApplicationScoped means that the class is usable in the whole application
public class GameState {
    public final int COLUMN_QUANTITY = 7; //amount of columns (by default 7)
    public final int ROW_QUANTITY = 6; //amount of columns (by default 6)
    public final int PLAYER_MIN = 1;
    public final int PLAYER_MAX = 2;

    public int[][] matrix = new int[ROW_QUANTITY][COLUMN_QUANTITY]; //contains the cubes' positions
    public Settings settings; //settings are set by the player
    public String sessionID = null; //the session ID prevents multiple device access
    public int score = 0; //the score that gets evaluated for a certain game move
    public int move = 0; //counts the players moves
    public boolean win = false;
}