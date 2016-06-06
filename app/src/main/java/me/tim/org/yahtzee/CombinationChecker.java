package me.tim.org.yahtzee;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

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

    public boolean checkFullHouse(List<Integer> numbers) {
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

    public boolean checkAllMultipleOccurrences(List<Integer> numbers, int amount) {
        for (int i=1; i < 7; i++) {
            if (Collections.frequency(numbers, i) >= amount) {
                return true;
            }
        }
        return false;
    }

    public boolean checkStreet(List<Integer> numbers, int streetLength) {
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

    public boolean checkYahtzee(List<Integer> numbers) {
        if (Collections.frequency(numbers, numbers.get(1)) != 5) {
            return false;
        } else {
            return true;
        }
    }

}
