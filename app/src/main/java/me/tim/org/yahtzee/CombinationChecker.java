package me.tim.org.yahtzee;

/**
 * Created by Nekkyou on 6-5-2016.
 */
public class CombinationChecker {
    private static CombinationChecker combinationChecker = null;

    private CombinationChecker() {

    }

    public static CombinationChecker getInstance() {
        if (combinationChecker == null) {
            combinationChecker = new CombinationChecker();
        }
        return combinationChecker;
    }

}
