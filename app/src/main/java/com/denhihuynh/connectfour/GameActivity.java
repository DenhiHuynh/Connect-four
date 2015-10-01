package com.denhihuynh.connectfour;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import model.ConnectFour;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    final static long INTERVAL = 500;
    final static long TIMEOUT = 5000;
    private TableLayout tableLayout;
    private ArrayList<TableRow> gameBoardTable;
    private DiscDropper discDropper;
    private ConnectFour connectFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameBoardTable = new ArrayList<>();
        tableLayout = (TableLayout) findViewById(R.id.gameTable);
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
        //TODO fixa korrekt shite
        if (discDropper == null || discDropper.isFinished()) {
            discDropper = new DiscDropper(5, col, R.drawable.rounded_corner_red);
            discDropper.start();
        } else {
            discDropper.stop();
            discDropper = null;
        }
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
                if (rowIndex < destinationRowIndex) {
                    GameActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gameBoardTable.get(rowIndex).getChildAt(col).setBackgroundResource(R.drawable.rounded_corner_white);
                            gameBoardTable.get(rowIndex + 1).getChildAt(col).setBackgroundResource(drawableId);
                            rowIndex++;
                        }
                    });
                } else {
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

        if (rowIndex < 6)
            gameBoardTable.get(rowIndex).getChildAt(col).setBackgroundResource(R.drawable.rounded_corner_white);

        if (rowIndex < 5)
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
