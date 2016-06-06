package me.tim.org.yahtzee;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nekkyou on 8-5-2016.
 */
public class DataController {
    private Activity activity;
    private String fileName = "TimYahtzeeScore";
    private File file;

    public DataController(Activity activity) {
        this.activity = activity;
        file = new File(activity.getFilesDir(), fileName);
    }


    public void saveScore(int score) {
        ArrayList<Integer> scores;

        if (getScores() != null) {
            scores = (ArrayList<Integer>) getScores();
        } else {
            scores = new ArrayList<>();
        }

        scores.add(score);

        JSONArray jsonArray = new JSONArray(scores);

        FileOutputStream fos;
        ObjectOutputStream out;

        try {
            fos = activity.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(jsonArray.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getScores() {
        List<Integer> scores = new ArrayList<>();
        JSONArray jsonArray = null;
        FileInputStream fis;
        ObjectInputStream ois;


        String arrayInText = "";
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            arrayInText = (String) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            jsonArray = new JSONArray(arrayInText);

            for (int i=0; i < jsonArray.length(); i++) {
                scores.add(Integer.valueOf(jsonArray.getString(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return scores;
    }



}
