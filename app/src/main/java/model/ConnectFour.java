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
            if (gameBoard[i][col] < 0) {
                gameBoard[i][col] = currentPlayer;
                return i;
            }
        }
        return COLISFULL;
    }

    /**
     * Evaluates the game to check if the current player won, if the board is full or just continued game.
     *
     * @return the number of the winner if there is one, otherwise a command
     * explaining the state of the game (FULLBOARD, CONTINUEDGAME).
     */
    public int evaluateGame(int row,int col) {
        //Check if current player has won
        boolean won = checkForWin(row,col);
        if (won) {
            return currentPlayer;
        }
        //Check if the board is full
        boolean fullBoard = true;
        for (int i = 0; i < rows; i++) {
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
     * Check if the last put disc resulted in a win.
     * @param r the last row index
     * @param c the last col index
     * @return if currentplayer has won or not.
     */
    private boolean checkForWin(int r, int c){
        int nDirs = 8;
        int xInc[] = {1, 1, 0, -1, -1, -1, 0,   1};
        int yInc[] = {0, 1, 1,  1,  0, -1, -1, -1};

        for(int i=0;i<nDirs; i++){
            if (winInDir(r, c, xInc[i], yInc[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the row index and col index given is inside gameboard.
     * @param r the row index
     * @param c the col index
     * @return if r and c is inside of the game board.
     */
    boolean inBound(int r, int c){
        return !(r>=rows || r<0 || c>=cols || c<0);
    }

    /**
     * Checks if there is a winning move in some direction given by (xInc,yInc)
     * @param r the last row index
     * @param c the last col index
     * @param rInc the direction to check in rows
     * @param cInc the direction to check in cols
     * @return if there is a win in the given direction of the board.
     */
    boolean winInDir(int r, int c, int rInc, int cInc){
        int inLine=0;
        while(inBound(r, c) && gameBoard[r][c] == currentPlayer){
            inLine++;
            r+=rInc;
            c+=cInc;
        }
        return inLine>=4;
    }



}
