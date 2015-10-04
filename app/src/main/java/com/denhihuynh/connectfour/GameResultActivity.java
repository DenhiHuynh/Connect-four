package com.denhihuynh.connectfour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import constants.SharedPreferenceConstants;
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
    private SharedPreferences prefs;
    private String[] audits;
    private Button setupButton,rematchButton;
    private TextView bigResultTextView,resultTextView;
    private ListView auditList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        prefs = this.getSharedPreferences(
                "com.denhihuynh.connectfour", Context.MODE_PRIVATE);

        dataSource = new HighScoresDataSource(this);
        dataSource.open();
        setUpViews();
        String auditString = prefs.getString(SharedPreferenceConstants.AUDITLOG, null);
        if(auditString != null){
            audits = auditString.split("\\r?\\n");
        }else{
            audits = new String[1];
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.row_layout_audit_log,R.id.textView,audits);
        auditList.setAdapter(adapter);
        displayInfo();
    }

    /**
     * Setup for views
     */
    private void setUpViews() {
        setupButton = (Button) findViewById(R.id.setupButton);
        rematchButton = (Button) findViewById(R.id.rematchButton);
        setupButton.setOnClickListener(this);
        rematchButton.setOnClickListener(this);
        bigResultTextView = (TextView) findViewById(R.id.gameResultBigText);
        resultTextView = (TextView) findViewById(R.id.gameResultText);
        auditList = (ListView) findViewById(R.id.auditList);
    }

    /**
     * Displays the correct info in the different views.
     */
    private void displayInfo() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerNames = extras.getStringArrayList(PLAYERNAMES);
            String result = extras.getString(RESULT);
            switch (result) {
                case RESULTWINNER:
                    bigResultTextView.setText(R.string.game_result_winner_big);
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
                    bigResultTextView.setText(R.string.gameresultactivity);
                    String drawText = "between " + playerNames.get(0) + " and " + playerNames.get(1) + ".";
                    resultTextView.setText(drawText);
                    dataSource.addHighScore(playerNames.get(0), HighScoresDataSource.DRAW);
                    dataSource.addHighScore(playerNames.get(1), HighScoresDataSource.DRAW);
                    break;
            }
            prefs.edit().putBoolean(SharedPreferenceConstants.HIGHSCOREEXISTS, true).apply();
        } else {
            Log.e(TAG, "GameResultActivity did not get playerNames");
        }
        dataSource.close();
    }

    /**
     * Handles if game should restart or setting up a new game.
     * @param v
     */
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

    /**
     * Backpress takes the user back to Mainactivity.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
