
package org.acme.impl;

/*
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
        testCheckForWin_v1(matrix, -11, true);
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
        testCheckForWin_v1(matrix, -11, true);
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
        testCheckForWin_v1(matrix, -33, true);
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
        testCheckForWin_v1(matrix, -11, true);
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
        testCheckForWin_v1(matrix, -11, true);
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
        testCheckForWin_v1(matrix, -11, true);
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
        testCheckForWin_v1(matrix, -22, true);
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
        testCheckForWin_v1(matrix, -22, true);
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
        testCheckForWin_v1(matrix, -22, true);
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
        testCheckForWin_v1(matrix, -11, true);
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
        testCheckForWin_v1(matrix, -11, true);
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
        testCheckForWin_v1(matrix, -22, true);
    }

    void testCheckForWin_v1(int[][] matrix, int score, boolean win) {
        Settings settings = new Settings();
        GameState gameState = new GameState();
        gameState.matrix = matrix;
        gameState.settings.endNode = 10;

        ConnectFourImpl connectFourImpl = new ConnectFourImpl();
        connectFourImpl.gameState = gameState;

        connectFourImpl.checkForWin(1, 0);

        assertEquals(score, gameState.score);
        assertEquals(win, gameState.win);
    }
}
*/
