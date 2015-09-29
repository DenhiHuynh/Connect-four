package com.denhihuynh.connextfour;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import interfaces.OnFragmentInteractionListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class StartScreenFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;

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

    /**
     * Switches fragment when button is pressed.
     * @param v view pressed.
     */
    @Override
    public void onClick(View v) {
        String command = null;
        switch (v.getId()) {
            case R.id.startButton:
                command = MainActivity.START;
                break;
            case R.id.resumeButton:
                command = MainActivity.RESUME;
                break;
            case R.id.highScoreButton:
                command = MainActivity.HIGHSCORE;
                break;
        }
        if (mListener != null) {
            //Calling Mainactivity in order to switch fragment shown.
            mListener.onFragmentInteraction(command);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
