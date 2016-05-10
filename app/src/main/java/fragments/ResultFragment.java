package fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.denhihuynh.connectfour.GameActivity;
import com.denhihuynh.connectfour.GameSetupActivity;
import com.denhihuynh.connectfour.R;

import java.util.ArrayList;

import constants.SharedPreferenceConstants;
import database.HighScoresDataSource;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;

    private static final String TAG = "GameResultFragment";
    public static final String RESULT = "result";
    public static final String RESULTTIE = "tie";
    public static final String RESULTWINNER = "winner";
    public static final String PLAYERNAMES = "playerNames";
    private String result , winner;

    private ArrayList<String> playerNames;
    private HighScoresDataSource dataSource;
    private SharedPreferences prefs;
    private Button setupButton, rematchButton;
    private TextView bigResultTextView, resultTextView;


    public ResultFragment() {
        // Required empty public constructor
    }


    public static ResultFragment newInstance(String result, ArrayList<String> playerNames, String resultWinner) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(RESULT, result);
        args.putStringArrayList(PLAYERNAMES, playerNames);
        args.putString(RESULTWINNER, resultWinner);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playerNames = getArguments().getStringArrayList(PLAYERNAMES);
            result = getArguments().getString(RESULT);
            winner = getArguments().getString(RESULTWINNER);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        prefs = getActivity().getSharedPreferences(
                "com.denhihuynh.connectfour", Context.MODE_PRIVATE);

        dataSource = new HighScoresDataSource(getActivity());
        dataSource.open();
        setUpViews(rootView);
        displayInfo();
        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Setup for views
     */
    private void setUpViews(View rootView) {
        setupButton = (Button) rootView.findViewById(R.id.setupButton);
        rematchButton = (Button) rootView.findViewById(R.id.rematchButton);
        setupButton.setOnClickListener(this);
        rematchButton.setOnClickListener(this);
        bigResultTextView = (TextView) rootView.findViewById(R.id.gameResultBigText);
        resultTextView = (TextView) rootView.findViewById(R.id.gameResultText);
    }

    /**
     * Displays the correct info in the different views.
     */
    private void displayInfo() {

        switch (result) {
            case RESULTWINNER:
                bigResultTextView.setText(R.string.game_result_winner_big);
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
                String drawText = "Between " + playerNames.get(0) + " and " + playerNames.get(1) + ".";
                resultTextView.setText(drawText);
                dataSource.addHighScore(playerNames.get(0), HighScoresDataSource.DRAW);
                dataSource.addHighScore(playerNames.get(1), HighScoresDataSource.DRAW);
                break;
        }
        prefs.edit().putBoolean(SharedPreferenceConstants.HIGHSCOREEXISTS, true).apply();

        dataSource.close();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Handles if game should restart or setting up a new game.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setupButton:
                Intent setupIntent = new Intent(getActivity(), GameSetupActivity.class);
                startActivity(setupIntent);
                break;
            case R.id.rematchButton:
                Bundle extras = new Bundle();
                extras.putStringArrayList(PLAYERNAMES, playerNames);
                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
