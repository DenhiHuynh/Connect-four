package model;

/**
 * Position contains a position on the game board for connect four.
 */
public class Position {
    private int row;
    private int col;

    public Position(int row,int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol() {
        return col;
    }
}
