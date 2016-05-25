package com.denhihuynh.connectfour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import constants.SharedPreferenceConstants;
import fragments.ResultFragment;
import model.ConnectFour;
import model.Position;

/**
 * This activity handles the game animations and gameplay.
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener, ResultFragment.OnFragmentInteractionListener {
    private final static String TAG = "GameActivity";
    final static long INTERVAL = 100;
    private TableLayout tableLayout;
    private ArrayList<TableRow> gameBoardTable;
    private StringBuilder auditLog;
    private DiscDropper discDropper;
    private ConnectFour connectFour;
    private int rows, cols;
    private int currentPlayer;
    private TextView currentPlayerText;
    private ArrayList<String> playerNames;
    private int lastRow, lastCol;
    private SharedPreferences prefs;
    private boolean finishedGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rows = 6;
        cols = 7;
        finishedGame = false;
        gameBoardTable = new ArrayList<>();
        auditLog = new StringBuilder();
        tableLayout = (TableLayout) findViewById(R.id.gameTable);
        currentPlayerText = (TextView) findViewById(R.id.currentPlayerName);
        prefs = this.getSharedPreferences(
                "com.denhihuynh.connectfour", Context.MODE_PRIVATE);
        boolean onGoingGameExists = prefs.getBoolean(SharedPreferenceConstants.ONGOINGGAMEEXISTS, false);
        addGameBoardRows();
        if (onGoingGameExists) {
            createResumedGame();
        } else {
            prefs.edit().putBoolean(SharedPreferenceConstants.ONGOINGGAMEEXISTS, true).apply();

            //Using a normal 6x7 game board with two players.
            connectFour = new ConnectFour(rows, cols, 2, prefs);
            currentPlayer = 0;
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                playerNames = extras.getStringArrayList(GameSetupActivity.PLAYERNAMES);
            }
            currentPlayerText.setText(playerNames.get(0));
        }

    }

    /**
     * Restores a started game.
     */
    private void createResumedGame() {
        //Using a normal 6x7 game board with two players.
        connectFour = new ConnectFour(rows, cols, 2, prefs);
        String audits = prefs.getString(SharedPreferenceConstants.AUDITLOG, null);
        if (audits != null) {
            auditLog.append(audits);
        } else {
            Log.d(TAG, "Audit log was null.");
        }
        boolean successFullyRecreatedGame = connectFour.getGameInstance();
        if (successFullyRecreatedGame) {
            String playerOne = prefs.getString(SharedPreferenceConstants.PLAYERONENAME, null);
            String playerTwo = prefs.getString(SharedPreferenceConstants.PLAYERTWONAME, null);
            currentPlayer = prefs.getInt(SharedPreferenceConstants.GAMEACTIVITYCURRENTPLAYER, 0);
            if (currentPlayer == 0) {
                currentPlayerText.setText(playerOne);
            } else {
                currentPlayerText.setText(playerTwo);
            }
            if (playerOne != null && playerTwo != null) {
                playerNames = new ArrayList<>();
                playerNames.add(playerOne);
                playerNames.add(playerTwo);
            } else {
                //This should not happen. Debug message is shown in case of this happening.
                Log.d(TAG, "Player names was null");
            }
        } else {
            //This should not happen. Debug message is shown in case of this happening.
            Log.d(TAG, "Connect four game was not created correctly");
        }

        int[][] gameBoard = connectFour.getGameBoard();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                TableRow row = gameBoardTable.get(rows - 1 - i);
                if (gameBoard[i][j] == 0) {
                    row.getChildAt(j).setBackgroundResource(R.drawable.rounded_corner_red);
                } else if (gameBoard[i][j] == 1) {
                    row.getChildAt(j).setBackgroundResource(R.drawable.rounded_corner_yellow);
                }
            }
        }
    }


    /**
     * Adds rows to the tablelayout which represents the game board.
     */
    private void addGameBoardRows() {
        LayoutInflater inflater = getLayoutInflater();
        tableLayout.setStretchAllColumns(true);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        tableLayout.setWeightSum(6);
        for (int i = 0; i < 6; i++) {
            TableRow tableRow = (TableRow) inflater.inflate(R.layout.tablerow_game, null, false);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            Button col1 = (Button) tableRow.getChildAt(0);
            Button col2 = (Button) tableRow.getChildAt(1);
            Button col3 = (Button) tableRow.getChildAt(2);
            Button col4 = (Button) tableRow.getChildAt(3);
            Button col5 = (Button) tableRow.getChildAt(4);
            Button col6 = (Button) tableRow.getChildAt(5);
            Button col7 = (Button) tableRow.getChildAt(6);
            col1.setOnClickListener(this);
            col2.setOnClickListener(this);
            col3.setOnClickListener(this);
            col4.setOnClickListener(this);
            col5.setOnClickListener(this);
            col6.setOnClickListener(this);
            col7.setOnClickListener(this);
            tableLayout.addView(tableRow);
            gameBoardTable.add(tableRow);
        }
    }


    @Override
    public void onClick(View v) {
        if (!finishedGame) {
            int col = 0;
            switch (v.getId()) {
                case R.id.button_column_1:
                    col = 0;
                    break;
                case R.id.button_column_2:
                    col = 1;
                    break;
                case R.id.button_column_3:
                    col = 2;
                    break;
                case R.id.button_column_4:
                    col = 3;
                    break;
                case R.id.button_column_5:
                    col = 4;
                    break;
                case R.id.button_column_6:
                    col = 5;
                    break;
                case R.id.button_column_7:
                    col = 6;
                    break;
            }
            dropDisc(col);
        }
    }

    /**
     * Method to perform dropping disc animation.
     *
     * @param col the column to drop a disc into
     */
    private void dropDisc(final int col) {
        if (discDropper == null || discDropper.isFinished()) {
            int lastAddedRow = connectFour.addToColumn(col);
            lastCol = col;
            lastRow = lastAddedRow;
            String playerName;
            if (currentPlayer == 0) {
                playerName = playerNames.get(0);
            } else {
                playerName = playerNames.get(1);
            }
            //Using 1 indexing on row and column in audit log.
            String audit = "Player " + playerName + " added disc to row: " + (lastRow + 1) + ", column: " + (lastCol + 1) + ".\n";
            auditLog.append(audit);
            int destinationRowIndex = rows - 1 - lastAddedRow;
            if (lastAddedRow != ConnectFour.COLISFULL) {
                if (currentPlayer == 0) {
                    discDropper = new DiscDropper(destinationRowIndex, col, R.drawable.rounded_corner_red);
                } else {
                    discDropper = new DiscDropper(destinationRowIndex, col, R.drawable.rounded_corner_yellow);
                }
                discDropper.start();
            } else {
                Toast.makeText(getApplicationContext(), "Column is full",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            discDropper.stop();
            discDropper = null;
        }
    }

    /**
     * Evaluates the game board to check if its time for a new player, gameboard is full or if a winner exists.
     */
    private void evaluateGame() {
        final int action = connectFour.evaluateGame(lastRow, lastCol);
        switch (action) {
            case ConnectFour.FULLBOARD:
                finishedGame = true;
                prefs.edit().putString(SharedPreferenceConstants.AUDITLOG, auditLog.toString()).apply();
                prefs.edit().putBoolean(SharedPreferenceConstants.ONGOINGGAMEEXISTS, false).apply();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.result_screen_anim_in, R.anim.result_screen_anim_out)
                        .add(R.id.game_Container, ResultFragment.newInstance(ResultFragment.RESULTTIE, playerNames, null)).commit();

                break;
            case ConnectFour.CONTINUEDGAME:
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentPlayer = (currentPlayer + 1) % 2;

                        if (currentPlayer == 0) {
                            currentPlayerText.setText(playerNames.get(0));
                        } else {
                            currentPlayerText.setText(playerNames.get(1));
                        }
                    }
                });
                break;
            default: //This means we have a winner
                // Add the fragment to the 'fragment_container' FrameLayout
                finishedGame = true;
                prefs.edit().putString(SharedPreferenceConstants.AUDITLOG, auditLog.toString()).apply();
                prefs.edit().putBoolean(SharedPreferenceConstants.ONGOINGGAMEEXISTS, false).apply();
                String winner;
                if (currentPlayer == 0) {
                    winner = playerNames.get(0);
                } else {
                    winner = playerNames.get(1);
                }
                ArrayList<Position> winPositions = connectFour.getWinPosition();
                final int winnerDrawable = currentPlayer == 0 ? R.drawable.rounded_corner_red_win : R.drawable.rounded_corner_yellow_win;
                for (Position p : winPositions) {
                    final int row = rows - 1 - p.getRow();
                    final int col = p.getCol();
                    GameActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gameBoardTable.get(row).getChildAt(col).setBackgroundResource(winnerDrawable);
                        }
                    });
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.result_screen_anim_in, R.anim.result_screen_anim_out)
                        .add(R.id.game_Container, ResultFragment.newInstance(ResultFragment.RESULTWINNER, playerNames, winner)).commit();

                break;

        }
    }

    /**
     * Saving game on back press.
     */
    @Override
    public void onBackPressed() {
        saveGameBoard();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Saving game on onPause.
     */
    @Override
    public void onPause() {
        super.onPause();
        saveGameBoard();
    }

    /**
     * Saves the current state of the game.
     */
    private void saveGameBoard() {
        prefs.edit().putString(SharedPreferenceConstants.AUDITLOG, auditLog.toString()).apply();
        prefs.edit().putInt(SharedPreferenceConstants.GAMEACTIVITYCURRENTPLAYER, currentPlayer).apply();
        prefs.edit().putString(SharedPreferenceConstants.PLAYERONENAME, playerNames.get(0)).apply();
        prefs.edit().putString(SharedPreferenceConstants.PLAYERTWONAME, playerNames.get(1)).apply();
        connectFour.saveGameInstance();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO add interaction
    }

    /**
     * Class for handling dropping of discs into game board.
     */
    private class DiscDropper {
        private TimerTask timerTask;
        private int rowIndex;
        private int col;
        private int drawableId;
        private int destinationRowIndex;
        private boolean isFinished;
        private Timer timer;

        public DiscDropper(final int destinationRowIndex, final int col, final int drawableId) {
            this.col = col;
            this.drawableId = drawableId;
            this.destinationRowIndex = destinationRowIndex;
            rowIndex = 0;
            isFinished = false;
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (rowIndex < destinationRowIndex && destinationRowIndex >= 0) {
                        GameActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gameBoardTable.get(rowIndex).getChildAt(col).setBackgroundResource(R.drawable.rounded_corner_white);
                                gameBoardTable.get(rowIndex + 1).getChildAt(col).setBackgroundResource(drawableId);
                                rowIndex++;
                            }
                        });
                    } else {
                        int rowIndex = 0;
                        while (rowIndex < 6 && rowIndex < destinationRowIndex) {
                            gameBoardTable.get(rowIndex).getChildAt(col).setBackgroundResource(R.drawable.rounded_corner_white);
                            rowIndex++;
                        }
                        evaluateGame();
                        isFinished = true;
                        cancel();
                    }
                }
            };
        }

        /**
         * Starts the discdropper. If the disc dropper is already started, then the disc is instantly dropped to the bottom.
         */
        public void start() {
            gameBoardTable.get(0).getChildAt(col).setBackgroundResource(drawableId);
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, INTERVAL, INTERVAL);
        }

        /**
         * Instantly drops the disc to the correct position
         */
        public void stop() {
            timerTask.cancel();
            timer.cancel();
            int rowIndex = 0;
            while (rowIndex < 6 && rowIndex < destinationRowIndex) {
                gameBoardTable.get(rowIndex).getChildAt(col).setBackgroundResource(R.drawable.rounded_corner_white);
                rowIndex++;
            }
            evaluateGame();
            if (!finishedGame) {
                gameBoardTable.get(destinationRowIndex).getChildAt(col).setBackgroundResource(drawableId);
            }
        }

        /**
         * Checks if the timertask has finished.
         *
         * @return if the timertask has finished.
         */
        public boolean isFinished() {
            return isFinished;
        }

    }
}
