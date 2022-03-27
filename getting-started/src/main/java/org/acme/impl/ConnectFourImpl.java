package org.acme.impl;

import org.acme.model.ConnectFourException;
import org.acme.model.GameState;
import org.acme.model.Settings;
import org.acme.model.Turn;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.UUID;

// this class contains the main game logic
@Dependent
public class ConnectFourImpl {

    // gameState gets injected in class ConnectFourImpl
    @Inject
    GameState gameState;

    // the method startGame gets called once at the start of the game
    public GameState startGame(Settings settings, String sessionID) throws ConnectFourException {

        // if a session ID exists and it's not the session ID of the device
        // of the user that was playing before => then an exception gets thrown
        if (gameState.sessionID != null && !sessionID.equals(gameState.sessionID)) {
            throw new ConnectFourException("Ein Benutzer ist bereits angemeldet!");
        }

        // a new session ID gets created if no user is currently playing
        // session ID and settings get set for the gameState object
        gameState.sessionID = UUID.randomUUID().toString();
        gameState.settings = settings;
        gameState.win = false;
        gameState.move = 0;
        gameState.score = 0;
        gameState.matrix = new int[gameState.ROW_QUANTITY][gameState.COLUMN_QUANTITY];

        return gameState;
    }

    // the method doTurn updates the gameState after a player moves
    public GameState doTurn(Turn turn) throws ConnectFourException {
        gameState.move++;
        // the row in which the gaming piece will be placed gets determined
        int row = determineRow(turn.column);
        int player = gameState.PLAYER_MIN;

        // in pvp mode the player gets declared
        if (gameState.settings.gameMode.equals("pvp") && (gameState.move % 2) == 0) {
            player = gameState.PLAYER_MAX;
        }
        // update the matrix and check if player has won
        gameState.matrix[row][turn.column] = player;
        checkForWin(player, 0);

        return gameState;
    }

    // this method updates the gameState for the robots move
    public GameState doRobotTurn() throws ConnectFourException {
        gameState.move++;
        // column gets determined by use of alpha-beta pruning
        int column = findBestMove();
        // the row in which the gaming piece will be placed gets determined
        int row = determineRow(column);

        // update the matrix and check if robot has won
        gameState.matrix[row][column] = gameState.PLAYER_MAX;
        checkForWin(gameState.PLAYER_MAX, 0);

        return gameState;
    }

    // this method is not completed
    // the purpose of waitForInitialState was to wait until
    // the robot put all gaming pieces in their initial position
    public GameState waitForInitialState() throws ConnectFourException {
        gameState.sessionID = null; // reset the session ID
        return gameState;
    }

    // this method determines the row in which the gaming
    // piece will fall when thrown in a specific column
    public int determineRow(int column) {
        int row;
        for (row = 0; row < gameState.ROW_QUANTITY; row++) {
            if (gameState.matrix[row][column] == 0) {
                break;
            }
        }
        return row;
    }

