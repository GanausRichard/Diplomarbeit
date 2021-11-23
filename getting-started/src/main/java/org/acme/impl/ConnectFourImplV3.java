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
        gameState.score = 0;
        gameState.win = false;
        gameState.matrix = new int[gameState.ROW_QUANTITY][gameState.COLUMN_QUANTITY];
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
        checkForWin(gameState.PLAYER_MAX);

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
        checkForWin(gameState.PLAYER_MIN);

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

    public void checkForWin(int player) {       //checks every possibility for a win
        int win_chances = 0;
        gameState.win = false;

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

    /*public void checkForWin_v2(int player) {    //checks every possibility for a win
        int win_chances = 0;
        gameState.win = false;

        int[][] positions = new int[2][];   // position[0][n] = row /// position[1][n] = column
        int total_positions = 0;
        int n_in_a_row = 0;      //number of positions in a row
        int n_in_a_column = 0;      //number of positions in a column

        //save positions and check rows for a win
        for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
            for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
                if (gameState.matrix[row][column] == player) {
                    positions[0][total_positions] = row;
                    positions[1][total_positions] = column;
                    total_positions++;

                    n_in_a_row++;
                }
            }

            if (n_in_a_row == 4) {  //4 in a row
                for (int i = 0; i < (n_in_a_row - 1); i++) {
                    if (positions[1][total_positions] == positions[1][total_positions + 1] + 1) {
                        //win conditions met once
                    }
                }
            }
            else if (n_in_a_row == 5) {  //5 in a row

            }
            else if (n_in_a_row == 6) {  //6 in a row

             }
            else if (n_in_a_row == 7) {  //7 in a row
                //win condition met four times
            }
            n_in_a_row = 0;
        }



        //column has to be equal to check 4 in a column
        for (int i = 0; i < total_positions; i++) {
            for (int column = 0; column >= gameState.COLUMN_QUANTITY; column++) {
                do {
                    n_in_a_column++;
                } while (positions[1][i] == column);
            }
        }
        // check both diagonals

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
    }*/

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
