package org.acme.impl;

import org.acme.model.ConnectFourExeption;
import org.acme.model.GameState;
import org.acme.model.Settings;
import org.acme.model.Turn;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.UUID;

@Dependent
public class ConnectFourImplV3 {

    @Inject
    GameState gameState;

    public GameState startGame(Settings settings, String sessionID) throws ConnectFourExeption {
        if (gameState.sessionID != null && !sessionID.equals(gameState.sessionID)) {
            throw new ConnectFourExeption("Ein Benutzer ist bereits angemeldet!");
        }
        gameState.sessionID = UUID.randomUUID().toString();
        gameState.move = 0;
        gameState.score = 0;
        gameState.win = false;
        gameState.matrix = new int[gameState.ROW_QUANTITY][gameState.COLUMN_QUANTITY];
        gameState.settings = settings;

        gameState.timeNewLoop = 0;
        gameState.timeOldLoop = 0;

        return gameState;
    }

    public GameState doTurn(Turn turn) {
        gameState.move++;
        int player = gameState.PLAYER_MIN;
        int row = determineRow(turn.column);

        if(gameState.settings.gameMode.equals("pvp")) {
            if ((gameState.move % 2) == 1) {
                player = gameState.PLAYER_MIN;
            }
            else if ((gameState.move % 2) == 0) {
                player = gameState.PLAYER_MAX;
            }
        }

        gameState.matrix[row][turn.column] = player;
        //sendPositions(takeCubeFrom(player), putCubeTo(turn.column));

        checkForWin_v1(player);
        //implement "if game finished"
        System.out.println("Duration of old loop: " + gameState.timeOldLoop);
        System.out.println("Duration of new loop: " + gameState.timeNewLoop);
        return gameState;
    }

