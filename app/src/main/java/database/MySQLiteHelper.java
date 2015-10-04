package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_HIGHSCORES = "highScores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYERNAME = "playerName";
    public static final String COLUMN_WINS = "wins";
    public static final String COLUMN_DRAWS = "draws";
    public static final String COLUMN_LOSSES = "losses";

    private static final String DATABASE_NAME = "highScores.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_HIGHSCORES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PLAYERNAME
            + " text not null, " + COLUMN_WINS + " integer, " + COLUMN_DRAWS + " integer, " + COLUMN_LOSSES + " integer" + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);
        onCreate(db);
    }
}
