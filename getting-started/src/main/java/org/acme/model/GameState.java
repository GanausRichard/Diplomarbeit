package org.acme.model;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped //ApplicationScoped means that the class is usable in the whole application
public class GameState {
    public final int COLUMN_QUANTITY = 7; //amount of columns (by default 7)
    public final int ROW_QUANTITY = 6; //amount of columns (by default 6)
    public final int END_NODE = 10; //sets how deep the search tree should be
    public final int PLAYER_MIN = 1;
    public final int PLAYER_MAX = 2;

    public int score = 0; //the score that gets evaluated for a certain game move
    public int move = 0; //counts the players moves
    public int countYelCubes = 0; //counts the used cubes for PlayerMIN
    public int countRedCubes = 0; //counts the used cubes for PlayerMAX
    public boolean win = false;
    public Settings settings;
    public int[][] matrix = new int[ROW_QUANTITY][COLUMN_QUANTITY]; //contains the cubes' positions
    public String sessionID = null; //the session ID prevents multiple device access
    public boolean acknowledged = true; //indicates if the robot is finished with his movements
    public boolean initialState = true; //indicates if the all cubes are in the starting position
}