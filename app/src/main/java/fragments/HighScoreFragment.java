package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.denhihuynh.connectfour.R;

import java.util.List;

import adapter.HighScoreAdapter;
import database.HighScoresDataSource;
import model.HighScore;


public class HighScoreFragment extends Fragment {
    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private HighScoreAdapter mAdapter;

    private HighScoresDataSource dataSource;

    public static HighScoreFragment newInstance() {
        HighScoreFragment fragment = new HighScoreFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HighScoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new HighScoresDataSource(getContext());
        dataSource.open();
        List<HighScore> highScores = dataSource.getAllHighScores();
        mAdapter = new HighScoreAdapter(getContext(), highScores);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.highScoreList);
        mListView.setAdapter(mAdapter);

        return view;
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }


}
