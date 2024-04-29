package io.github.uoyeng1g6.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class LeaderboardManager {

    class Ranking {
        String player;
        int score;
    }

    ArrayList<Ranking> leaderboard = new ArrayList<Ranking>();

    File file;

    /**
     * Controls access, ranking and updating of the leaderboard file
     * @param filepath File path of a .txt that stores the leaderboard
     */
    public LeaderboardManager(String filepath) {
        try {
            file = new File(filepath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] res = line.split(",");

                Ranking entry = new Ranking();
                entry.player = res[0];
                entry.score = Integer.parseInt(res[1]);

                leaderboard.add(entry);
            }
            sortLeaderboard();
            if (leaderboard.size() > 10) {
                leaderboard.subList(9, leaderboard.size() - 1).clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sortLeaderboard() {
        leaderboard.sort(new SortByScore().thenComparing(new SortByName()));
    }

    public String[][] getRanking() {
        String[][] stringRanking = new String[leaderboard.size()][2];
        for (int i = 0; i < leaderboard.size(); i++) {
            stringRanking[i][0] = leaderboard.get(i).player;
            stringRanking[i][1] = Integer.toString(leaderboard.get(i).score);
        }
        return stringRanking;
    }

    public String[][] addEntry(String playerName, int score) {
        Ranking newEntry = new Ranking();
        newEntry.player = playerName;
        newEntry.score = score;

        if (leaderboard.size() < 10) {
            leaderboard.add(newEntry);
            sortLeaderboard();
        } else {
            leaderboard.add(newEntry);
            sortLeaderboard();
            leaderboard.remove(10);
        }
        writeToFile();
        return getRanking();
    }

    public void writeToFile() {
        try {
            FileWriter writer = new FileWriter(file.getPath());
            for (Ranking entry : leaderboard) {
                writer.write(entry.player + "," + Integer.toString(entry.score) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SortByScore implements Comparator<Ranking> {
        @Override
        public int compare(Ranking o1, Ranking o2) {
            return o2.score - o1.score;
        }
    }

    class SortByName implements Comparator<Ranking> {

        @Override
        public int compare(Ranking o1, Ranking o2) {
            return o1.player.compareTo(o2.player);
        }
    }
}
