package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.HighScore;

/**
 * This class handles all communication with the database.
 */
public class HighScoresDataSource {
    public static final int WIN = 0;
    public static final int DRAW = 1;
    public static final int LOSS = 2;


    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PLAYERNAME, MySQLiteHelper.COLUMN_WINS, MySQLiteHelper.COLUMN_DRAWS, MySQLiteHelper.COLUMN_LOSSES};


    public HighScoresDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    /**
     * Opens the database
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the database
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Adds a highscore to the database. If a user already exists, the gameresult is added to the database. Otherwise a new user and highscore is added.
     *
     * @param playerName the name of the player
     * @param gameResult the result of the last played game (WIN, DRAW or LOSS)
     * @return
     */
    public void addHighScore(String playerName, int gameResult) {
        //Check if database already has a player with name playerName
        Cursor cursor = database.query(MySQLiteHelper.TABLE_HIGHSCORES,
                allColumns, MySQLiteHelper.COLUMN_PLAYERNAME + " = '" + playerName + "'", null,
                null, null, null);

        if (cursor.getCount() > 0) { //There is a player called playerName
            HighScore highScore = cursorToHighScore(cursor);
            ContentValues values = new ContentValues();
            int win = highScore.getWins();
            int draw = highScore.getDraws();
            int loss = highScore.getLosses();
            switch (gameResult) {
                case WIN:
                    win++;
                    values.put(MySQLiteHelper.COLUMN_WINS, win);
                    break;
                case DRAW:
                    draw++;
                    values.put(MySQLiteHelper.COLUMN_DRAWS, draw);
                    break;
                case LOSS:
                    loss++;
                    values.put(MySQLiteHelper.COLUMN_LOSSES, loss);
                    break;
            }

            // Which row to update, based on the ID
            String selection = MySQLiteHelper.COLUMN_PLAYERNAME + " = ";
            String[] selectionArgs = {playerName};

            int count = database.update(
                    MySQLiteHelper.TABLE_HIGHSCORES,
                    values,
                    selection,
                    selectionArgs);
        }else{ //There was not a player called playerName. Create a new player highscore.
            ContentValues values = new ContentValues();
            int win = 0;
            int draw = 0;
            int loss = 0;
            switch (gameResult) {
                case WIN:
                    win++;
                    break;
                case DRAW:
                    draw++;
                    break;
                case LOSS:
                    loss++;
                    break;
            }
            values.put(MySQLiteHelper.COLUMN_PLAYERNAME,playerName);
            values.put(MySQLiteHelper.COLUMN_WINS, win);
            values.put(MySQLiteHelper.COLUMN_DRAWS, draw);
            values.put(MySQLiteHelper.COLUMN_LOSSES, loss);
            long insertId = database.insert(MySQLiteHelper.TABLE_HIGHSCORES, null,
                    values);
        }
        cursor.close();
    }

    /**
     * Gets all highscore from database and sorts them after wins.
     * @return the list of highscores for all players.
     */
    public List<HighScore> getAllHighScores() {
        List<HighScore> highScores = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_HIGHSCORES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HighScore highScore = cursorToHighScore(cursor);
            highScores.add(highScore);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Collections.sort(highScores);
        return highScores;
    }

    /**
     * Creates a HighScore object from the cursor values.
     * @param cursor the cursor to extract information from.
     * @return a HighScore object containt the highscore info for one player.
     */
    private HighScore cursorToHighScore(Cursor cursor) {
        String playerName = cursor.getString(1);
        int wins = cursor.getInt(2);
        int draws = cursor.getInt(3);
        int losses = cursor.getInt(4);
        HighScore highScore = new HighScore(playerName, wins, draws, losses);
        return highScore;
    }

}
