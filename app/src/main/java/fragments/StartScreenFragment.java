package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.denhihuynh.connectfour.MainActivity;
import com.denhihuynh.connectfour.R;

import java.util.ArrayList;

import constants.SharedPreferenceConstants;
import interfaces.OnFragmentInteractionListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class StartScreenFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private SharedPreferences prefs;

    public StartScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_start_screen, container, false);
        prefs = getActivity().getSharedPreferences(
                "com.denhihuynh.connectfour", Context.MODE_PRIVATE);
        Button setupButton = (Button) root.findViewById(R.id.setupButton);
        Button resumeButton = (Button) root.findViewById(R.id.resumeButton);
        setupButton.setOnClickListener(this);
        boolean onGoingGameExists = prefs.getBoolean(SharedPreferenceConstants.ONGOINGGAMEEXISTS,false);
        if(onGoingGameExists){
            resumeButton.setOnClickListener(this);
        }else{
            resumeButton.setAlpha(0.3f);
        }
        return root;
    }

    /**
     * Switches fragment when button is pressed.
     * @param v view pressed.
     */
    @Override
    public void onClick(View v) {
        String command = null;
        ArrayList<String> extras = null;
        switch (v.getId()) {
            case R.id.setupButton:
                command = MainActivity.SETUPGAME;
                break;
            case R.id.resumeButton:
                command = MainActivity.RESUME;
                break;
        }
        if (mListener != null) {
            //Calling Mainactivity in order to switch fragment.
            mListener.onFragmentInteraction(command,extras);
        }
    }

    /**
     * Connecting Mainactivity's onFragmentInteraction method with fragment in onAttach.
     * @param context
     */
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

    /**
     * Detaches Mainactivity's onFragmentInteraction method with fragment in onDetach.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
