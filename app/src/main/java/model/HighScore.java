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
        if(this.wins - another.wins == 0){
            if(this.draws - another.draws == 0){
                if(this.losses - another.losses == 0){
                    return 0;
                }else{
                    return another.losses - this.losses;
                }
            }else{
                return this.draws - another.draws;
            }
        }
        return this.wins - another.wins;
    }
}
