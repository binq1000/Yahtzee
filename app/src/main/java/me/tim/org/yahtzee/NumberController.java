package me.tim.org.yahtzee;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by Nekkyou on 29-3-2016.
 */
public class NumberController {
    private MainActivity activity;
    private boolean firstThrow = true;

    public int getThrowAmount() {
        return throwAmount;
    }

    public void setThrowAmount(int throwAmount) {
        this.throwAmount = throwAmount;
    }

    public void raiseThrowAmount() {
        throwAmount++;
    }

    public boolean isFirstThrow() {
        return firstThrow;
    }

    public void setFirstThrow(boolean firstThrow) {
        this.firstThrow = firstThrow;
    }

    private int throwAmount;
    private DataController dataController;

    public NumberController(final MainActivity activity) {
        this.activity = activity;
        throwAmount = 0;
        dataController = new DataController(activity);
    }

    public void shaked() {
        Button button = (Button) activity.findViewById(R.id.btnThrow);
        if (firstThrow) {
            generateNumbers();
        } else {
            if (generateNewNumbers()) {
                throwAmount++;
            }
        }
        if (throwAmount >= 2) {
            button.setEnabled(false);
        }

        imageAnimation();
    }

    private int getTotalSum(List<Integer> numbers) {
        int value = 0;
        for (Integer integer : numbers) {
            value += integer;
        }
        return value;
    }


    //region score
    public void processScore(String item) {

        if (firstThrow) {
            return;
        }

        List<Integer> numbers = activity.getNumbers();
        int totalSum = getTotalSum(numbers);

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
                if (CombinationChecker.getInstance().checkAllMultipleOccurrences(numbers, 3)) {
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
                if (CombinationChecker.getInstance().checkAllMultipleOccurrences(numbers, 4)) {
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
                if (CombinationChecker.getInstance().checkStreet(numbers, 4)) {
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
                if (CombinationChecker.getInstance().checkStreet(numbers, 5)) {
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
                if (CombinationChecker.getInstance().checkFullHouse(numbers)) {
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
                if (CombinationChecker.getInstance().checkYahtzee(numbers)) {
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

        if (activity.getBasicScore() >= 63) {
            textView.setText(String.valueOf(value) + " + 35\nBonus Acquired");
        } else {
            textView.setText(String.valueOf(value));
        }
    }


    private int getTotalValue() {
        return activity.getBasicScore() + activity.getAdvancedScore();
    }
    //endregion

    private void checkEnd() {
        boolean shouldEnd = activity.shouldEnd();
        if (shouldEnd) {
            activity.setButtonToRestart();
        }
    }

    private void resetNumbers() {
        activity.setBtnThrow(true);
        firstThrow = true;
        throwAmount = 0;

        activity.setImageViewOnReset();
    }



    //region number Generation
    public boolean generateNewNumbers() {
        Random r = new Random();

        ArrayList<ImageView> checkedDice = getCheckedDice();
        if (checkedDice.isEmpty()) {
            return false;
        }

        ListIterator<ImageView> it = checkedDice.listIterator();

        while (it.hasNext()) {
            ImageView imageView = it.next();
            int value = r.nextInt(6) + 1;
            imageView.setTag(R.id.imageViewNumbericValue, value);
            setImageViewImage(imageView);
        }

        return true;
    }

    public ArrayList<ImageView> getCheckedDice() {
        ArrayList<ImageView> checkedDices = new ArrayList<>();
        ListIterator<ImageView> it = imageViews.listIterator();

        while (it.hasNext()) {
            ImageView imageView = it.next();
            boolean isChecked = (boolean) imageView.getTag(R.id.imageViewIsChecked);
            if (isChecked) {
                checkedDices.add(imageView);
            }
        }

        return checkedDices;
    }

    public void generateNumbers() {
        Random r = new Random();
        int value = 0;

        value = r.nextInt(6) + 1;
        ImageView imageView = (ImageView) activity.findViewById(R.id.number1Img);
        imageView.setTag(R.id.imageViewNumbericValue, value);
        activity.setImageViewImage(imageView);

        value = r.nextInt(6) + 1;
        imageView = (ImageView) activity.findViewById(R.id.number2Img);
        imageView.setTag(R.id.imageViewNumbericValue, value);
        activity.setImageViewImage(imageView);

        value = r.nextInt(6) + 1;
        imageView = (ImageView) activity.findViewById(R.id.number3Img);
        imageView.setTag(R.id.imageViewNumbericValue, value);
        activity.setImageViewImage(imageView);

        value = r.nextInt(6) + 1;
        imageView = (ImageView) activity.findViewById(R.id.number4Img);
        imageView.setTag(R.id.imageViewNumbericValue, value);
        activity.setImageViewImage(imageView);

        value = r.nextInt(6) + 1;
        imageView = (ImageView) activity.findViewById(R.id.number5Img);
        imageView.setTag(R.id.imageViewNumbericValue, value);
        activity.setImageViewImage(imageView);
    }
    //endregion



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
                } else {
                    if (generateNewNumbers()) {
                        throwAmount++;
                    }
                }
                if (throwAmount >= 2) {
                    button.setEnabled(false);
                }

                imageAnimation();
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
