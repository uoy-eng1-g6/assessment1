package io.github.uoyeng1g6.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.InteractionComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.components.TextureComponent;
import io.github.uoyeng1g6.components.VelocityComponent;
import io.github.uoyeng1g6.enums.MoveDirection;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.systems.AnimationSystem;
import io.github.uoyeng1g6.systems.InteractionSystem;
import io.github.uoyeng1g6.systems.MapRenderingSystem;
import io.github.uoyeng1g6.systems.MovementSystem;
import io.github.uoyeng1g6.systems.PlayerInputSystem;
import io.github.uoyeng1g6.systems.RenderingSystem;

public class Playing implements Screen {
    final HeslingtonHustle game;

    final OrthographicCamera camera;
    final Viewport viewport;

    Engine engine;
    GameState gameState;

    public Playing(HeslingtonHustle game) {
        this.game = game;
        this.engine = new PooledEngine();
        this.gameState = new GameState();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 100, 80);
        viewport = new FitViewport(100, 80, camera);

        engine.addEntity(initPlayer(engine));

        var library = engine.createEntity()
                .add(new PositionComponent(2, 2))
                .add(new TextureComponent(game.buildingTextureAtlas.findRegion("B01"), 0.05f).show())
                .add(new InteractionComponent(state -> System.out.println("INTERACTED"), new Rectangle(2, 2, 5, 5)));
        engine.addEntity(library);

        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new InteractionSystem(gameState));
        engine.addSystem(new MapRenderingSystem(game.tiledMap, camera));
        engine.addSystem(new RenderingSystem(game.spriteBatch));
        engine.addSystem(new AnimationSystem(game.spriteBatch));
    }

    Entity initPlayer(Engine engine) {
        var playerAnimations = new AnimationComponent(0.125f);
        playerAnimations.animations.put(
                MoveDirection.STATIONARY,
                new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_down"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(
                MoveDirection.UP,
                new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_up"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(
                MoveDirection.DOWN,
                new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_down"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(
                MoveDirection.LEFT,
                new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_left"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(
                MoveDirection.RIGHT,
                new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_right"), Animation.PlayMode.LOOP));

        return engine.createEntity()
                .add(new PlayerComponent())
                .add(new PositionComponent())
                .add(new VelocityComponent())
                .add(playerAnimations);
    }

    @Override
    public void render(float delta) {
        game.spriteBatch.begin();
        engine.update(delta);
        game.spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
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
