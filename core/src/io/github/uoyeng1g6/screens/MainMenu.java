package io.github.uoyeng1g6.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.utils.ChangeListener;


public class MainMenu implements Screen {
    final HeslingtonHustle game;

    Stage stage;
    Table table;

    public MainMenu(HeslingtonHustle game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table(game.skin);
        table.setFillParent(true);
        table.setDebug(game.debug);
        stage.addActor(table);

        table.add("Heslington Hustle");
        table.row();

        var startButton = new TextButton("Start Game", game.skin);
        startButton.addListener(ChangeListener.of((e, a) -> game.setState(HeslingtonHustle.State.PLAYING)));
        table.add(startButton);

        table.row();

        var quitButton = new TextButton("Quit", game.skin);
        quitButton.addListener(ChangeListener.of((e, a) -> game.quit()));
        table.add(quitButton);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
