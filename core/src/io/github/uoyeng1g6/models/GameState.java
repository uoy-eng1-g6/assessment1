package io.github.uoyeng1g6.models;

import io.github.uoyeng1g6.constants.ActivityType;
import java.util.ArrayList;
import java.util.HashMap;

public class GameState {
    public static class Day {
        public final HashMap<ActivityType, Integer> activityStats = new HashMap<>();

        public int statFor(ActivityType type) {
            return activityStats.getOrDefault(type, 0);
        }
    }

    public static class InteractionOverlay {
        public final String text;
        public final float displayFor;

        public InteractionOverlay(String text, float displayFor) {
            this.text = text;
            this.displayFor = displayFor;
        }
    }

    private static final int MAX_ENERGY = 100;
    private static final int OVERLAY_SECONDS_PER_HOUR = 2;

    public final ArrayList<Day> days = new ArrayList<>(7);
    public Day currentDay = new Day();

    public int daysRemaining = 7;
    public int energyRemaining = MAX_ENERGY;
    public float hoursRemaining = 16;

    public InteractionOverlay interactionOverlay = null;

    public void advanceDay() {
        daysRemaining--;
        energyRemaining = MAX_ENERGY;

        days.add(currentDay);
        currentDay = new Day();

        interactionOverlay = new InteractionOverlay("Sleeping...", 5);
    }

    public boolean doActivity(int timeUsage, int energyUsage, ActivityType type, String overlayText) {
        if (hoursRemaining < timeUsage || energyRemaining < energyUsage) {
            return false;
        }

        hoursRemaining -= timeUsage;
        energyRemaining -= energyUsage;

        currentDay.activityStats.merge(type, 1, Integer::sum);

        interactionOverlay = new InteractionOverlay(overlayText, OVERLAY_SECONDS_PER_HOUR * timeUsage);

        return true;
    }
}
