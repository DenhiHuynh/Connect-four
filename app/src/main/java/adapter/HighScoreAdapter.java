package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.denhihuynh.connectfour.R;

import java.util.List;

import model.HighScore;


public class HighScoreAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<HighScore> highScores;

    public HighScoreAdapter(Context context, List<HighScore> highScores){
        mInflater = LayoutInflater.from(context);
        this.highScores = highScores;
    }

    @Override
    public int getCount() {
        return highScores.size();
    }

    @Override
    public Object getItem(int position) {
        return highScores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = mInflater.inflate(R.layout.row_layout_highscores, parent, false);
            holder = new ViewHolder();
            holder.playerName = (TextView) view.findViewById(R.id.playerName);
            holder.wins = (TextView) view.findViewById(R.id.wins);
            holder.draws = (TextView) view.findViewById(R.id.draws);
            holder.losses = (TextView) view.findViewById(R.id.losses);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        HighScore highScore = highScores.get(position);

        holder.playerName.setText(highScore.getPlayerName());
        holder.wins.setText("W: " + Integer.toString(highScore.getWins()));
        holder.draws.setText("D: " + Integer.toString(highScore.getDraws()));
        holder.losses.setText("L: " + Integer.toString(highScore.getLosses()));
        return view;
    }

    /**
     * Class to use view holder pattern.
     */
    private class ViewHolder {
        public TextView playerName, wins, draws, losses;
    }
}
