package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.denhihuynh.connectfour.R;

import java.util.ArrayList;

import interfaces.OnFragmentInteractionListener;

public class GameFragment extends Fragment {
    private static final String PLAYERNAMES = "playerNames";

    private ArrayList<String> playerNames;
    private OnFragmentInteractionListener mListener;
    private TableLayout tableLayout;

    public static GameFragment newInstance(ArrayList<String> playerNames) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(PLAYERNAMES, playerNames);
        fragment.setArguments(args);
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (getArguments() != null) {
            playerNames = getArguments().getStringArrayList(PLAYERNAMES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game, container, false);
        tableLayout = (TableLayout) root.findViewById(R.id.gameTable);
        addGameBoardRows(inflater);
        return root;
    }

    /**
     * Adds rows to the tablelayout which represents the game board.
     */
    private void addGameBoardRows(LayoutInflater inflater) {
        for(int i = 0;i<6;i++){
            View rowView = inflater.inflate(R.layout.tablerow_game, null, false);
            tableLayout.addView(rowView);
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
