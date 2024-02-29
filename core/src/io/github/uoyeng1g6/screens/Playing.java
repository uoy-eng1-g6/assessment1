package io.github.uoyeng1g6.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.uoyeng1g6.HeslingtonHustle;

public class Playing implements Screen {
    private static final float PLAYER_SPEED = 50f;

    final HeslingtonHustle game;

    final Camera camera;
    final Viewport viewport;

    Player player;

    public Playing(HeslingtonHustle game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        this.player = new Player(10, 10);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) player.left(Gdx.graphics.getDeltaTime() * PLAYER_SPEED);
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) player.right(Gdx.graphics.getDeltaTime() * PLAYER_SPEED);
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) player.up(Gdx.graphics.getDeltaTime() * PLAYER_SPEED);
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) player.down(Gdx.graphics.getDeltaTime() * PLAYER_SPEED);

        ScreenUtils.clear(0, 0, 0.2f, 1);

        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Color.WHITE);
        game.shapeRenderer.rect(player.x, player.y, player.width, player.height);
        game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
    public void dispose() {}

    static class Player {
        float x, y, width, height;

        public Player(float x, float y) {
            this.x = x;
            this.y = y;
            this.width = 10;
            this.height = 10;
        }

        void left(float amount) {
            x -= amount;
        }

        void right(float amount) {
            x += amount;
        }

        void up(float amount) {
            y += amount;
        }

        void down(float amount) {
            y -= amount;
        }
    }
}
