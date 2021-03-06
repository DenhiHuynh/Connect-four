package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.denhihuynh.connectfour.R;

/**
 * This fragments shows the title in Startscreen(MainActivity) when there is no record for highscores.
 */
public class GameTitleFragment extends Fragment {

    public static GameTitleFragment newInstance() {
        GameTitleFragment fragment = new GameTitleFragment();
        return fragment;
    }

    public GameTitleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_title, container, false);
    }
}
