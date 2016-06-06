package me.tim.org.yahtzee;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private ArrayList<ImageView> imageViews;
    private boolean firstThrow = true;
    private int throwAmount;
    private ArrayList<TextView> basicTextViews;
    private ArrayList<TextView> advanceTextViews;
    private DataController dataController;
    //For shaking
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    public NumberController(final Activity activity) {
        this.activity = activity;
        imageViews = new ArrayList<>();
        throwAmount = 0;
        basicTextViews = new ArrayList<>();
        advanceTextViews = new ArrayList<>();
        dataController = new DataController(activity);


        setListeners();
        fillTextViewList();
        setImageViewOnClick();


        Button saveButton = (Button) activity.findViewById(R.id.saveScoreButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataController.saveScore(getTotalValue());
            }
        });

        Button getSaveButton = (Button) activity.findViewById(R.id.getsaveScoreButton);
        getSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> scores = dataController.getScores();
                for (Integer score : scores) {
                    System.out.println(score);
                }
            }
        });
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


    //region score
    private void processScore(String item) {

        if (firstThrow) {
            return;
        }

        List<Integer> numbers = new ArrayList<>();
        int totalSum = 0;

        ListIterator<ImageView> it = imageViews.listIterator();
        while (it.hasNext()) {
            ImageView imageView = it.next();
            numbers.add((Integer) imageView.getTag(R.id.imageViewNumbericValue));
            totalSum += (int) imageView.getTag(R.id.imageViewNumbericValue);
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

        if (getBasicScore() >= 63) {
            textView.setText(String.valueOf(value) + " + 35\nBonus Acquired");
        } else {
            textView.setText(String.valueOf(value));
        }
    }

    private int getBasicScore() {
        int value = 0;
        for (TextView textView : basicTextViews) {
            value += getSingleScoreFromTextView(textView);
        }

        return value;
    }

    private int getAdvancedScore() {
        int value = 0;
        for (TextView textView : advanceTextViews) {
            value += getSingleScoreFromTextView(textView);
        }
        return value;
    }

    private int getTotalValue() {
        return getBasicScore() + getAdvancedScore();
    }

    private int getSingleScoreFromTextView(TextView textView) {
        if (textView.getText().toString().matches("")) {
            return 0;
        } else {
            return Integer.valueOf(textView.getText().toString());
        }
    }
    //endregion

    private void checkEnd() {
        boolean shouldEnd = true;
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.addAll(basicTextViews);
        textViews.addAll(advanceTextViews);

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

    private void resetNumbers() {
        activity.findViewById(R.id.btnThrow).setEnabled(true);
        firstThrow = true;
        throwAmount = 0;

        ListIterator<ImageView> it = imageViews.listIterator();
        while (it.hasNext()) {
            ImageView imageView = it.next();
            imageView.setImageResource(R.drawable.ic_loop_black_18dp);
        }
    }

    //region Animation
    private void imageAnimation() {
        for (ImageView imageView : imageViews) {
            if (firstThrow) {
                singleImageAnimation(imageView);
            }
            else if ((boolean)imageView.getTag(R.id.imageViewIsChecked)) {
                singleImageAnimation(imageView);
                imageView.setTag(R.id.imageViewIsChecked, false);
            }
            System.out.println("IsChecked: " + (boolean)imageView.getTag(R.id.imageViewIsChecked));
            System.out.println("firstThrow: " + firstThrow + "\n");
        }
        if (firstThrow) {
            firstThrow = false;
        }
    }

    private void singleImageAnimation(final ImageView imageView) {
        AnimationDrawable animation = new AnimationDrawable();
        for (Drawable drawable : getRandomDrawables(10)) {
            animation.addFrame(drawable, 100);
        }
        animation.addFrame(getLastDiceDrawable(imageView), 100);

        animation.setOneShot(true);
        imageView.setImageDrawable(animation);
        animation.start();


    }

    private List<Drawable> getRandomDrawables(int amount) {
        Random r = new Random();
        List<Drawable> drawables = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            int value = r.nextInt(6) + 1;
            drawables.add(getDrawableDiceByNumber(value));
        }

        return drawables;
    }

    private Drawable getDrawableDiceByNumber(int number) {
        if (number < 1 || number > 6) {
            return null;
        }

        switch (number) {
            case 1:
                return activity.getResources().getDrawable(R.drawable.ic_looks_one_black_18dp);
            case 2:
                return activity.getResources().getDrawable(R.drawable.ic_looks_two_black_18dp);
            case 3:
                return activity.getResources().getDrawable(R.drawable.ic_looks_3_black_18dp);
            case 4:
                return activity.getResources().getDrawable(R.drawable.ic_looks_4_black_18dp);
            case 5:
                return activity.getResources().getDrawable(R.drawable.ic_looks_5_black_18dp);
            case 6:
                return activity.getResources().getDrawable(R.drawable.ic_looks_6_black_18dp);
        }

        return null;
    }

    private Drawable getLastDiceDrawable(ImageView imageView) {
        int value = (int) imageView.getTag(R.id.imageViewNumbericValue);
        return getDrawableDiceByNumber(value);
    }
    //endregion

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
        setImageViewImage(imageView);

        value = r.nextInt(6) + 1;
        imageView = (ImageView) activity.findViewById(R.id.number2Img);
        imageView.setTag(R.id.imageViewNumbericValue, value);
        setImageViewImage(imageView);

        value = r.nextInt(6) + 1;
        imageView = (ImageView) activity.findViewById(R.id.number3Img);
        imageView.setTag(R.id.imageViewNumbericValue, value);
        setImageViewImage(imageView);

        value = r.nextInt(6) + 1;
        imageView = (ImageView) activity.findViewById(R.id.number4Img);
        imageView.setTag(R.id.imageViewNumbericValue, value);
        setImageViewImage(imageView);

        value = r.nextInt(6) + 1;
        imageView = (ImageView) activity.findViewById(R.id.number5Img);
        imageView.setTag(R.id.imageViewNumbericValue, value);
        setImageViewImage(imageView);
    }
    //endregion

    private void setImageViewImage(ImageView imageView) {

        int value = (int) imageView.getTag(R.id.imageViewNumbericValue);
        boolean isChecked = (boolean) imageView.getTag(R.id.imageViewIsChecked);

        if (value < 1 || value > 6) {
            return;
        }

        switch (value) {
            case 1:
                if (isChecked) {
                    imageView.setImageResource(R.drawable.ic_looks_one_white_18dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_looks_one_black_18dp);
                }
                break;
            case 2:
                if (isChecked) {
                    imageView.setImageResource(R.drawable.ic_looks_two_white_18dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_looks_two_black_18dp);
                }
                break;
            case 3:
                if (isChecked) {
                    imageView.setImageResource(R.drawable.ic_looks_3_white_18dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_looks_3_black_18dp);
                }
                break;
            case 4:
                if (isChecked) {
                    imageView.setImageResource(R.drawable.ic_looks_4_white_18dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_looks_4_black_18dp);
                }
                break;
            case 5:
                if (isChecked) {
                    imageView.setImageResource(R.drawable.ic_looks_5_white_18dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_looks_5_black_18dp);
                }
                break;
            case 6:
                if (isChecked) {
                    imageView.setImageResource(R.drawable.ic_looks_6_white_18dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_looks_6_black_18dp);
                }
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

    //region Listeners

    public void setListeners() {
        ImageView imageView = (ImageView) activity.findViewById(R.id.number1Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        imageView = (ImageView) activity.findViewById(R.id.number2Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        imageView = (ImageView) activity.findViewById(R.id.number3Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        imageView = (ImageView) activity.findViewById(R.id.number4Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        imageView = (ImageView) activity.findViewById(R.id.number5Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        final Button button = (Button) activity.findViewById(R.id.btnThrow);
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

        setCardListeners();
    }

    private void setImageViewOnClick() {
        ImageView imageView = (ImageView) activity.findViewById(R.id.number1Img);
        setSingleImageViewClick(imageView);
        imageView = (ImageView) activity.findViewById(R.id.number2Img);
        setSingleImageViewClick(imageView);
        imageView = (ImageView) activity.findViewById(R.id.number3Img);
        setSingleImageViewClick(imageView);
        imageView = (ImageView) activity.findViewById(R.id.number4Img);
        setSingleImageViewClick(imageView);
    }

    private void setSingleImageViewClick(ImageView imageView) {
        imageView.setTag("Test");
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData dragData = ClipData.newPlainText("Test", "second Test");
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

                v.startDrag(dragData, myShadow, null, 0);
                return false;
            }
        });
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

    private void setImageViewClicked(final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = (boolean) v.getTag(R.id.imageViewIsChecked);
                if (isChecked) {
                    v.setTag(R.id.imageViewIsChecked, false);

                } else {
                    v.setTag(R.id.imageViewIsChecked, true);
                }
                setImageViewImage(imageView);
            }
        });
    }

    private void fillTextViewList() {
        basicTextViews.add((TextView) activity.findViewById(R.id.textValueOne));
        basicTextViews.add((TextView) activity.findViewById(R.id.textValueTwo));
        basicTextViews.add((TextView) activity.findViewById(R.id.textValueThree));
        basicTextViews.add((TextView) activity.findViewById(R.id.textValueFour));
        basicTextViews.add((TextView) activity.findViewById(R.id.textValueFive));
        basicTextViews.add((TextView) activity.findViewById(R.id.textValueSix));

        advanceTextViews.add((TextView) activity.findViewById(R.id.textValueThreeOfAKind));
        advanceTextViews.add((TextView) activity.findViewById(R.id.textValueFourOfAKind));
        advanceTextViews.add((TextView) activity.findViewById(R.id.textValueFullHouse));
        advanceTextViews.add((TextView) activity.findViewById(R.id.textValueSmallStraight));
        advanceTextViews.add((TextView) activity.findViewById(R.id.textValueLongStraigth));
        advanceTextViews.add((TextView) activity.findViewById(R.id.textValueYahtzee));
        advanceTextViews.add((TextView) activity.findViewById(R.id.textValueChance));
    }
    //endregion
}