    public GameState doRobotTurn() {
        gameState.move++;
        int column = findBestMove();
        int row = determineRow(column);

        gameState.matrix[row][column] = gameState.PLAYER_MAX;
        //sendPositions(takeCubeFrom(gameState.PLAYER_MAX), putCubeTo(turn.column));

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
        else if (gameState.score >= 2) {                          //playerMAX has won
            return gameState.score;
        }
        else if (gameState.score <= -2) {                          //playerMIN has won
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
        else if (gameState.score >= 2) {                       //playerMAX has won
            return gameState.score;
        }
        else if (gameState.score <= -2) {                       //playerMIN has won
            return gameState.score;
        }
        else if (!areMovesLeft()) {    //tie
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
        int streak;
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
        /*
        long startTime = ZonedDateTime.now().toInstant().toEpochMilli();
        //check the main diagonal and it's parallels
        for (int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
            for (int column = 0; column < (gameState.COLUMN_QUANTITY - 3); column++) {
                if (gameState.matrix[row][column] == player &&
                        gameState.matrix[row + 1][column + 1] == player &&
                        gameState.matrix[row + 2][column + 2] == player &&
                        gameState.matrix[row + 3][column + 3] == player) {
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
                    win_chances++;
                }
            }
        }
        long endTime = ZonedDateTime.now().toInstant().toEpochMilli();
        gameState.timeOldLoop += (endTime - startTime);
        */

        long startTime = ZonedDateTime.now().toInstant().toEpochMilli();
        //check the main diagonal and it's parallels
        boolean continue_loop;
        for (int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
            for (int column = 0; column < (gameState.COLUMN_QUANTITY - 3); column++) {
                streak = 0;
                continue_loop = false;
                // if the current position == player then next position of that diagonal gets checked
                // the loop breaks as soon as the first position of that diagonal != player
                // or when  4 positions of that diagonal == player
                do {

                    if (gameState.matrix[row + streak][column + streak] == player) {
                        streak++;
                        continue_loop = true;
                        if (streak >= 4) {      //at least 4 in a row
                            win_chances++;
                            continue_loop = false;
                        }
                    }
                    else {
                        continue_loop = false;
                    }
                } while(continue_loop);
            }
        }

        //check the antidiagonal and it's parallels
        for (int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
            for (int column = 3; column < gameState.COLUMN_QUANTITY; column++) {
                streak = 0;
                continue_loop = false;
                // if the current position == player then next position of that diagonal gets checked
                // the loop breaks as soon as the first position of that diagonal != player
                // or when  4 positions of that diagonal == player
                do {
                    if (gameState.matrix[row + streak][column - streak] == player) {
                        streak++;
                        continue_loop = true;
                        if (streak >= 4) {      //at least 4 in a row
                            win_chances++;
                            continue_loop = false;
                        }
                    }
                    else {
                        continue_loop = false;
                    }
                } while(continue_loop);
            }
        }
        long endTime = ZonedDateTime.now().toInstant().toEpochMilli();
        gameState.timeNewLoop += (endTime - startTime);

        if (win_chances > 0) {
            gameState.win = true;
        }

        if (player == gameState.PLAYER_MIN) {
            gameState.score = (-1) * win_chances;
        }
        else if (player == gameState.PLAYER_MAX) {
            gameState.score = win_chances;
        }
    }
    /*
    Position takeCubeFrom(int player) {
        int countYelCubes = 0;  //cubes for PlayerMIN
        int countRedCubes = 0;  //cubes for PlayerMAX

        if (player == gameState.PLAYER_MIN) { //take one of the players cubes from the magazine
            countYelCubes++;

            if(countYelCubes/2 == 1) {  //uneven
                sendPositions(, Position.MAG_TOP_YEL1);
            }
            else {  //even
                sendPositions(, Position.MAG_TOP_YEL2);
            }
        }
        else if (player == gameState.PLAYER_MAX) { //take one of the robots cubes from the magazine
            countRedCubes++;

            if(countRedCubes/2 == 1) {  //uneven
                sendPositions(, Position.MAG_TOP_YEL1);
            }
            else {  //even
                sendPositions(, Position.MAG_TOP_YEL2);
            }
        }
    }

    Position putCubeTo(int column) throws ConnectFourExeption {
        return Position.valueOfI(column + 5);
    }

    void removeCubes() {
        int countYelCubes = 0;  //cubes of PlayerMIN
        int countRedCubes = 0;  //cubes of PlayerMAX

        for (int row = 0; row < gameState.ROW_QUANTITY; row++) {
            for (int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
                if(gameState.matrix[row][column] == gameState.PLAYER_MIN) {
                    countYelCubes++;
                    //check if amount of cubes is even or uneven for alternating between magazines
                    if(countYelCubes/2 == 1) {  //uneven
                        sendPositions(, Position.MAG_TOP_YEL1);
                    }
                    else {  //even
                        sendPositions(, Position.MAG_TOP_YEL2);
                    }
                }
                else if (gameState.matrix[row][column] == gameState.PLAYER_MAX) {
                    countRedCubes++;
                    //check if amount of cubes is even or uneven for alternating between magazines
                    if(countRedCubes/2 == 1) {  //uneven
                        sendPositions(, Position.MAG_TOP_RED1);
                    }
                    else {  //even
                        sendPositions(, Position.MAG_TOP_RED2);
                    }
                }
            }
        }
    }

    void sendPositions(Position takeFrom, Position putTo) {
        boolean robotAcknowledged = false;

        while(robotAcknowledged = false) {
            //wait(500);
            robotAcknowledged = isAcknoledged();
        }
        sendNumberToRobot(takeFrom);
        while(robotAcknowledged = false) {
            //wait(500);
            robotAcknowledged = isAcknoledged();
        }
        sendNumberToRobot(putTo);
    }

    void sendNumberToRobot(Position programNumber) {

    }

    boolean isAcknoledged() {
        boolean acknoledged = false;

        return acknoledged;
    }
     */

    boolean areMovesLeft() {
        boolean moves_left = false;
        int max_possible_moves = gameState.COLUMN_QUANTITY * gameState.ROW_QUANTITY;

        if(gameState.move < max_possible_moves) {
            moves_left = true;
        }
        return moves_left;
    }
}
