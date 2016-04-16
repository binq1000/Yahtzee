package me.tim.org.yahtzee;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nekkyou on 16-4-2016.
 */
public class CustomList extends ArrayAdapter<ScoreItem> {
    private final Activity context;
    private final List<ScoreItem> scores;

    public CustomList(Activity context, List<ScoreItem> scores) {
        super(context, R.layout.single_score, scores);
        this.context = context;
        this.scores = scores;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_score, null, true);
        TextView textKey = (TextView) rowView.findViewById(R.id.textKey);
        TextView textValue = (TextView) rowView.findViewById(R.id.textValue);

        ScoreItem scoreItem = scores.get(position);

        textKey.setText(scoreItem.getName());
        if (scoreItem.getScore() != -1) {
            textValue.setText(String.valueOf(scoreItem.getScore()));
        }
        return rowView;
    }
}
