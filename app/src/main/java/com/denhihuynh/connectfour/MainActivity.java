package com.denhihuynh.connectfour;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import constants.SharedPreferenceConstants;
import fragments.GameTitleFragment;
import fragments.HighScoreFragment;
import fragments.StartScreenButtonFragment;
import interfaces.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    public static final String SETUPGAME = "setupGame";
    public static final String RESUME = "resume";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(
                "com.denhihuynh.connectfour", Context.MODE_PRIVATE);

        boolean highScoresExist = prefs.getBoolean(SharedPreferenceConstants.HIGHSCOREEXISTS,false);
        if(highScoresExist){
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, HighScoreFragment.newInstance()).commit();
        }else{
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, GameTitleFragment.newInstance()).commit();
        }
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.button_container, StartScreenButtonFragment.newInstance()).commit();

    }

    /**
     * Method used by fragments in order to communicate with the activity. Used to switch fragments.
     *
     * @param command
     */
    @Override
    public void onFragmentInteraction(String command, ArrayList<String> extras) {
        switch (command) {
            case SETUPGAME:
                Intent setupIntent = new Intent(this, GameSetupActivity.class);
                startActivity(setupIntent);
                break;
            case RESUME:
                Intent gameResumeIntent = new Intent(this, GameActivity.class);
                startActivity(gameResumeIntent);
                break;
        }

    }

    /**
     * Backpress checks if the user really want to quit.
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.quit)
                .setMessage(R.string.really_quit)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
