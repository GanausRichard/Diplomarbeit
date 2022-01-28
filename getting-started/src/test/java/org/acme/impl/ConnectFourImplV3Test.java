package org.acme.impl;

import org.acme.model.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectFourImplV3Test {

    @Test
    void checkForWin_v1() {
        int[][] matrix = {
                { 1, 2, 2, 1, 0, 0, 0 },
                { 0, 1, 2, 2, 0, 0, 0 },
                { 0, 0, 1, 2, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 }
        };
        testCheckForWin_v1(matrix, -1, true);
    }

    @Test
    void checkForWin_v1_2() {
        int[][] matrix = {
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 1, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 0, 0 },
                { 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 }
        };
        testCheckForWin_v1(matrix, -1, true);
    }

    @Test
    void checkForWin_v1_3() {
        int[][] matrix = {
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 1, 0, 0, 0, 0 },
                { 0, 1, 0, 1, 0, 0, 0 },
                { 1, 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 1, 0 },
                { 0, 0, 0, 0, 0, 0, 1 }
        };
        testCheckForWin_v1(matrix, -3, true);
    }

    @Test
    void checkForWin_v1_4() {
        int[][] matrix = {
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 1, 0 },
                { 0, 0, 0, 0, 0, 0, 1 }
        };
        testCheckForWin_v1(matrix, -1, true);
    }

    @Test
    void checkForWin_v1_5() {
        int[][] matrix = {
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 1, 0 },
                { 0, 0, 0, 0, 0, 0, 0 }
        };
        testCheckForWin_v1(matrix, -1, true);
    }

    @Test
    void checkForWin_v1_6() {
        int[][] matrix = {
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 1, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 0, 0 },
                { 1, 0, 0, 0, 0, 0, 0 }
        };
        testCheckForWin_v1(matrix, -1, true);
    }

    @Test
    void checkForWin_v1_7() {
        int[][] matrix = {
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 1, 0, 1, 0, 0 },
                { 0, 1, 0, 0, 0, 1, 0 },
                { 1, 0, 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0, 0, 0 }
        };
        testCheckForWin_v1(matrix, -2, true);
    }

    @Test
    void checkForWin_v1_8() {
        int[][] matrix = {
                { 0, 0, 0, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 0, 0, 0 }
        };
        testCheckForWin_v1(matrix, -2, true);
    }

    @Test
    void checkForWin_v1_9() {
        int[][] matrix = {
                { 1, 1, 1, 1, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 1, 1, 1 }
        };
        testCheckForWin_v1(matrix, -2, true);
    }

    @Test
    void checkForWin_v1_10() {
        int[][] matrix = {
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 }
        };
        testCheckForWin_v1(matrix, -2, true);
    }

    @Test
    void checkForWin_v1_11() {
        int[][] matrix = {
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 }
        };
        testCheckForWin_v1(matrix, -1, true);
    }

    @Test
    void checkForWin_v1_12() {
        int[][] matrix = {
                { 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 0, 0, 0, 0, 1 },
                { 1, 0, 0, 0, 0, 0, 1 },
                { 1, 0, 0, 0, 0, 0, 1 },
                { 1, 0, 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0, 0, 0 }
        };
        testCheckForWin_v1(matrix, -2, true);
    }



    void testCheckForWin_v1(int[][] matrix, int score, boolean win) {

        GameState gameState = new GameState();
        gameState.matrix = matrix;

        ConnectFourImplV3 connectFourImplV3 = new ConnectFourImplV3();
        connectFourImplV3.gameState = gameState;

        connectFourImplV3.checkForWin_v1(1);

        assertEquals(score, gameState.score);
        assertEquals(win, gameState.win);
    }
}
