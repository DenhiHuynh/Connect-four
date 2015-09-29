package com.denhihuynh.connextfour;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class StartScreenFragment extends Fragment implements View.OnClickListener {

    public StartScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_start_screen, container, false);
        Button startButton = (Button) root.findViewById(R.id.startButton);
        Button resumeButton = (Button) root.findViewById(R.id.resumeButton);
        Button highscoreButton = (Button) root.findViewById(R.id.highScoreButton);
        startButton.setOnClickListener(this);
        resumeButton.setOnClickListener(this);
        highscoreButton.setOnClickListener(this);
        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                Toast.makeText(getActivity(), "start",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.resumeButton:
                Toast.makeText(getActivity(), "resume",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.highScoreButton:
                Toast.makeText(getActivity(), "high",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Starts a new greed game.
     * @param view
     */
    public void startNewGame(View view) {
        Toast.makeText(getActivity(), "start new game",
                Toast.LENGTH_LONG).show();
        //switch fragment
    }

    /**
     * Resumes an old game instance.
     * @param view
     */
    public void resumeGame(View view) {
        Toast.makeText(getActivity(), "resume game",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Shows high score list.
     * @param view
     */
    public void showHighScore(View view) {
        Toast.makeText(getActivity(), "see highscore",
                Toast.LENGTH_LONG).show();
    }

}