    // the best move for the robot gets evaluated in this method
    // (implementation of alpha-beta pruning)
    public int findBestMove() {
        // initialize scores
        int current_move_score;
        int alpha = Integer.MIN_VALUE; //best score for player max
        int beta = Integer.MAX_VALUE;  //best score for player min
        int depth = 0;

        int ideal_column = gameState.COLUMN_QUANTITY;
        boolean column_checked = false;

        // initialize ideal_column to the first column with empty space
        for (int column = 0; column < gameState.COLUMN_QUANTITY && !column_checked; column++) {
            for (int row = 0; row < gameState.ROW_QUANTITY && !column_checked; row++) {
                // check if matrix[row][column] is empty
                if (gameState.matrix[row][column] == 0) {
                    ideal_column = column;
                    column_checked = true;
                }
            }
        }

        // iterate over all columns to find the best move
        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
                // check if matrix[row][column] is empty
                if (gameState.matrix[row][column] == 0) {
                    gameState.matrix[row][column] = gameState.PLAYER_MAX; // move (only temporary)
                    depth++;
                    // evaluate score of the current move
                    current_move_score = maxFunction(alpha, beta, depth);
                    depth--;
                    gameState.matrix[row][column] = 0; // undo the current move

                    // check if the score of the current move is better than the score of the best possible move
                    if (current_move_score > alpha) {
                        alpha = current_move_score; // overwrite best score
                        ideal_column = column; // set the ideal column
                    }
                    break; // if column is checked => break => check the next column
                }
            }
        }
        return ideal_column;
    }

    // maxFunction determines the maximum value of its child nodes
    public int maxFunction(int alpha, int beta, int depth) {
        gameState.score = 0;
        checkForWin(gameState.PLAYER_MAX, depth);

        // end function if condition is met
        if(endMinMax(depth)) {
            return gameState.score;
        }

        // iterate over all columns to find the best move
        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            boolean column_checked = false;
            for (int row = 0; (row < gameState.ROW_QUANTITY); row++) {
                // check if matrix[row][column] is empty
                if (gameState.matrix[row][column] == 0) {
                    gameState.matrix[row][column] = gameState.PLAYER_MIN; // move (only temporary)
                    depth++;
                    // evaluate score of the current move and save it as beta
                    beta = Math.min(beta, minFunction(alpha, beta, depth));
                    depth--;
                    gameState.matrix[row][column] = 0; // undo the current move

                    // if alpha is the highest value => return alpha
                    if (beta <= alpha) {
                        return alpha;
                    }
                    break; // if column is checked => break => check the next column
                }
            }
        }
        return beta;
    }

    // minFunction determines the minimum value of its child nodes
    public int minFunction(int alpha, int beta, int depth) {
        gameState.score = 0;
        checkForWin(gameState.PLAYER_MIN, depth);

        // end function if condition is met
        if (endMinMax(depth)) {
            return gameState.score;
        }

        // iterate over all columns to find the best move
        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            boolean column_checked = false;
            for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
                // check if matrix[row][column] is empty
                if (gameState.matrix[row][column] == 0) {
                    gameState.matrix[row][column] = gameState.PLAYER_MAX; // move (only temporary)
                    depth++;
                    // evaluate score of the current move and save it as alpha
                    alpha = Math.max(alpha, maxFunction(alpha, beta, depth));
                    depth--;
                    gameState.matrix[row][column] = 0; // undo the current move

                    // if beta is the smallest value => return beta
                    if (alpha >= beta) {
                        return beta;
                    }
                    break; // if column is checked => break => check the next column
                }
            }
        }
        return alpha;
    }

    // this method evaluates a score for the algorithm and
    // checks if a player has won the game
    public void checkForWin(int player, int depth) {
        int streak;
        boolean continue_loop;
        int win_chances = 0;
        // depending on the difficulty, the multiplier ranges from 1 to ...
        int multiplier = 1 + gameState.settings.endNode - depth;
        gameState.win = false;

        // check for 4 in a row
        for (int column = 0; column < (gameState.COLUMN_QUANTITY - 3); column++) {
            for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
                streak = 0;
                do {
                    if (gameState.matrix[row][column + streak] == player) {
                        streak++;
                        continue_loop = true;
                        // if at least 4 in a row => increase win_chances
                        // by multiplier => then check out the next column
                        if (streak >= 4) {
                            win_chances += multiplier;
                            continue_loop = false;
                        }
                    }

                    else {
                        // break do while loop
                        continue_loop = false;
                    }
                } while(continue_loop);
            }
        }

        // check for 4 in a column
        for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            for (int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
                streak = 0;
                do {
                    if (gameState.matrix[row + streak][column] == player) {
                        streak++;
                        continue_loop = true;
                        // if at least 4 in a row => increase win_chances
                        // by multiplier => then check out the next column
                        if (streak >= 4) {
                            win_chances += multiplier;
                            continue_loop = false;
                        }
                    }
                    else {
                        // break do while loop
                        continue_loop = false;
                    }
                } while(continue_loop);
            }
        }

        //check the main diagonal and its parallels
        for (int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
            for (int column = 0; column < (gameState.COLUMN_QUANTITY - 3); column++) {
                streak = 0;
                // if the current position == player then next position on the diagonal gets checked
                // the loop breaks as soon as the first position of that diagonal != player
                // or when 4 positions of that diagonal == player
                do {
                    if (gameState.matrix[row + streak][column + streak] == player) {
                        streak++;
                        continue_loop = true;
                        if (streak >= 4) { //at least 4 in a row
                            win_chances += multiplier;
                            continue_loop = false;
                        }
                    }
                    else {
                        // break do while loop
                        continue_loop = false;
                    }
                } while(continue_loop);
            }
        }

        //check the antidiagonal and its parallels
        for (int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
            for (int column = 3; column < gameState.COLUMN_QUANTITY; column++) {
                streak = 0;
                // if the current position == player then next position on the diagonal gets checked
                // the loop breaks as soon as the first position of that diagonal != player
                // or when 4 positions of that diagonal == player
                do {
                    if (gameState.matrix[row + streak][column - streak] == player) {
                        streak++;
                        continue_loop = true;
                        if (streak >= 4) {      //at least 4 in a row
                            win_chances += multiplier;
                            continue_loop = false;
                        }
                    }
                    else {
                        // break do while loop
                        continue_loop = false;
                    }
                } while(continue_loop);
            }
        }

        //negative score for player min
        if (player == gameState.PLAYER_MIN) {
            gameState.score = (-1) * win_chances;
        }
        //positive score for player max
        else if (player == gameState.PLAYER_MAX) {
            gameState.score = win_chances;
        }

        if (win_chances != 0) {
            gameState.win = true;
        }
    }

    // this method is for breaking the algorithm on a set condition
    boolean endMinMax(int depth) {
        // if end node reached or playerMAX/playerMIN has won or it's a tie => return true
        if (depth >= gameState.settings.endNode || gameState.win || isMatrixFilled()) {
            return true;
        }
        else {
            return false;
        }
    }

    // this method checks if all positions of the matrix are filled
    boolean isMatrixFilled() {
        boolean matrixFilled = false;
        int maxPossibleMoves = gameState.COLUMN_QUANTITY * gameState.ROW_QUANTITY;

        if(gameState.move >= maxPossibleMoves) {
            matrixFilled = true;
        }
        return matrixFilled;
    }
}