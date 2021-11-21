package org.acme.impl;

import org.acme.model.GameState;
import org.acme.model.Settings;
import org.acme.model.Turn;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class ConnectFourImplV2 {

    @Inject
    GameState gameState;

    public GameState startGame(Settings settings) {
        gameState.score = 0;
        gameState.win = false;
        gameState.settings = settings;
        return gameState;
    }

    public GameState doTurn(Turn turn) {
        int row = determineRow(turn.column);
        gameState.matrix[row][turn.column] = gameState.PLAYER_MIN;
        checkForWin(gameState.PLAYER_MIN);
        //implement "if game finished"
        return gameState;
    }

    public GameState doRobotTurn() {
        int column = findBestMove();
        int row = determineRow(column);
        gameState.matrix[row][column] = gameState.PLAYER_MAX;
        checkForWin(gameState.PLAYER_MAX);
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
        int best_move_score = Integer.MIN_VALUE;       //best score for player max

        //initialize ideal_row and ideal_column
        int ideal_column = gameState.COLUMN_QUANTITY;
        boolean column_checked = false;

        for (int column = 0; column < gameState.COLUMN_QUANTITY && !column_checked; column++) {
            //determine in which row the tile should be placed
            for (int row = 0; row < gameState.ROW_QUANTITY && !column_checked; row++) {
                if (gameState.matrix[row][column] == 0)    //check if matrix[row][column] is empty
                {
                    ideal_column = column;        //out of matrix range for declaration
                    column_checked = true;
                }
            }
            column++;
        }

        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {

            column_checked = false;
            for (int row = 0; row < gameState.ROW_QUANTITY && !column_checked; row++) {       //determine row
                if (gameState.matrix[row][column] == 0) {       //check if matrix[row][column] is empty
                    gameState.matrix[row][column] = gameState.PLAYER_MAX;    //make a move
                     current_move_score = minFunction();        //evaluate score of the current move
                    gameState.matrix[row][column] = 0;        //undo the current move

                    //check if the score of the current move is better than the score of the best possible move
                    if (current_move_score > best_move_score) {
                        best_move_score = current_move_score;    //overwrite best score
                        ideal_column = column;  //memorise the position
                    }
                    column_checked = true;        //does not repeat the for loop if column is already checked
                }
            }
        }
        return ideal_column;
    }

    public int minFunction() {
        gameState.score = 0;
        checkForWin(gameState.PLAYER_MAX);
        gameState.win = false;

        if (gameState.score >= 1)                            //playerMAX has won
        {
            return gameState.score;
        } else if (gameState.score <= -1)                        //playerMIN has won
        {
            return gameState.score;
        } else if (!areMovesLeft())    //tie
        {
            return 0;
        }

        int best_score = Integer.MAX_VALUE;    //worst possible outcome for playerMIN

        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {

            boolean column_checked = false;
            for (int row = 0; row < gameState.ROW_QUANTITY && !column_checked; row++) {       //determine row
                if (gameState.matrix[row][column] == 0) {
                    gameState.matrix[row][column] = gameState.PLAYER_MIN;        //make a move
                    best_score = Math.min(best_score, maxFunction());
                    gameState.matrix[row][column] = 0;                //undo the move

                    column_checked = true;        //does not repeat the do-while loop if column is already checked
                }
            }
        }
        return best_score;
    }

    public int maxFunction() {
        gameState.score = 0;
        checkForWin(gameState.PLAYER_MIN);
        gameState.win = false;

        if (gameState.score >= 1)                            //playerMAX has won
        {
            return gameState.score;
        } else if (gameState.score <= -1)                        //playerMIN has won
        {
            return gameState.score;
        } else if (!areMovesLeft())    //tie
        {
            return 0;
        }

        int best_score = Integer.MIN_VALUE;    //worst possible outcome for playerMAX

        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {

            boolean column_checked = false;
            for (int row = 0; row < gameState.ROW_QUANTITY && !column_checked; row++) {        //determine row
                if (gameState.matrix[row][column] == 0) {
                    gameState.matrix[row][column] = gameState.PLAYER_MAX;        //make a move
                    best_score = Math.max(best_score, minFunction());
                    gameState.matrix[row][column] = 0;                //undo the move

                    column_checked = true;        //does not repeat the do-while loop if column is already checked
                }
            }
        }
        return best_score;
    }

    public void checkForWin(int player) {       //checks every possibility for a win
        int win_chances = 0;

        //check all rows for a win
        for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
            for (int column = 0; column < (gameState.COLUMN_QUANTITY - 3); column++) {
                if (gameState.matrix[row][column] == player &&
                        gameState.matrix[row][column + 1] == player &&
                        gameState.matrix[row][column + 2] == player &&
                        gameState.matrix[row][column + 3] == player) {
                    gameState.win = true;
                    win_chances++;
                }
            }
        }

        //check all columns for a win
        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            for (int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
                if (gameState.matrix[row][column] == player &&
                        gameState.matrix[row + 1][column] == player &&
                        gameState.matrix[row + 2][column] == player &&
                        gameState.matrix[row + 3][column] == player) {
                    gameState.win = true;
                    win_chances++;
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
