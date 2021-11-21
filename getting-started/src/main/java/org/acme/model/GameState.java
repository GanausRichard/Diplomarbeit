package org.acme.model;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class GameState {
    public final int COLUMN_QUANTITY = 5;   //by default 7
    public final int ROW_QUANTITY = 4;  // by default 6
    public final int END_NODE = 19; //by default 42
    public final int PLAYER_MIN = 1;
    public final int PLAYER_MAX = 2;

    public int score = 0;
    public boolean win = false;
    public Settings settings;
    public int[][] matrix = new int[ROW_QUANTITY][COLUMN_QUANTITY];
}