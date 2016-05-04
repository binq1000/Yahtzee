package me.tim.org.yahtzee;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Nekkyou on 29-3-2016.
 */
public class NumberController {

    private Activity activity;
    private ArrayList<ToggleButton> toggleButtons;

    private boolean firstThrow = true;
    private int throwAmount;

    private ArrayList<TextView> textViews;

    public NumberController(Activity activity) {

        TextView textView = (TextView) activity.findViewById(R.id.totalTextValue);
        textView.setText("0");

        this.activity = activity;

        toggleButtons = new ArrayList<>();

        setListeners();
        toggleEnableToggleButton(false);
        throwAmount = 0;

        textViews = new ArrayList<>();
        fillTextViewList();
    }

    private void fillTextViewList() {
        textViews.add((TextView) activity.findViewById(R.id.textValueOne));
        textViews.add((TextView) activity.findViewById(R.id.textValueTwo));
        textViews.add((TextView) activity.findViewById(R.id.textValueThree));
        textViews.add((TextView) activity.findViewById(R.id.textValueFour));
        textViews.add((TextView) activity.findViewById(R.id.textValueFive));
        textViews.add((TextView) activity.findViewById(R.id.textValueSix));

        textViews.add((TextView) activity.findViewById(R.id.textValueThreeOfAKind));
        textViews.add((TextView) activity.findViewById(R.id.textValueFourOfAKind));
        textViews.add((TextView) activity.findViewById(R.id.textValueFullHouse));
        textViews.add((TextView) activity.findViewById(R.id.textValueSmallStraight));
        textViews.add((TextView) activity.findViewById(R.id.textValueLongStraigth));
        textViews.add((TextView) activity.findViewById(R.id.textValueYahtzee));
        textViews.add((TextView) activity.findViewById(R.id.textValueChance));
    }

