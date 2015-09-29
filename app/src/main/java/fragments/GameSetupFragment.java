package fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.denhihuynh.connectfour.MainActivity;
import com.denhihuynh.connectfour.R;

import java.util.ArrayList;

import interfaces.OnFragmentInteractionListener;


public class GameSetupFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private EditText playerOneName, playerTwoName;

    public static GameSetupFragment newInstance() {
        GameSetupFragment fragment = new GameSetupFragment();
        return fragment;
    }

    public GameSetupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game_setup, container, false);
        Button startButton = (Button) root.findViewById(R.id.startButton);
        playerOneName = (EditText) root.findViewById(R.id.edittext_firstplayer);
        playerTwoName = (EditText) root.findViewById(R.id.edittext_secondplayer);
        startButton.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        String command = MainActivity.START;
        ArrayList<String> extras = new ArrayList<>();
        String firstPlayer = String.valueOf(playerOneName.getText());
        String secondPlayer = String.valueOf(playerTwoName.getText());
        extras.add(firstPlayer);
        extras.add(secondPlayer);
        //TODO Add name checks so players can not have the same name or empty string
        if (mListener != null) {
            mListener.onFragmentInteraction(command,extras);
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
