package org.acme.model;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class GameState {
    public final int COLUMN_QUANTITY = 7;   //by default 7
    public final int ROW_QUANTITY = 6;  // by default 6
    public final int END_NODE = 10; //by default 42 / cannot be higher than 12 by now
    public final int PLAYER_MIN = 1;
    public final int PLAYER_MAX = 2;

    public int score = 0;
    public int move = 0; //only counts player's moves
    public boolean win = false;
    public Settings settings;
    public int[][] matrix = new int[ROW_QUANTITY][COLUMN_QUANTITY];
}