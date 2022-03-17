package org.acme.impl;

import de.re.easymodbus.exceptions.ModbusException;
import org.acme.model.*;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.IOException;
import java.util.UUID;

@Dependent
public class ConnectFourImpl {

    //gameState gets injected in class ConnectFourImpl
    @Inject
    GameState gameState;

    //the method startGame gets called once at the start of the game
    public GameState startGame(Settings settings, String sessionID) throws ConnectFourException, IOException {
        //if a session ID exists and it's not the session ID of the device of the user that was playing before
        //then an exception gets thrown
        if(gameState.sessionID != null && !sessionID.equals(gameState.sessionID)) {
            throw new ConnectFourException("Ein Benutzer ist bereits angemeldet!");
        }
        //a new session ID gets created if no user is currently playing
        //session ID and settings get inherited to the gameState object
        gameState.sessionID = UUID.randomUUID().toString();
        gameState.settings = settings;
        gameState.win = false;
        gameState.move = 0;
        gameState.score = 0;
        gameState.matrix = new int[gameState.ROW_QUANTITY][gameState.COLUMN_QUANTITY];
        //connect to modbusClient for codesys communication
        //gameState.modbusClient.Connect("172.31.1.146", 22);
        //configureAutExtInterface(); //for proper robot usage

        return gameState;
    }

    public GameState doTurn(Turn turn) throws ConnectFourException, IOException, ModbusException {
        gameState.move++;
        int row = determineRow(turn.column);
        int player = gameState.PLAYER_MIN;
        if(gameState.settings.gameMode.equals("pvp") && (gameState.move % 2) == 0) {
            player = gameState.PLAYER_MAX;
        }
        gameState.matrix[row][turn.column] = player;
        //sendPositions(takeCubeFrom(player), putCubeTo(turn.column));
        gameState.initialState = false;
        checkForWin(player, 0);
        return gameState;
    }

    public GameState doRobotTurn() throws ConnectFourException, IOException, ModbusException {
        gameState.move++;
        int column = findBestMove();
        int row = determineRow(column);

        gameState.matrix[row][column] = gameState.PLAYER_MAX;
        //sendPositions(takeCubeFrom(gameState.PLAYER_MAX), putCubeTo(column));

        checkForWin(gameState.PLAYER_MAX, 0);
        return gameState;
    }

    public GameState waitForInitialState() throws ConnectFourException, IOException, ModbusException {
        removeCubes();
        gameState.sessionID = null;
        //configureAutExtInterface(); //reset
        return gameState;
    }

