package model;

/**
 * Object containing the scores for a specific player. Used in database.
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

    /**
     * Compares this highscore against another one. First checks the number of wins.
     * If equal, it checks draws and at last which player has lost fewest games.
     * @param another the other highscore to compare to.
     * @return
     */
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
