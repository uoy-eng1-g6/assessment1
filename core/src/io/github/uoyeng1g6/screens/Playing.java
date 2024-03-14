package io.github.uoyeng1g6.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.components.VelocityComponent;
import io.github.uoyeng1g6.enums.MoveDirection;
import io.github.uoyeng1g6.systems.AnimationSystem;
import io.github.uoyeng1g6.systems.MapRenderingSystem;
import io.github.uoyeng1g6.systems.MovementSystem;
import io.github.uoyeng1g6.systems.PlayerInputSystem;
import io.github.uoyeng1g6.systems.RenderingSystem;

public class Playing implements Screen {
    final HeslingtonHustle game;

    final OrthographicCamera camera;
    final Viewport viewport;

    Engine engine;

    public Playing(HeslingtonHustle game) {
        this.game = game;
        this.engine = new PooledEngine();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 60, 40);
//        camera.zoom = 0.5f;

        viewport = new FitViewport(60, 40, camera);

        var playerAnimations = new AnimationComponent();
        playerAnimations.animations.put(MoveDirection.STATIONARY, new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_down"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(MoveDirection.UP, new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_up"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(MoveDirection.DOWN, new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_down"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(MoveDirection.LEFT, new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_left"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(MoveDirection.RIGHT, new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_right"), Animation.PlayMode.LOOP));
        var player = engine.createEntity()
                .add(new PlayerComponent())
                .add(new PositionComponent())
                .add(new VelocityComponent())
                .add(playerAnimations);

        engine.addEntity(player);

        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new MapRenderingSystem(game.tiledMap, camera));
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new RenderingSystem(game.spriteBatch));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        engine.update(delta);

//        player.animator.setAnimation(player.movable.moving);
//        var sprite = player.animator.getKeyFrame();
//
//        player.movable.clamp(viewport, sprite);
//        sprite.setX(player.movable.x);
//        sprite.setY(player.movable.y);
//
//        game.spriteBatch.begin();
//        sprite.draw(game.spriteBatch);
//        game.spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
}
