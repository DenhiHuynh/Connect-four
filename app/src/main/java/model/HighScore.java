package model;

/**
 * Created by Hans-Johan on 2015-10-03.
 */
public class HighScore implements Comparable<HighScore>{
    private String playerName;
    private int wins;
    private int draws;
    private int losses;

    public HighScore(String playerName, int wins, int draws, int losses) {
        this.playerName = playerName;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
    }


    public String getPlayerName() {
        return playerName;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    @Override
    public int compareTo(HighScore another) {
        return this.wins - another.wins;
    }
}
