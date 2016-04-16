package me.tim.org.yahtzee;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by Nekkyou on 29-3-2016.
 */
public class NumberController {

    private Activity activity;
    private ArrayList<ToggleButton> toggleButtons;

    public NumberController(Activity activity) {
        this.activity = activity;

        toggleButtons = new ArrayList<>();

        setListeners();
        toggleEnableToggleButton(false);
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

        Button button = (Button) activity.findViewById(R.id.btnThrow);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewNumbers();
            }
        });

    }

    public void generateNewNumbers() {
        Random r = new Random();

        ListIterator<ToggleButton> it = toggleButtons.listIterator();
        while (it.hasNext()) {
            ToggleButton toggleButton = it.next();
            if (toggleButton.isChecked()) {
                toggleButton.setText(String.valueOf(r.nextInt(7)));
                toggleButton.setTextOn(toggleButton.getText());
                toggleButton.setTextOff(toggleButton.getText());

                toggleButton.setChecked(false);
            }

        }

        toggleButtons.clear();
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
        tButton.setText(String.valueOf(r.nextInt(7)));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number1);
        tButton.setText(String.valueOf(r.nextInt(7)));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number2);
        tButton.setText(String.valueOf(r.nextInt(7)));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number3);
        tButton.setText(String.valueOf(r.nextInt(7)));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number4);
        tButton.setText(String.valueOf(r.nextInt(7)));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

        tButton = (ToggleButton) activity.findViewById(R.id.number5);
        tButton.setText(String.valueOf(r.nextInt(7)));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());

    }
}