    public int determineRow(int column) {
        int row;
        for(row = 0; row < gameState.ROW_QUANTITY; row++) {
            if(gameState.matrix[row][column] == 0) {
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

        for(int column = 0; column < gameState.COLUMN_QUANTITY && !column_checked; column++) {
            //determine in which row the tile should be placed
            for(int row = 0; row < gameState.ROW_QUANTITY; row++) {
                if(gameState.matrix[row][column] == 0) {  //check if matrix[row][column] is empty
                    ideal_column = column;        //out of matrix range for declaration
                    column_checked = true;
                    break;
                }
            }
        }

        for(int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            for(int row = 0; row < gameState.ROW_QUANTITY; row++) {       //determine row
                if(gameState.matrix[row][column] == 0) {       //check if matrix[row][column] is empty
                    gameState.matrix[row][column] = gameState.PLAYER_MAX;    //make a move
                    depth++;
                    current_move_score = minFunction(alpha, beta, depth);        //evaluate score of the current move
                    depth--;
                    gameState.matrix[row][column] = 0;        //undo the current move

                    //check if the score of the current move is better than the score of the best possible move
                    if(current_move_score > alpha) {
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
        checkForWin(gameState.PLAYER_MAX, depth);

        //end minFunction
        if(endMinMax(depth)) {
            return gameState.score;
        }

        for(int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            boolean column_checked = false;
            for(int row = 0; (row < gameState.ROW_QUANTITY) && !column_checked; row++) {       //determine row
                if(gameState.matrix[row][column] == 0) {
                    gameState.matrix[row][column] = gameState.PLAYER_MIN;        //make a move
                    depth++;
                    beta = Math.min(beta, maxFunction(alpha, beta, depth));
                    depth--;
                    gameState.matrix[row][column] = 0;                //undo the move

                    if(beta <= alpha) {
                        return alpha;
                    }
                    column_checked = true;        //does not repeat the for loop if column is already checked
                }
            }
        }
        return beta;
    }

    public int maxFunction(int alpha, int beta, int depth) { //represents max player's move
        gameState.score = 0;
        checkForWin(gameState.PLAYER_MIN, depth);

        //end maxFunction
        if(endMinMax(depth)) {
            return gameState.score;
        }

        for(int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            boolean column_checked = false;
            for(int row = 0; row < gameState.ROW_QUANTITY && !column_checked; row++) {        //determine row
                if(gameState.matrix[row][column] == 0) {
                    gameState.matrix[row][column] = gameState.PLAYER_MAX;        //make a move
                    depth++;
                    alpha = Math.max(alpha, minFunction(alpha, beta, depth));
                    depth--;
                    gameState.matrix[row][column] = 0;                //undo the move

                    if(alpha >= beta) {
                        return beta;
                    }
                    column_checked = true;  //does not repeat the for loop if column is already checked
                }
            }
        }
        return alpha;
    }

    public void checkForWin(int player, int depth) {    //checks every possibility for a win
        int streak;
        int win_chances = 0;
        int multiplier = 1 + (gameState.END_NODE - depth);    //can range from 1 to 11 (9 would be ideal)
        gameState.win = false;


        //check for 4 in a row
        for(int row = 0; row < gameState.ROW_QUANTITY; row++) {
            streak = 0;
            for(int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
                if(gameState.matrix[row][column] == player) {
                    streak++;
                    if(streak >= 4) {      //at least 4 in a row
                        win_chances += multiplier;
                    }
                }
                else {
                    streak = 0;
                }
            }
        }

        //check for 4 in a column
        for(int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
            streak = 0;
            for(int row = 0; row < gameState.ROW_QUANTITY; row++) {
                if(gameState.matrix[row][column] == player) {
                    streak++;
                    if(streak >= 4) {      //at least 4 in a row
                        win_chances += multiplier;
                    }
                }
                else {
                    streak = 0;
                }
            }
        }

        //check the main diagonal and it's parallels
        boolean continue_loop;
        for(int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
            for(int column = 0; column < (gameState.COLUMN_QUANTITY - 3); column++) {
                streak = 0;
                // if the current position == player then next position of that diagonal gets checked
                // the loop breaks as soon as the first position of that diagonal != player
                // or when  4 positions of that diagonal == player
                do {

                    if(gameState.matrix[row + streak][column + streak] == player) {
                        streak++;
                        continue_loop = true;
                        if(streak >= 4) {      //at least 4 in a row
                            win_chances += multiplier;
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
        for(int row = 0; row < (gameState.ROW_QUANTITY - 3); row++) {
            for(int column = 3; column < gameState.COLUMN_QUANTITY; column++) {
                streak = 0;
                // if the current position == player then next position of that diagonal gets checked
                // the loop breaks as soon as the first position of that diagonal != player
                // or when  4 positions of that diagonal == player
                do {
                    if(gameState.matrix[row + streak][column - streak] == player) {
                        streak++;
                        continue_loop = true;
                        if(streak >= 4) {      //at least 4 in a row
                            win_chances += multiplier;
                            continue_loop = false;
                        }
                    }
                    else {
                        continue_loop = false;
                    }
                } while(continue_loop);
            }
        }

        if(win_chances > 0) {
            gameState.win = true;
        }

        if(player == gameState.PLAYER_MIN) {
            gameState.score = (-1) * win_chances;
        }
        else if(player == gameState.PLAYER_MAX) {
            gameState.score = win_chances;
        }
    }

    boolean endMinMax(int depth) {
        //if end node reached or playerMAX/playerMIN has won or it's a tie
        return depth >= gameState.END_NODE || gameState.win || isMatrixFilled();
    }

    boolean isMatrixFilled() {
        boolean matrixFilled = false;
        int maxPossibleMoves = gameState.COLUMN_QUANTITY * gameState.ROW_QUANTITY;

        if(gameState.move >= maxPossibleMoves) {
            matrixFilled = true;
        }
        return matrixFilled;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    void removeCubes() throws ConnectFourException, IOException, ModbusException { //function completed
        for(int row = 0; row < gameState.ROW_QUANTITY; row++) {
            if(row == 0) {
                sendNumberToRobot(Position.RETURN_FIRST_ROW);
            }
            else {
                sendNumberToRobot(Position.RETURN_REMAINING_ROWS);
            }
            for(int column = 0; column < gameState.COLUMN_QUANTITY; column++) {
                if(gameState.matrix[row][column] == gameState.PLAYER_MIN) {
                    gameState.countYelCubes--;
                    //check if amount of cubes is even or uneven for alternating between magazines
                    if(gameState.countYelCubes/2 == 1) {  //uneven
                        sendPositions(Position.valueOfI(column + 12), Position.MAG_TOP_YEL1);
                    }
                    else {  //even
                        sendPositions(Position.valueOfI(column + 12), Position.MAG_TOP_YEL2);
                    }
                }
                else if(gameState.matrix[row][column] == gameState.PLAYER_MAX) {
                    gameState.countRedCubes--;
                    //check if amount of cubes is even or uneven for alternating between magazines
                    if(gameState.countRedCubes/2 == 1) {  //uneven
                        sendPositions(Position.valueOfI(column + 12), Position.MAG_TOP_RED1);
                    }
                    else {  //even
                        sendPositions(Position.valueOfI(column + 12), Position.MAG_TOP_RED2);
                    }
                }
            }
        }
        sendNumberToRobot(Position.RETURN_END);
        gameState.initialState = true;
    }

    void sendPositions(Position takeFrom, Position putTo) throws IOException, ModbusException { //function completed
        if(isAcknowledged()) {
            sendNumberToRobot(takeFrom);
        }
        if(isAcknowledged()) {
            sendNumberToRobot(putTo);
        }
        gameState.initialState = false;
    }

    Position takeCubeFrom(int player) { //function completed
        if(player == gameState.PLAYER_MIN) { //take one of the players cubes from the magazine
            gameState.countYelCubes++;

            if(gameState.countYelCubes/2 == 1) {  //uneven
                return Position.MAG_TOP_YEL1;
            }
            else {  //even
                return Position.MAG_TOP_YEL2;
            }
        }
        else { //take one of the robots cubes from the magazine
            gameState.countRedCubes++;

            if(gameState.countRedCubes/2 == 1) {  //uneven
                return Position.MAG_TOP_YEL1;
            }
            else {  //even
                return Position.MAG_TOP_YEL2;
            }
        }
    }

    Position putCubeTo(int column) throws ConnectFourException { //function completed
        return Position.valueOfI(column + 5);
    }

    void sendNumberToRobot(Position programNumber) throws IOException, ModbusException { //function needs to be verified
        //int bitNumber = Integer.bitCount(programNumber.value()); //counts all 1s of binary value of programNumber
        //boolean parity = (bitNumber % 2) == 1; //set parity bit for even parity

        //send programNumber.value() and parity to the robot
        gameState.modbusClient.WriteSingleRegister(0, programNumber.value());
    }

    boolean isAcknowledged() throws IOException, ModbusException {   //function almost finished (time needs to be specified and coil checked)
        //read acknowledge bit from robot and return it
        boolean[] inputCoils = gameState.modbusClient.ReadCoils(0, 1);
        while(!inputCoils[0]) {
            //wait(50);
        }
        return true;
    }

    void configureAutExtInterface(int pgno, int pgnoValid, boolean extStart, boolean moveEnable, boolean confMess, boolean drivesOn, boolean drivesOff) throws IOException, ModbusException { //function needs to be finished
        int[] outputRegisters = {pgno, pgnoValid};
        boolean[] outputCoils = {extStart, moveEnable, confMess, drivesOn, drivesOff};

        gameState.modbusClient.WriteMultipleRegisters(0, outputRegisters);
        gameState.modbusClient.WriteMultipleCoils(0, outputCoils);
    }
}
