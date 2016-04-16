package me.tim.org.yahtzee;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by Nekkyou on 29-3-2016.
 */
public class NumberController {

    private Activity activity;
    private ArrayList<ToggleButton> toggleButtons;

    private boolean firstThrow = true;
    private int throwAmount;
    private ListView listView;
    private List<ScoreItem> scoreItems;
    private CustomList adapter;

    public NumberController(Activity activity) {

        TextView textView = (TextView) activity.findViewById(R.id.totalTextValue);
        textView.setText("0");

        this.activity = activity;

        toggleButtons = new ArrayList<>();

        setListeners();
        toggleEnableToggleButton(false);
        throwAmount = 0;

        setListView();
    }


    private void setListView() {
        listView = (ListView) activity.findViewById(R.id.list);
        scoreItems = getScoreKeys();
        adapter = new CustomList(activity, scoreItems);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO on item clicked
                if (!firstThrow) {
                    processScores(scoreItems.get(+position));
                }
            }
        });
    }

    private void processScores(ScoreItem scoreItem) {
        if (scoreItem.getScore() != -1) {
            return;
        }

        List<Integer> numbers = new ArrayList<>();
        ListIterator<ToggleButton> it = toggleButtons.listIterator();
        while (it.hasNext()) {
            ToggleButton toggleButton = it.next();
            numbers.add(Integer.valueOf(toggleButton.getText().toString()));
        }

        int value = 0;

        switch (scoreItem.getName()) {
            case "Ones":
                value = Collections.frequency(numbers, 1);
                scoreItem.setScore(value);
                break;
            case "Twos":
                value = Collections.frequency(numbers, 2) * 2;
                scoreItem.setScore(value);
                break;
            case "Threes":
                value = Collections.frequency(numbers, 3) * 3;
                scoreItem.setScore(value);
                break;
            case "Fours":
                value = Collections.frequency(numbers, 4) * 4;
                scoreItem.setScore(value);
                break;
            case "Fives":
                value = Collections.frequency(numbers, 5) * 5;
                scoreItem.setScore(value);
                break;
            case "Sixes":
                value = Collections.frequency(numbers, 6) * 6;
                scoreItem.setScore(value);
                break;
            default:
                throw new IllegalArgumentException("Input not found");
        }

        TextView textView = (TextView) activity.findViewById(R.id.totalTextValue);
        textView.setText(String.valueOf(Integer.valueOf(textView.getText().toString()) + value));
        adapter.notifyDataSetChanged();
        resetNumbers();

        int shouldEnd = 0;
        for (ScoreItem score : scoreItems) {
            if (score.getScore() == -1) {
                shouldEnd++;
            }
        }
        if (shouldEnd == 0) {
            Button button = (Button) activity.findViewById(R.id.btnThrow);
            button.setText("Restart");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restart();
                }
            });
        }
    }

    private void resetNumbers() {
        toggleEnableToggleButton(false);
        activity.findViewById(R.id.btnThrow).setEnabled(true);
        firstThrow = true;
        throwAmount = 0;

        ListIterator<ToggleButton> it = toggleButtons.listIterator();
        while (it.hasNext()) {
            ToggleButton toggleButton = it.next();
            toggleButton.setText("");
        }
    }

    private List<ScoreItem> getScoreKeys() {
        ArrayList<ScoreItem> scores = new ArrayList();
        scores.add(new ScoreItem("Ones"));
        scores.add(new ScoreItem("Twos"));
        scores.add(new ScoreItem("Threes"));
        scores.add(new ScoreItem("Fours"));
        scores.add(new ScoreItem("Fives"));
        scores.add(new ScoreItem("Sixes"));

        return scores;
    }

    public void toggleEnableToggleButton(boolean value) {
        ToggleButton tButton = (ToggleButton) activity.findViewById(R.id.number1);
        tButton.setEnabled(value);

        tButton = (ToggleButton) activity.findViewById(R.id.number2);
        tButton.setEnabled(value);

        tButton = (ToggleButton) activity.findViewById(R.id.number3);
        tButton.setEnabled(value);

        tButton = (ToggleButton) activity.findViewById(R.id.number4);
        tButton.setEnabled(value);

        tButton = (ToggleButton) activity.findViewById(R.id.number5);
        tButton.setEnabled(value);
    }

    public void setListeners() {

        ToggleButton tButton = (ToggleButton) activity.findViewById(R.id.number1);
        tButton.setText("");
        setToggleButtonListeners(tButton);
        toggleButtons.add(tButton);

        tButton = (ToggleButton) activity.findViewById(R.id.number2);
        tButton.setText("");
        setToggleButtonListeners(tButton);
        toggleButtons.add(tButton);

        tButton = (ToggleButton) activity.findViewById(R.id.number3);
        tButton.setText("");
        setToggleButtonListeners(tButton);
        toggleButtons.add(tButton);

        tButton = (ToggleButton) activity.findViewById(R.id.number4);
        tButton.setText("");
        setToggleButtonListeners(tButton);
        toggleButtons.add(tButton);

        tButton = (ToggleButton) activity.findViewById(R.id.number5);
        tButton.setText("");
        setToggleButtonListeners(tButton);
        toggleButtons.add(tButton);

        final Button button = (Button) activity.findViewById(R.id.btnThrow);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstThrow) {
                    generateNumbers();
                    firstThrow = false;
                } else {
                    generateNewNumbers();
                    throwAmount++;
                }

                if (throwAmount >= 2) {
                    button.setEnabled(false);
                }
            }
        });
    }

    public void generateNewNumbers() {
        Random r = new Random();

        ListIterator<ToggleButton> it = toggleButtons.listIterator();
        while (it.hasNext()) {
            ToggleButton toggleButton = it.next();
            if (toggleButton.isChecked()) {
                toggleButton.setText(String.valueOf(r.nextInt(6) + 1));
                toggleButton.setTextOn(toggleButton.getText());
                toggleButton.setTextOff(toggleButton.getText());

                toggleButton.setChecked(false);
            }

        }
    }

    public void setToggleButtonListeners(final ToggleButton tButton) {
        tButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Do something
                } else {
                    //Nothing
                }
            }
        });
    }

    public void generateNumbers() {

        toggleEnableToggleButton(true);


        Random r = new Random();
        ToggleButton tButton = (ToggleButton) activity.findViewById(R.id.number1);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number1);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number2);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number3);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number4);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number5);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

    }


    public void restart() {
        firstThrow = true;
        throwAmount = 0;

        final Button button = (Button) activity.findViewById(R.id.btnThrow);
        button.setText("Throw");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstThrow) {
                    generateNumbers();
                    firstThrow = false;
                } else {
                    generateNewNumbers();
                    throwAmount++;
                }

                if (throwAmount >= 2) {
                    button.setEnabled(false);
                }
            }
        });

        TextView textView = (TextView) activity.findViewById(R.id.totalTextValue);
        textView.setText("0");

        for (ScoreItem scoreItem : scoreItems) {
            scoreItem.setScore(-1);
        }

        adapter.notifyDataSetChanged();
    }
}
