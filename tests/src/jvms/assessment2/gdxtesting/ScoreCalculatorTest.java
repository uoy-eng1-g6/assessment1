package jvms.assessment2.gdxtesting;

import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.models.ScoreCalculator;
import io.github.uoyeng1g6.screens.EndScreen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ScoreCalculatorTest {

    GameState gameState;

    @Before
    public void initialiseGameState() {
        gameState = new GameState();
    }

    @Test
    public void idealScoreTest() {
        for (int day_count = 1; day_count < 7; day_count++) {
            for (int i=1; i<3; i++) {
                gameState.doActivity(1, 0, ActivityType.MEAL, "", "");
            }
            for (int i=1; i<8; i++) {
                gameState.doActivity(1, 0, ActivityType.STUDY, "", "");
            }
            for (int i=1; i<3; i++) {
                gameState.doActivity(1, 0, ActivityType.RECREATION, "", "");
            }
            gameState.advanceDay();
        }

        int score = ScoreCalculator.calculateExamScore(gameState.days);

        //Testing the score you get is high if you play in the intended way.
        assertTrue(score >= 90 && score <= 100);
    }

    @Test
    public void midScoreTest() {
        for (int day_count = 1; day_count < 7; day_count++) {
            for (int i=1; i<2; i++) {
                gameState.doActivity(1, 0, ActivityType.MEAL, "", "");
            }
            for (int i=1; i<5; i++) {
                gameState.doActivity(1, 0, ActivityType.STUDY, "", "");
            }
            for (int i=1; i<3; i++) {
                gameState.doActivity(1, 0, ActivityType.RECREATION, "", "");
            }
            gameState.advanceDay();
        }
        int score = ScoreCalculator.calculateExamScore(gameState.days);

        //Testing the score you get is mid if you don't play in the intended way but you still do some study.
        assertTrue(score >= 45 && score <= 55);
    }

    @Test
    public void badScoreTest() {
        for (int day_count = 1; day_count < 7; day_count++) {
            for (int i=1; i<5; i++) {
                gameState.doActivity(1, 0, ActivityType.MEAL, "", "");
            }
            for (int i=1; i<2; i++) {
                gameState.doActivity(1, 0, ActivityType.STUDY, "", "");
            }
            for (int i=1; i<1; i++) {
                gameState.doActivity(1, 0, ActivityType.RECREATION, "", "");
            }
            gameState.advanceDay();
        }
        int score = ScoreCalculator.calculateExamScore(gameState.days);

        //Testing the score you get is low if you don't play in the intended way.
        assertTrue(score >= 5 && score <= 15);
    }

    @Test
    public void zeroStudyTest() {
        for (int day_count = 1; day_count < 7; day_count++) {
            for (int i=1; i<3; i++) {
                gameState.doActivity(1, 0, ActivityType.MEAL, "", "");
            }
            for (int i=1; i<4; i++) {
                gameState.doActivity(1, 0, ActivityType.RECREATION, "", "");
            }
            gameState.advanceDay();
        }
        int score = ScoreCalculator.calculateExamScore(gameState.days);
        System.out.println(score);

        //Testing the score you get is zero if you don't study every day.
        assertTrue(score == 0);
    }

    @Test
    public void noStudyCatchupScoreTest() {
        for (int day_count = 1; day_count < 7; day_count++) {
            for (int i=1; i<5; i++) {
                gameState.doActivity(1, 0, ActivityType.MEAL, "", "");
            }
            if (day_count != 2 && day_count != 3) {
                for (int i=1; i<4; i++) {
                    gameState.doActivity(1, 0, ActivityType.STUDY, "", "");
                }
            }
            for (int i=1; i<1; i++) {
                gameState.doActivity(1, 0, ActivityType.RECREATION, "", "");
            }
            gameState.advanceDay();
        }
        int score = ScoreCalculator.calculateExamScore(gameState.days);

        System.out.println(score);

        //Testing the score you get is low if you don't play in the intended way.
        assertTrue(score == 0);
    }

}
