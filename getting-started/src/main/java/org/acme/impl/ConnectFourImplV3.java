package org.acme.impl;

import org.acme.model.GameState;
import org.acme.model.Settings;
import org.acme.model.Turn;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class ConnectFourImplV3 {

    @Inject
    GameState gameState;

    public GameState startGame(Settings settings) {
        gameState.move = 0;
        gameState.score = 0;
        gameState.win = false;
        gameState.matrix = new int[gameState.ROW_QUANTITY][gameState.COLUMN_QUANTITY];
        gameState.settings = settings;
        return gameState;
    }

    public GameState doTurn(Turn turn) {
        int player = gameState.PLAYER_MIN;
        int row = determineRow(turn.column);
        gameState.move++;

        if(gameState.settings.gameMode.equals("pvp")) {
            if ((gameState.move % 2) == 1) {
                player = gameState.PLAYER_MIN;
            }
            else if ((gameState.move % 2) == 0) {
                player = gameState.PLAYER_MAX;
            }
        }

        gameState.matrix[row][turn.column] = player;
        checkForWin_v1(player);
        //implement "if game finished"
        return gameState;
    }

    public GameState doRobotTurn() {
        int column = findBestMove();
        int row = determineRow(column);
        gameState.matrix[row][column] = gameState.PLAYER_MAX;
        checkForWin_v1(gameState.PLAYER_MAX);
        //implement "if game finished"
        return gameState;
    }

    public int determineRow(int column) {
        int row;
        for (row = 0; row < gameState.ROW_QUANTITY; row++) {
            if (gameState.matrix[row][column] == 0) {
                break;
            }
        }
        return row;
    }

    public int findBestMove() {
        //initialize scores
        int current_move_score;
        int alpha = Integer.MIN_VALUE;       //best score for player max
        int beta = Integer.MAX_VALUE;       //best score for player min
        int depth = 0;

        //initialize ideal_row and ideal_column
        int ideal_column = gameState.COLUMN_QUANTITY;
        boolean column_checked = false;

        for (int column = 0; column < gameState.COLUMN_QUANTITY && !column_checked; column++) {
            //determine in which row the tile should be placed
            for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
                if (gameState.matrix[row][column] == 0) {  //check if matrix[row][column] is empty
                    ideal_column = column;        //out of matrix range for declaration
                    column_checked = true;
                    break;
                }
            }
        }

        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            for (int row = 0; row < gameState.ROW_QUANTITY; row++) {       //determine row
                if (gameState.matrix[row][column] == 0) {       //check if matrix[row][column] is empty
                    gameState.matrix[row][column] = gameState.PLAYER_MAX;    //make a move
                    depth++;
                    current_move_score = minFunction(alpha, beta, depth);        //evaluate score of the current move
                    depth--;
                    gameState.matrix[row][column] = 0;        //undo the current move

                    //check if the score of the current move is better than the score of the best possible move
                    if (current_move_score > alpha) {
                        alpha = current_move_score;    //overwrite best score
                        ideal_column = column;  //memorise the position
                    }
                    break;        //does not repeat the for loop if column is already checked
                }
            }
        }
        return ideal_column;
    }

    public int minFunction(int alpha, int beta, int depth) { //represents min player's move
        gameState.score = 0;
        checkForWin_v1(gameState.PLAYER_MAX);

        if (depth >= gameState.END_NODE /*|| !areMovesLeft()*/) {  //end node reached
            return gameState.score;
        }
        else if (gameState.score >= 4) {                          //playerMAX has won
            return gameState.score;
        }
        else if (gameState.score <= -4) {                          //playerMIN has won
            return gameState.score;
        }
        else if (!areMovesLeft()) {   //tie
            return gameState.score;
        }

        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            boolean column_checked = false;
            for (int row = 0; row < gameState.ROW_QUANTITY && !column_checked; row++) {       //determine row
                if (gameState.matrix[row][column] == 0) {
                    gameState.matrix[row][column] = gameState.PLAYER_MIN;        //make a move
                    depth++;
                    beta = Math.min(beta, maxFunction(alpha, beta, depth));
                    depth--;
                    gameState.matrix[row][column] = 0;                //undo the move

                    if (beta <= alpha) {
                        return alpha;
                    }
                    column_checked = true;        //does not repeat the do-while loop if column is already checked
                }
            }
        }
        return beta;
    }

    public int maxFunction(int alpha, int beta, int depth) { //represents max player's move
        gameState.score = 0;
        checkForWin_v1(gameState.PLAYER_MIN);

        if (depth >= gameState.END_NODE /*|| !areMovesLeft()*/) {  //end node reached
            return gameState.score;
        }
        else if (gameState.score >= 4) {                       //playerMAX has won
            return gameState.score;
        }
        else if (gameState.score <= -4) {                       //playerMIN has won
            return gameState.score;
        }
        else if (!areMovesLeft()) {    //ti
            return gameState.score;
        }

        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            boolean column_checked = false;
            for (int row = 0; row < gameState.ROW_QUANTITY && !column_checked; row++) {        //determine row
                if (gameState.matrix[row][column] == 0) {
                    gameState.matrix[row][column] = gameState.PLAYER_MAX;        //make a move
                    depth++;
                    alpha = Math.max(alpha, minFunction(alpha, beta, depth));
                    depth--;
                    gameState.matrix[row][column] = 0;                //undo the move

                    if (alpha >= beta) {
                        return beta;
                    }
                    column_checked = true;        //does not repeat the do-while loop if column is already checked
                }
            }
        }
        return alpha;
    }

    public void checkForWin_v1(int player) {    //checks every possibility for a win
        int streak = 0;
        int win_chances = 0;
        gameState.win = false;

        //check for 4 in a row
        for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
            streak = 0;
            for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
                if (gameState.matrix[row][column] == player) {
                    streak++;
                    if (streak >= 4) {      //at least 4 in a row
                        win_chances++;
                    }
                }
                else {
                    streak = 0;
                }
            }
        }

        //check for 4 in a column
        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            streak = 0;
            for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
                if (gameState.matrix[row][column] == player) {
                    streak++;
                    if (streak >= 4) {      //at least 4 in a row
                        win_chances++;
                    }
                }
                else {
                    streak = 0;
                }
            }
        }

        //check the main diagonal and it's parallels
        for (int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
            for (int column = 0; column < (gameState.COLUMN_QUANTITY - 3); column++) {
                if (gameState.matrix[row][column] == player &&
                        gameState.matrix[row + 1][column + 1] == player &&
                        gameState.matrix[row + 2][column + 2] == player &&
                        gameState.matrix[row + 3][column + 3] == player) {
                    gameState.win = true;
                    win_chances++;
                }
            }
        }

        //check the antidiagonal and it's parallels
        for (int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
            for (int column = 3; column < gameState.COLUMN_QUANTITY; column++) {
                if (gameState.matrix[row][column] == player &&
                        gameState.matrix[row + 1][column - 1] == player &&
                        gameState.matrix[row + 2][column - 2] == player &&
                        gameState.matrix[row + 3][column - 3] == player) {
                    gameState.win = true;
                    win_chances++;
                }
            }
        }

        if (win_chances > 0) {
        gameState.win = true;
        }

        if (player == gameState.PLAYER_MIN) {
            gameState.score = (-1) * win_chances;
        } else if (player == gameState.PLAYER_MAX) {
            gameState.score = win_chances;
        }
    }

    boolean areMovesLeft() {
        boolean moves_left = false;

        for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
            for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
                if (gameState.matrix[row][column] == 0) {
                    moves_left = true;
                }
            }
        }
        return moves_left;
    }
}
