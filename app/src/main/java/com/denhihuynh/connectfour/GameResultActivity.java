package com.denhihuynh.connectfour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.ArrayList;

import database.HighScoresDataSource;

public class GameResultActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GameResultActivity";
    public static final String RESULT = "result";
    public static final String RESULTTIE = "tie";
    public static final String RESULTWINNER = "winner";
    public static final String WINNERNAME = "winner";
    public static final String PLAYERNAMES = "playerNames";
    private ArrayList<String> playerNames;
    private HighScoresDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        Bundle extras = getIntent().getExtras();
        dataSource = new HighScoresDataSource(this);
        dataSource.open();
        Button setupButton = (Button) findViewById(R.id.setupButton);
        Button rematchButton = (Button) findViewById(R.id.rematchButton);
        setupButton.setOnClickListener(this);
        rematchButton.setOnClickListener(this);
        TextView bigResultTextView = (TextView) findViewById(R.id.gameResultBigText);
        TextView resultTextView = (TextView) findViewById(R.id.gameResultText);
        if (extras != null) {
            playerNames = extras.getStringArrayList(PLAYERNAMES);
            String result = extras.getString(RESULT);
            switch (result) {
                case RESULTWINNER:
                    bigResultTextView.setText("Winner");
                    String winner = extras.getString(RESULTWINNER);
                    String winText = "Congratulations, " + winner + " has won.";
                    resultTextView.setText(winText);
                    dataSource.addHighScore(winner, HighScoresDataSource.WIN);
                    if (winner != null) {
                        if (winner.equals(playerNames.get(0))) {
                            dataSource.addHighScore(playerNames.get(1), HighScoresDataSource.LOSS);
                        } else {
                            dataSource.addHighScore(playerNames.get(0), HighScoresDataSource.LOSS);
                        }
                    } else {
                        //This should not happen. Log message is written if it happens.
                        Log.e(TAG, "Winner pointed to null");
                    }
                    break;
                case RESULTTIE:
                    bigResultTextView.setText("Draw");
                    String drawText = "Game between " + playerNames.get(0) + " and " + playerNames.get(1) + " has ended in a draw.";
                    resultTextView.setText(drawText);
                    dataSource.addHighScore(playerNames.get(0), HighScoresDataSource.DRAW);
                    dataSource.addHighScore(playerNames.get(1), HighScoresDataSource.DRAW);
                    break;
            }

        } else {
            Log.e(TAG, "GameResultactivity did not get playerNames");
        }
        dataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finished_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setupButton:
                Intent setupIntent = new Intent(this, GameSetupActivity.class);
                startActivity(setupIntent);
                break;
            case R.id.rematchButton:
                Bundle extras = new Bundle();
                extras.putStringArrayList(PLAYERNAMES, playerNames);
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