    private void processScore(String item) {

        if (firstThrow) {
            return;
        }

        List<Integer> numbers = new ArrayList<>();
        int totalSum = 0;

        ListIterator<ToggleButton> it = toggleButtons.listIterator();
        while (it.hasNext()) {
            ToggleButton toggleButton = it.next();
            numbers.add(Integer.valueOf(toggleButton.getText().toString()));
            totalSum += Integer.valueOf(toggleButton.getText().toString());
        }

        //region Switch
        switch (item) {
            case "Ones":
                if (!setSingleScore((TextView) activity.findViewById(R.id.textValueOne), Collections.frequency(numbers, 1))) {
                    return;
                }
                break;
            case "Twos":
                if (!setSingleScore((TextView) activity.findViewById(R.id.textValueTwo), Collections.frequency(numbers, 2) * 2)) {
                    return;
                }
                break;
            case "Threes":
                if (!setSingleScore((TextView) activity.findViewById(R.id.textValueThree), Collections.frequency(numbers, 3) * 3)) {
                    return;
                }
                break;
            case "Fours":
                if (!setSingleScore((TextView) activity.findViewById(R.id.textValueFour), Collections.frequency(numbers, 4) * 4)) {
                    return;
                }
                break;
            case "Fives":
                if (!setSingleScore((TextView) activity.findViewById(R.id.textValueFive), Collections.frequency(numbers, 5) * 5)) {
                    return;
                }
                break;
            case "Sixes":
                if (!setSingleScore((TextView) activity.findViewById(R.id.textValueSix), Collections.frequency(numbers, 6) * 6)) {
                    return;
                }
                break;
            case "ThreeOfAKind":
                //TODO
                if (checkAllMultipleOccurrences(numbers, 3)) {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueThreeOfAKind), totalSum)) {
                        return;
                    }
                } else {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueThreeOfAKind), 0)) {
                        return;
                    }
                }
                break;
            case "FourOfAKind":
                //TODO
                if (checkAllMultipleOccurrences(numbers, 4)) {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueFourOfAKind), totalSum)) {
                        return;
                    }
                } else {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueFourOfAKind), 0)) {
                        return;
                    }
                }
                break;
            case "SmlStraigth":
                if (checkStreet(numbers, 4)) {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueSmallStraight), 30)) {
                        return;
                    }
                } else {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueSmallStraight), 0)) {
                        return;
                    }
                }
                break;
            case "LngStraigth":
                if (checkStreet(numbers, 5)) {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueLongStraigth), 40)) {
                        return;
                    }
                } else {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueLongStraigth), 0)) {
                        return;
                    }
                }
                break;
            case "FullHouse":
                //TODO
                if (checkFullHouse(numbers)) {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueFullHouse), 25)) {
                        return;
                    }
                } else {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueFullHouse), 0)) {
                        return;
                    }
                }
                break;
            case "Yahtzee":
                if (checkYahtzee(numbers)) {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueYahtzee), 50)) {
                        return;
                    }
                } else {
                    if (!setSingleScore((TextView) activity.findViewById(R.id.textValueYahtzee), 0)) {
                        return;
                    }
                }
                break;
            case "Chance":
                if (!setSingleScore((TextView)activity.findViewById(R.id.textValueChance), totalSum)) {
                    return;
                }
                break;
            default:
                throw new IllegalArgumentException("Input not found");
        }
        //endregion

        setTotalValue();
        resetNumbers();

        checkEnd();
    }

    private boolean checkFullHouse(List<Integer> numbers) {
        TreeSet<Integer> distinctNumbers = new TreeSet<>(numbers);
        if (distinctNumbers.size() != 2) {
            return false;
        } else {
            for (int i : distinctNumbers) {
                if (Collections.frequency(numbers, i) < 2) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkAllMultipleOccurrences(List<Integer> numbers, int amount) {
        for (int i=1; i < 7; i++) {
            if (Collections.frequency(numbers, i) >= amount) {
                return true;
            }
        }
        return false;
    }

    private boolean checkStreet(List<Integer> numbers, int streetLength) {
        Collections.sort(numbers);
        int counter = 1;
        int lastNumber = 0;
        for (int i : numbers) {
            if (lastNumber == 0) {
                lastNumber = i;
            } else {
                if (i - 1 == lastNumber) {
                    counter++;
                    if (counter >= streetLength) {
                        return true;
                    }
                } else if (i != lastNumber){
                    counter = 1;
                }

                lastNumber = i;
            }
        }
        return false;
    }

    private boolean checkYahtzee(List<Integer> numbers) {
        if (Collections.frequency(numbers, numbers.get(1)) != 5) {
            return false;
        } else {
            return true;
        }
    }

    private void checkEnd() {
        boolean shouldEnd = true;
        for (TextView textView : textViews) {
            if (textView.getText().toString().matches("")) {
                shouldEnd = false;
            }
        }
        if (shouldEnd) {
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

    private boolean setSingleScore(TextView textView, int value) {
        if (!textView.getText().toString().matches("")) {
            return false;
        } else {
            textView.setText(String.valueOf(value));
            return true;
        }
    }

    private void setTotalValue() {
        TextView textView = (TextView) activity.findViewById(R.id.totalTextValue);
        int value = getTotalValue();

        if (value >= 63) {
            textView.setText(String.valueOf(value) + " + 35\nBonus Acquired");
        } else {
            textView.setText(String.valueOf(value));
        }
    }

    private int getTotalValue() {
        int value = 0;

        TextView textView = (TextView) activity.findViewById(R.id.textValueOne);
        if (!textView.getText().toString().matches("")) {
            value += Integer.valueOf(textView.getText().toString());
        }

        textView = (TextView) activity.findViewById(R.id.textValueTwo);
        if (!textView.getText().toString().matches("")) {
            value += Integer.valueOf(textView.getText().toString());
        }

        textView = (TextView) activity.findViewById(R.id.textValueThree);
        if (!textView.getText().toString().matches("")) {
            value += Integer.valueOf(textView.getText().toString());
        }

        textView = (TextView) activity.findViewById(R.id.textValueFour);
        if (!textView.getText().toString().matches("")) {
            value += Integer.valueOf(textView.getText().toString());
        }

        textView = (TextView) activity.findViewById(R.id.textValueFive);
        if (!textView.getText().toString().matches("")) {
            value += Integer.valueOf(textView.getText().toString());
        }
        textView = (TextView) activity.findViewById(R.id.textValueSix);
        if (!textView.getText().toString().matches("")) {
            value += Integer.valueOf(textView.getText().toString());
        }

        return value;
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
            toggleButton.setTextOn("");
            toggleButton.setTextOff("");
            toggleButton.setChecked(false);
        }
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

        setCardListeners();
    }

    public void setCardListeners() {
        setSingleCardListener((CardView) activity.findViewById(R.id.cardOne), "Ones");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardTwo), "Twos");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardThree), "Threes");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardFour), "Fours");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardFive), "Fives");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardSix), "Sixes");

        setSingleCardListener((CardView) activity.findViewById(R.id.cardThreeOfAKind), "ThreeOfAKind");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardFourOfAKind),  "FourOfAKind");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardSmallStraight), "SmlStraigth");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardLongStraigth), "LngStraigth");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardFullHouse), "FullHouse");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardYahtzee), "Yahtzee");
        setSingleCardListener((CardView) activity.findViewById(R.id.cardChance), "Chance");
    }

    private void setSingleCardListener(CardView cardView, final String value) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processScore(value);
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
        setImageButtonImage((ImageView) activity.findViewById(R.id.number1Img), Integer.valueOf(tButton.getText().toString()));

        tButton = (ToggleButton) activity.findViewById(R.id.number2);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());
        setImageButtonImage((ImageView) activity.findViewById(R.id.number2Img), Integer.valueOf(tButton.getText().toString()));

        tButton = (ToggleButton) activity.findViewById(R.id.number3);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());
        setImageButtonImage((ImageView) activity.findViewById(R.id.number3Img), Integer.valueOf(tButton.getText().toString()));

        tButton = (ToggleButton) activity.findViewById(R.id.number4);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());
        setImageButtonImage((ImageView) activity.findViewById(R.id.number4Img), Integer.valueOf(tButton.getText().toString()));

        tButton = (ToggleButton) activity.findViewById(R.id.number5);
        tButton.setText(String.valueOf(r.nextInt(6) + 1));
        tButton.setTextOn(tButton.getText());
        tButton.setTextOff(tButton.getText());
        setImageButtonImage((ImageView) activity.findViewById(R.id.number5Img), Integer.valueOf(tButton.getText().toString()));

    }

    private void setImageButtonImage(ImageView imageButton, int value) {
        if (value < 1 || value > 6) {
            return;
        }

        switch (value) {
            case 1:
                imageButton.setImageResource(R.drawable.ic_looks_one_black_18dp);
                break;
            case 2:
                imageButton.setImageResource(R.drawable.ic_looks_two_black_18dp);
                break;
            case 3:
                imageButton.setImageResource(R.drawable.ic_looks_3_black_18dp);
                break;
            case 4:
                imageButton.setImageResource(R.drawable.ic_looks_4_black_18dp);
                break;
            case 5:
                imageButton.setImageResource(R.drawable.ic_looks_5_black_18dp);
                break;
            case 6:
                imageButton.setImageResource(R.drawable.ic_looks_6_black_18dp);
                break;
        }
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

        emptyScoreStrings();
    }

    private void emptyScoreStrings() {
        TextView textView = (TextView) activity.findViewById(R.id.totalTextValue);
        textView.setText("0");

        //region setting Score labels to an empty string
        textView = (TextView) activity.findViewById(R.id.textValueOne);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueTwo);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueThree);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueFour);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueFive);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueSix);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueThreeOfAKind);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueFourOfAKind);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueFullHouse);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueSmallStraight);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueLongStraigth);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueYahtzee);
        textView.setText("");
        textView = (TextView) activity.findViewById(R.id.textValueChance);
        textView.setText("");
        //endregion
    }
}
