package model;

import android.content.SharedPreferences;

import constants.SharedPreferenceConstants;

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

    private SharedPreferences prefs;

    public ConnectFour(int rows, int cols, int nbrOfPlayers, SharedPreferences prefs) {
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
        this.prefs = prefs;
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
    public int evaluateGame(int row, int col) {
        //Check if current player has won
        boolean won = checkForWin(row, col);
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
     *
     * @param r the last row index
     * @param c the last col index
     * @return if currentplayer has won or not.
     */
    private boolean checkForWin(int r, int c) {
        int nDirs = 8;
        int xInc[] = {1, -1, 0, 0, 1, -1, 1, -1};
        int yInc[] = {0, 0, 1, -1, 1, -1, -1, 1};

        //Check for four in line in a specific direction
        for (int i = 0; i < nDirs; i++) {
            if (winInDir(r, c, xInc[i], yInc[i])) {
                return true;
            }
        }

        //Check for four in line combined in two directions
        for(int j = 0; j < nDirs; j += 2){
            if (nbrInDir(r, c, xInc[j], yInc[j]) + nbrInDir(r, c, xInc[j+1], yInc[j+1]) >= 5) {//5 because point gameBoard[r][c] will be counted twice
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the number of disc in order which belong to a user.
     *
     * @param r
     * @param c
     * @param rInc
     * @param cInc
     * @return
     */
    int nbrInDir(int r, int c, int rInc, int cInc) {
        int inLine = 0;
        while (inBound(r, c) && gameBoard[r][c] == currentPlayer) {
            inLine++;
            r += rInc;
            c += cInc;
        }
        return inLine;
    }

    /**
     * Check if the row index and col index given is inside gameboard.
     *
     * @param r the row index
     * @param c the col index
     * @return if r and c is inside of the game board.
     */
    boolean inBound(int r, int c) {
        return !(r >= rows || r < 0 || c >= cols || c < 0);
    }

    /**
     * Checks if there is a winning move in some direction given by (xInc,yInc)
     *
     * @param r    the last row index
     * @param c    the last col index
     * @param rInc the direction to check in rows
     * @param cInc the direction to check in cols
     * @return if there is a win in the given direction of the board.
     */
    boolean winInDir(int r, int c, int rInc, int cInc) {
        int inLine = 0;
        while (inBound(r, c) && gameBoard[r][c] == currentPlayer) {
            inLine++;
            r += rInc;
            c += cInc;
        }
        return inLine >= 4;
    }

    /**
     * Saves the game instance to shared preferences.
     */
    public void saveGameInstance() {
        prefs.edit().putInt(SharedPreferenceConstants.CONNECTFOURCURRENTPLAYER, currentPlayer).apply();
        prefs.edit().putString(SharedPreferenceConstants.CONNECTFOURGAMEBOARD, gameBoardToString()).apply();
    }

    /**
     * Creates a previously saved game instance.
     */
    public boolean getGameInstance() {
        currentPlayer = prefs.getInt(SharedPreferenceConstants.CONNECTFOURCURRENTPLAYER, 0);
        String gameBoardString = prefs.getString(SharedPreferenceConstants.CONNECTFOURGAMEBOARD, null);
        if (gameBoardString != null) {
            stringToGameBoard(gameBoardString);
            return true;
        } else {
            return false;
        }
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    /**
     * Creates a string representation out of the game board, which can be saved into SharedPreferences
     *
     * @return
     */
    private String gameBoardToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(gameBoard[i][j] + ",");
            }
            sb.append(";");
        }
        return sb.toString();
    }

    /**
     * Creates the int representation of the gameBoard from a String which was saved in sharedpreferences
     *
     * @param gameBoardString
     * @return
     */
    private void stringToGameBoard(String gameBoardString) {
        String[] rows = gameBoardString.split(";");
        for (int i = 0; i < rows.length; i++) {
            String[] entry = rows[i].split(",");
            for (int j = 0; j < entry.length; j++) {
                gameBoard[i][j] = Integer.parseInt(entry[j]);
            }
        }
    }
}
