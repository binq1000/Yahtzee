package me.tim.org.yahtzee;

/**
 * Created by Nekkyou on 16-4-2016.
 */
public class ScoreItem {
    private int score;
    private String name;

    public ScoreItem(String name) {
        this.name = name;
        score = -1;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
