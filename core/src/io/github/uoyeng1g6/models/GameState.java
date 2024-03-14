package io.github.uoyeng1g6.models;

import java.util.ArrayList;

public class GameState {
    public static class Day {
        private int numTimesEaten = 0;
        private int numTimesStudied = 0;
        private int numRecreationalActivities = 0;
    }

    private static final int MAX_ENERGY = 100;

    private final ArrayList<Day> days = new ArrayList<>(7);

    private int daysRemaining = 7;
    private int energy = MAX_ENERGY;
    private Day currentDay = new Day();

    public void advanceDay() {
        daysRemaining--;
        energy = MAX_ENERGY;

        days.add(currentDay);
        currentDay = new Day();
    }
}
