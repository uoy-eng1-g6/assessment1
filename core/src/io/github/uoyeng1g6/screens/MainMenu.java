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
import io.github.uoyeng1g6.utils.ChangeListener;

public class MainMenu implements Screen {
    final HeslingtonHustle game;

    Camera camera;
    Stage stage;
    Table root;

    public MainMenu(HeslingtonHustle game) {
        this.game = game;

        camera = new OrthographicCamera();
        var viewport = new FitViewport(800, 600, camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        root = new Table(game.skin);
        root.setFillParent(true);
        root.pad(0.25f);

        root.setDebug(game.debug);
        stage.addActor(root);

        root.add("Heslington Hustle").getActor().setFontScale(2);
        root.row();

        var inner = new Table(game.skin);

        var startButton = new TextButton("Start Game", game.skin);
        startButton.addListener(ChangeListener.of((e, a) -> game.setState(HeslingtonHustle.State.PLAYING)));
        inner.add(startButton).pad(10).width(Value.percentWidth(0.4f, inner)).height(Value.percentHeight(0.1f, inner));

        inner.row();

        var quitButton = new TextButton("Quit", game.skin);
        quitButton.addListener(ChangeListener.of((e, a) -> game.quit()));
        inner.add(quitButton).pad(10).width(Value.percentWidth(0.4f, inner)).height(Value.percentHeight(0.1f, inner));

        root.add(inner).grow();
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        stage.act();
        stage.draw();
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

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
