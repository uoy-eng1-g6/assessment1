package io.github.uoyeng1g6.models;

import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
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

    public final ArrayList<Day> days = new ArrayList<>(7);
    public Day currentDay = new Day();

    public int daysRemaining = 7;
    public int energyRemaining = GameConstants.MAX_ENERGY;
    public int hoursRemaining = GameConstants.MAX_HOURS;

    public InteractionOverlay interactionOverlay = null;

    public void advanceDay() {
        daysRemaining--;
        energyRemaining = GameConstants.MAX_ENERGY;
        hoursRemaining = GameConstants.MAX_HOURS;

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

        interactionOverlay = new InteractionOverlay(overlayText, GameConstants.OVERLAY_SECONDS_PER_HOUR * timeUsage);

        return true;
    }

    public int getTotalActivityCount(ActivityType type) {
        return days.stream().mapToInt(day -> day.statFor(type)).sum() + currentDay.statFor(type);
    }
}
