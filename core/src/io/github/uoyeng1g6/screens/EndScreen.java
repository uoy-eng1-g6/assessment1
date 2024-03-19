package io.github.uoyeng1g6.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.utils.ChangeListener;
import java.util.List;

public class EndScreen implements Screen {
    private static final float MAX_DAY_SCORE = 105.125f;
    private static final float MIN_DAY_SCORE = 0f;

    private final HeslingtonHustle game;
    private final GameState endGameState;

    Camera camera;
    Stage stage;

    public EndScreen(HeslingtonHustle game, GameState endGameState) {
        this.game = game;
        this.endGameState = endGameState;

        camera = new OrthographicCamera();
        var viewport = new FitViewport(GameConstants.WORLD_WIDTH * 10, GameConstants.WORLD_HEIGHT * 10, camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        var root = new Table(game.skin);
        root.setFillParent(true);
        root.pad(0.25f);

        root.setDebug(game.debug);
        stage.addActor(root);

        root.add("Game Over").getActor().setFontScale(2);
        root.row();

        var inner = new Table(game.skin);

        inner.add(String.format("Exam Score: %.2f / 100", calculateExamScore(endGameState.days))).padBottom(50);
        inner.row();
        inner.add("Times Studied: " + endGameState.getTotalActivityCount(ActivityType.STUDY));
        inner.row();
        inner.add("Meals Eaten: " + endGameState.getTotalActivityCount(ActivityType.MEAL));
        inner.row();
        inner.add("Recreational Activities Done: " + endGameState.getTotalActivityCount(ActivityType.RECREATION));
        inner.row();

        var mainMenuButton = new TextButton("Main Menu", game.skin);
        mainMenuButton.addListener(ChangeListener.of((e, a) -> game.setState(HeslingtonHustle.State.MAIN_MENU)));
        inner.add(mainMenuButton)
                .padTop(50)
                .width(Value.percentWidth(0.4f, inner))
                .height(Value.percentHeight(0.1f, inner));

        root.add(inner).grow();
    }

    float getDayScore(int studyCount, int mealCount, int recreationCount) {
        var studyPoints = 0;
        for (int i = 1; i <= studyCount; i++) {
            studyPoints += i <= 5 ? 10 : -5;
        }
        studyPoints = Math.max(0, studyPoints);

        // Calculate meal multiplier
        float mealMultiplier = 1;
        for (var i = 1; i <= mealCount; i++) {
            mealMultiplier += i <= 3 ? 0.15f : -0.025f;
        }
        mealMultiplier = Math.max(1, mealMultiplier);

        // Calculate recreation multiplier
        float recreationMultiplier = 1;
        for (var i = 1; i <= recreationCount; i++) {
            recreationMultiplier += i <= 3 ? 0.15f : -0.025f;
        }
        recreationMultiplier = Math.max(1, recreationMultiplier);

        // Calculate day score
        return studyPoints * mealMultiplier * recreationMultiplier;
    }

    float calculateExamScore(List<GameState.Day> days) {
        float totalScore = 0;

        for (var day : days) {
            int studyCount = day.statFor(ActivityType.STUDY);
            int mealCount = day.statFor(ActivityType.MEAL);
            int recreationCount = day.statFor(ActivityType.RECREATION);

            var dayScore = getDayScore(studyCount, mealCount, recreationCount);
            // Normalise day score between 0 and 100, round up to nearest whole number
            var normalisedDayScore = Math.ceil(((dayScore - MIN_DAY_SCORE) * 100) / (MAX_DAY_SCORE - MIN_DAY_SCORE));

            // Increase total score
            totalScore += (float) (normalisedDayScore * (1 / 7f));
        }

        // Clamp total score from 0-100
        return Math.min(100, Math.max(0, totalScore));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
