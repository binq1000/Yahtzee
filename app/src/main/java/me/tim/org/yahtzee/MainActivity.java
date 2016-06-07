package me.tim.org.yahtzee;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private NumberController controller;

    private SensorManager mSensorManager;
    private ShakeDetector mSensorListener;

    private CardView cardOne;
    private CardView cardTwo;
    private CardView cardThree;
    private CardView cardFour;
    private CardView cardFive;
    private CardView cardSix;
    private CardView cardThreeOfAKind;
    private CardView cardFourOfAKind;
    private CardView cardSmallStraight;
    private CardView cardLongStraight;
    private CardView cardFullHouse;
    private CardView cardYahtzee;
    private CardView cardChance;


    private TextView textValueOne;
    private TextView textValueTwo;
    private TextView textValueThree;
    private TextView textValueFour;
    private TextView textValueFive;
    private TextView textValueSix;
    private TextView textValueThreeOfAKind;
    private TextView textValueFourOfAKind;
    private TextView textValueFullHouse;
    private TextView textValueSmallStraight;
    private TextView textValueLongStraight;
    private TextView textValueYahtzee;
    private TextView textValueChance;

    private ImageView number1;
    private ImageView number2;
    private ImageView number3;
    private ImageView number4;
    private ImageView number5;

    private Button btnThrow;
    private Collection<TextView> basicTextViews;
    private Collection<TextView> advanceTextViews;
    private ArrayList<ImageView> imageViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controller = new NumberController(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeDetector();

        mSensorListener.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                Toast.makeText(MainActivity.this, "Shaked", Toast.LENGTH_SHORT).show();
                controller.shaked();
            }
        });

        instantiateGUI();
    }

    private void instantiateGUI() {
        basicTextViews = new ArrayList<>();
        advanceTextViews = new ArrayList<>();
        imageViews = new ArrayList<>();

        instantiateGUITextViews();
        instantiateGUIImageViews();
        instantiateGUICardViews();
        setListeners();
        fillTextViewList();
        setImageViewOnClick();

        btnThrow = (Button) findViewById(R.id.btnThrow);
    }

    private void instantiateGUITextViews() {
        textValueOne = (TextView) findViewById(R.id.textValueOne);
        textValueTwo = (TextView) findViewById(R.id.textValueTwo);
        textValueThree = (TextView) findViewById(R.id.textValueThree);
        textValueFour = (TextView) findViewById(R.id.textValueFour);
        textValueFive = (TextView) findViewById(R.id.textValueFive);
        textValueSix = (TextView) findViewById(R.id.textValueSix);

        textValueThreeOfAKind = (TextView) findViewById(R.id.textValueThreeOfAKind);
        textValueFourOfAKind = (TextView) findViewById(R.id.textValueFourOfAKind);
        textValueFullHouse = (TextView) findViewById(R.id.textValueFullHouse);
        textValueSmallStraight = (TextView) findViewById(R.id.textValueSmallStraight);
        textValueLongStraight = (TextView) findViewById(R.id.textValueLongStraigth);
        textValueYahtzee = (TextView) findViewById(R.id.textValueYahtzee);
        textValueChance = (TextView) findViewById(R.id.textValueChance);
    }

    private void instantiateGUIImageViews() {
        number1 = (ImageView) findViewById(R.id.number1Img);
        number2 = (ImageView) findViewById(R.id.number2Img);
        number3 = (ImageView) findViewById(R.id.number3Img);
        number4 = (ImageView) findViewById(R.id.number4Img);
        number5 = (ImageView) findViewById(R.id.number5Img);
    }

    private void instantiateGUICardViews() {
        cardOne = (CardView) findViewById(R.id.cardOne);
        cardTwo = (CardView) findViewById(R.id.cardTwo);
        cardThree = (CardView) findViewById(R.id.cardThree);
        cardFour = (CardView) findViewById(R.id.cardFour);
        cardFive = (CardView) findViewById(R.id.cardFive);
        cardSix = (CardView) findViewById(R.id.cardSix);

        cardThreeOfAKind = (CardView) findViewById(R.id.cardThreeOfAKind);
        cardFourOfAKind = (CardView) findViewById(R.id.cardFourOfAKind);
        cardSmallStraight = (CardView) findViewById(R.id.cardSmallStraight);
        cardLongStraight = (CardView) findViewById(R.id.cardLongStraigth);
        cardFullHouse = (CardView) findViewById(R.id.cardFullHouse);
        cardYahtzee = (CardView) findViewById(R.id.cardYahtzee);
        cardChance = (CardView) findViewById(R.id.cardChance);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.show_results:
                goToScoreActivity();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void goToScoreActivity() {
        Intent intent = new Intent(this, Score_Activity.class);

        startActivity(intent);
    }

    private void fillTextViewList() {
        basicTextViews.add(textValueOne);
        basicTextViews.add(textValueTwo);
        basicTextViews.add(textValueThree);
        basicTextViews.add(textValueFour);
        basicTextViews.add(textValueFive);
        basicTextViews.add(textValueSix);

        advanceTextViews.add(textValueThreeOfAKind);
        advanceTextViews.add(textValueFourOfAKind);
        advanceTextViews.add(textValueFullHouse);
        advanceTextViews.add(textValueSmallStraight);
        advanceTextViews.add(textValueLongStraight);
        advanceTextViews.add(textValueYahtzee);
        advanceTextViews.add(textValueChance);
    }
    public int getBasicScore() {
        int value = 0;
        value += getSingleScoreFromTextView(textValueOne);
        value += getSingleScoreFromTextView(textValueTwo);
        value += getSingleScoreFromTextView(textValueThree);
        value += getSingleScoreFromTextView(textValueFour);
        value += getSingleScoreFromTextView(textValueFive);
        value += getSingleScoreFromTextView(textValueSix);

        return value;
    }

    public int getAdvancedScore() {
        int value = 0;
        value += getSingleScoreFromTextView(textValueThreeOfAKind);
        value += getSingleScoreFromTextView(textValueFourOfAKind);
        value += getSingleScoreFromTextView(textValueSmallStraight);
        value += getSingleScoreFromTextView(textValueLongStraight);
        value += getSingleScoreFromTextView(textValueFullHouse);
        value += getSingleScoreFromTextView(textValueYahtzee);
        value += getSingleScoreFromTextView(textValueChance);
        return value;
    }

    public boolean shouldEnd() {
        boolean shouldEnd = true;
        ArrayList<TextView> textviews = new ArrayList<>();
        textviews.addAll(basicTextViews);
        textviews.addAll(advanceTextViews);

        for (TextView textView : textviews) {
            if (textviewEmpty(textView)) {
                shouldEnd = false;
            }
        }
        return shouldEnd;
    }

    public void setButtonToRestart() {
        Button button = (Button) findViewById(R.id.btnThrow);
        button.setText("Restart");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.restart();
            }
        });
    }

    private boolean textviewEmpty(TextView textview) {
        if (textview.getText().toString().matches("")) {
            return true;
        } else {
            return false;
        }
    }

    private int getSingleScoreFromTextView(TextView textView) {
        if (textView.getText().toString().matches("")) {
            return 0;
        } else {
            return Integer.valueOf(textView.getText().toString());
        }
    }


    private void setListeners() {
        ImageView imageView = (ImageView) findViewById(R.id.number1Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        imageView = (ImageView) findViewById(R.id.number2Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        imageView = (ImageView) findViewById(R.id.number3Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        imageView = (ImageView) findViewById(R.id.number4Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        imageView = (ImageView) findViewById(R.id.number5Img);
        imageView.setTag(R.id.imageViewIsChecked, false);
        imageViews.add(imageView);
        setImageViewClicked(imageView);

        final Button button = (Button) findViewById(R.id.btnThrow);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller.isFirstThrow()) {
                    controller.generateNumbers();
                } else {
                    if (controller.generateNewNumbers()) {
                        controller.raiseThrowAmount();
                    }
                }

                if (controller.getThrowAmount() >= 2) {
                    button.setEnabled(false);
                }

                imageAnimation();
            }
        });

        setCardListeners();
    }

    public void setCardListeners() {
        setSingleCardListener((CardView) findViewById(R.id.cardOne), "Ones");
        setSingleCardListener((CardView) findViewById(R.id.cardTwo), "Twos");
        setSingleCardListener((CardView) findViewById(R.id.cardThree), "Threes");
        setSingleCardListener((CardView) findViewById(R.id.cardFour), "Fours");
        setSingleCardListener((CardView) findViewById(R.id.cardFive), "Fives");
        setSingleCardListener((CardView) findViewById(R.id.cardSix), "Sixes");

        setSingleCardListener((CardView) findViewById(R.id.cardThreeOfAKind), "ThreeOfAKind");
        setSingleCardListener((CardView) findViewById(R.id.cardFourOfAKind),  "FourOfAKind");
        setSingleCardListener((CardView) findViewById(R.id.cardSmallStraight), "SmlStraigth");
        setSingleCardListener((CardView) findViewById(R.id.cardLongStraigth), "LngStraigth");
        setSingleCardListener((CardView) findViewById(R.id.cardFullHouse), "FullHouse");
        setSingleCardListener((CardView) findViewById(R.id.cardYahtzee), "Yahtzee");
        setSingleCardListener((CardView) findViewById(R.id.cardChance), "Chance");
    }

    private void setSingleCardListener(CardView cardView, final String value) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.processScore(value);
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

    public void setImageViewImage(ImageView imageView) {

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

    private void setImageViewOnClick() {
        ImageView imageView = (ImageView) findViewById(R.id.number1Img);
        setSingleImageViewClick(imageView);
        imageView = (ImageView) findViewById(R.id.number2Img);
        setSingleImageViewClick(imageView);
        imageView = (ImageView) findViewById(R.id.number3Img);
        setSingleImageViewClick(imageView);
        imageView = (ImageView) findViewById(R.id.number4Img);
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


    public List<Integer> getNumbers() {
        List<Integer> numbers = new ArrayList<>();
        ListIterator<ImageView> it = imageViews.listIterator();
        while (it.hasNext()) {
            ImageView imageView = it.next();
            numbers.add((Integer) imageView.getTag(R.id.imageViewNumbericValue));
        }

        return numbers;
    }


    public void setBtnThrow(boolean value) {
        btnThrow.setEnabled(value);
    }

    public void setImageViewOnReset() {
        ListIterator<ImageView> it = imageViews.listIterator();
        while (it.hasNext()) {
            ImageView imageView = it.next();
            imageView.setImageResource(R.drawable.ic_loop_black_18dp);
        }
    }


    //region Animation
    public void imageAnimation() {
        for (ImageView imageView : imageViews) {
            if (controller.isFirstThrow()) {
                singleImageAnimation(imageView);
            }
            else if ((boolean)imageView.getTag(R.id.imageViewIsChecked)) {
                singleImageAnimation(imageView);
                imageView.setTag(R.id.imageViewIsChecked, false);
            }
            System.out.println("IsChecked: " + (boolean)imageView.getTag(R.id.imageViewIsChecked));
            System.out.println("firstThrow: " + controller.isFirstThrow() + "\n");
        }
        if (controller.isFirstThrow()) {
            controller.setFirstThrow(false);
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
                return getResources().getDrawable(R.drawable.ic_looks_one_black_18dp);
            case 2:
                return getResources().getDrawable(R.drawable.ic_looks_two_black_18dp);
            case 3:
                return getResources().getDrawable(R.drawable.ic_looks_3_black_18dp);
            case 4:
                return getResources().getDrawable(R.drawable.ic_looks_4_black_18dp);
            case 5:
                return getResources().getDrawable(R.drawable.ic_looks_5_black_18dp);
            case 6:
                return getResources().getDrawable(R.drawable.ic_looks_6_black_18dp);
        }

        return null;
    }

    private Drawable getLastDiceDrawable(ImageView imageView) {
        int value = (int) imageView.getTag(R.id.imageViewNumbericValue);
        return getDrawableDiceByNumber(value);
    }
    //endregion

}
