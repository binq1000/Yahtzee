package me.tim.org.yahtzee;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Score_Activity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Integer> scores;
    private DataController dataController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        scores = new ArrayList<>();
        dataController = new DataController(this);
        setListView();
    }

    private void setListView() {
        listView = (ListView) findViewById(R.id.scoreList);
        scores = (ArrayList<Integer>) dataController.getScores();

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, R.layout.single_score, R.id.textValue, scores);
        listView.setAdapter(adapter);
    }

}
