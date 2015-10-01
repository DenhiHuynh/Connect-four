package model;

import java.util.ArrayList;

/**
 * This class is a representation of a connect four game.
 */
public class ConnectFour {
    public static final int FULLBOARD = -1;
    public static final int CONTINUEDGAME = -2;
    public static final int COLISFULL = -3;

    //Gameboard[row][col] is represented with row index 0 as bottom row
    // and col index 0 as leftmost column
    private int[][] gameBoard;
    private final int rows, cols, nbrOfPlayers;
    private int currentPlayer;

    public ConnectFour(int rows, int cols, int nbrOfPlayers) {
        this.rows = rows;
        this.cols = cols;
        this.nbrOfPlayers = nbrOfPlayers;
        currentPlayer = 0;
        gameBoard = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gameBoard[i][j] = -1;
            }
        }
    }

    /**
     * Tries to add a disc to the desired column.
     *
     * @param col is the column index.
     * @return which row it was added to, if all rows are full the method returns COLISFULL.
     */
    public int addToColumn(int col) {
        for (int i = 0; i < rows; i++) {
            if (gameBoard[i][col] == -1) {
                gameBoard[i][col] = currentPlayer;
                return i;
            }
        }
        return COLISFULL;
    }

    /**
     * Evaluates the game to check if the current player won.
     * When check is done, its time for the next player.
     *
     * @return the number of the winner if there is one, otherwise a command
     * explaining the state of the game (FULLBOARD, CONTINUEDGAME).
     */
    public int evaluateGame() {
        //Check if current player has won
        boolean won = checkForWin();
        if (won) {
            return currentPlayer;
        }
        //Check if the board is full
        boolean fullBoard = true;
        for (int i = 0; i < cols; i++) {
            if (gameBoard[rows - 1][i] == -1) {
                fullBoard = false;
            }
        }
        if (fullBoard) {
            return FULLBOARD;
        }
        //The board is not full nor has the current player won. The game continues for the next player.
        currentPlayer = ++currentPlayer % nbrOfPlayers;
        return CONTINUEDGAME;
    }

    /**
     * Help method to check if the current player has won.
     *
     * @return
     */
    private boolean checkForWin() {
        //Horizontal check
        for (int i = 0; i < rows; i++) {
            int nInLine = 0;
            for (int j = 0; j < cols; j++) {
                if (gameBoard[i][j] == currentPlayer) {
                    nInLine++;
                    if (nInLine == 4) {
                        return true;
                    }
                } else {
                    nInLine = 0;
                }
            }
        }

        //Vertical check
        for (int i = 0; i < cols; i++) {
            int nInLine = 0;
            for (int j = 0; j < rows; j++) {
                if (gameBoard[j][i] == currentPlayer) {
                    nInLine++;
                    if (nInLine == 4) {
                        return true;
                    }
                } else {
                    nInLine = 0;
                }
            }
        }

        //Diagonal check
        for (int c = 0; c < 4; c++) {
            int diag = 0;
            int nInLineUp = 0;
            int nInLineDown = 0;
            while (diag < rows && c + diag < cols) {
                if (gameBoard[diag][c + diag] == currentPlayer) {
                    nInLineUp++;
                    if (nInLineUp == 4) {
                        return true;
                    }
                } else {
                    nInLineUp = 0;
                }
                if (gameBoard[rows - 1 - diag][c + diag] == currentPlayer) {
                    nInLineDown++;
                    if (nInLineDown == 4) {
                        return true;
                    }
                } else {
                    nInLineDown = 0;
                }
                diag++;
            }
        }
        for (int r = 1; r < 3; r++) {
            int diag = 0;
            int nInLineUp = 0;
            int nInLineDown = 0;
            while (r + diag < rows && diag < cols) {
                if (gameBoard[r + diag][diag] == currentPlayer) {
                    nInLineUp++;
                    if (nInLineUp == 4) {
                        return true;
                    }
                } else {
                    nInLineUp = 0;
                }
                if (gameBoard[rows - 1 - diag][diag] == currentPlayer) {
                    nInLineDown++;
                    if (nInLineDown == 4) {
                        return true;
                    }
                } else {
                    nInLineUp = 0;
                }
                diag++;
            }
        }
        return false;
    }

}
