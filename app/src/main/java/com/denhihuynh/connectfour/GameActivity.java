package com.denhihuynh.connectfour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import model.ConnectFour;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    final static long INTERVAL = 200;
    private TableLayout tableLayout;
    private ArrayList<TableRow> gameBoardTable;
    private DiscDropper discDropper;
    private ConnectFour connectFour;
    private int rows, cols;
    private int currentPlayer;
    private TextView currentPlayerText;
    private Button currentPlayerColor;
    private ArrayList<String> playerNames;
    private int lastRow,lastCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameBoardTable = new ArrayList<>();
        tableLayout = (TableLayout) findViewById(R.id.gameTable);
        rows = 6;
        cols = 7;
        //Using a normal 6x7 game board with two players.
        connectFour = new ConnectFour(rows, cols, 2);
        currentPlayer = 0;
        currentPlayerColor = (Button) findViewById(R.id.currentPlayerColorButton);
        currentPlayerText = (TextView) findViewById(R.id.currentPlayerNumberText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerNames = extras.getStringArrayList(GameSetupActivity.PLAYERNAMES);
        }

        addGameBoardRows();
    }


    /**
     * Adds rows to the tablelayout which represents the game board.
     */
    private void addGameBoardRows() {
        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < 6; i++) {
            TableRow rowView = (TableRow) inflater.inflate(R.layout.tablerow_game, null, false);
            Button col1 = (Button) rowView.getChildAt(0);
            Button col2 = (Button) rowView.getChildAt(1);
            Button col3 = (Button) rowView.getChildAt(2);
            Button col4 = (Button) rowView.getChildAt(3);
            Button col5 = (Button) rowView.getChildAt(4);
            Button col6 = (Button) rowView.getChildAt(5);
            col1.setOnClickListener(this);
            col2.setOnClickListener(this);
            col3.setOnClickListener(this);
            col4.setOnClickListener(this);
            col5.setOnClickListener(this);
            col6.setOnClickListener(this);
            tableLayout.addView(rowView);
            gameBoardTable.add(rowView);
        }
    }


    @Override
    public void onClick(View v) {
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
        }
        dropDisc(col);
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
                Bundle extra = new Bundle();
                extra.putStringArrayList(GameResultActivity.PLAYERNAMES,playerNames);
                extra.putString(GameResultActivity.RESULT, GameResultActivity.RESULTTIE);
                Intent tieIntent = new Intent(this,GameResultActivity.class);
                tieIntent.putExtras(extra);
                startActivity(tieIntent);
                break;
            case ConnectFour.CONTINUEDGAME:
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentPlayer = (currentPlayer + 1) % 2;
                        currentPlayerText.setText(Integer.toString(currentPlayer + 1));
                        if (currentPlayer == 0) {
                            currentPlayerColor.setBackgroundResource(R.drawable.rounded_corner_red);
                        } else {
                            currentPlayerColor.setBackgroundResource(R.drawable.rounded_corner_yellow);
                        }
                    }
                });
                break;
            default: //This means we have a winner
                Bundle extras = new Bundle();
                extras.putString(GameResultActivity.RESULT, GameResultActivity.RESULTWINNER);
                extras.putStringArrayList(GameResultActivity.PLAYERNAMES,playerNames);
                if(currentPlayer == 0){
                    extras.putString(GameResultActivity.WINNERNAME,playerNames.get(0));
                }else{
                    extras.putString(GameResultActivity.WINNERNAME,playerNames.get(1));
                }
                Intent winIntent = new Intent(this,GameResultActivity.class);
                winIntent.putExtras(extras);
                startActivity(winIntent);
                break;

        }
    }

    @Override
    public void onBackPressed()
    {
        //TODO add savegameboard
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, INTERVAL, INTERVAL);
        }

        /**
         * Instantly drops the disc to the correct position
         */
        public void stop() {
            timerTask.cancel();
            evaluateGame();
            if (rowIndex < 6 && rowIndex < destinationRowIndex)
                gameBoardTable.get(rowIndex).getChildAt(col).setBackgroundResource(R.drawable.rounded_corner_white);

            if (rowIndex < 5 && rowIndex < destinationRowIndex - 1)
                gameBoardTable.get(rowIndex + 1).getChildAt(col).setBackgroundResource(R.drawable.rounded_corner_white);

            gameBoardTable.get(destinationRowIndex).getChildAt(col).setBackgroundResource(drawableId);
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
