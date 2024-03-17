package io.github.uoyeng1g6.models;

import io.github.uoyeng1g6.constants.ActivityType;
import java.util.ArrayList;
import java.util.HashMap;

public class GameState {
    public static class Day {
        private final HashMap<ActivityType, Integer> activityStats = new HashMap<>();

        public int statFor(ActivityType type) {
            return activityStats.getOrDefault(type, 0);
        }

        public HashMap<ActivityType, Integer> getActivityStats() {
            return activityStats;
        }
    }

    private static final int MAX_ENERGY = 100;

    private final ArrayList<Day> days = new ArrayList<>(7);

    private int daysRemaining = 7;
    private int energyRemaining = MAX_ENERGY;
    private float hoursRemaining = 16;
    private Day currentDay = new Day();

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public int getEnergyRemaining() {
        return energyRemaining;
    }

    public float getHoursRemaining() {
        return hoursRemaining;
    }

    public Day getCurrentDay() {
        return currentDay;
    }

    public void advanceDay() {
        daysRemaining--;
        energyRemaining = MAX_ENERGY;

        days.add(currentDay);
        currentDay = new Day();
    }

    public boolean doActivity(int timeUsage, int energyUsage, ActivityType type) {
        if (hoursRemaining < timeUsage || energyRemaining < energyUsage) {
            return false;
        }

        hoursRemaining -= timeUsage;
        energyRemaining -= energyUsage;

        currentDay.activityStats.merge(type, 1, Integer::sum);

        return true;
    }
}
