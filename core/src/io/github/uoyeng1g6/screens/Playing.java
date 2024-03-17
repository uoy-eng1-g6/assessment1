package io.github.uoyeng1g6.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.FixtureComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.constants.MoveDirection;
import io.github.uoyeng1g6.constants.PlayerConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.systems.AnimationSystem;
import io.github.uoyeng1g6.systems.DebugSystem;
import io.github.uoyeng1g6.systems.MapRenderingSystem;
import io.github.uoyeng1g6.systems.PlayerInputSystem;
import io.github.uoyeng1g6.systems.PlayerInteractionSystem;
import io.github.uoyeng1g6.systems.StaticRenderingSystem;

public class Playing implements Screen {
    private static final int WORLD_WIDTH = 65;
    private static final int WORLD_HEIGHT = 54;

    final HeslingtonHustle game;

    final OrthographicCamera camera;
    final Viewport viewport;

    Engine engine;
    GameState gameState;
    World world;

    Box2DDebugRenderer debugRenderer = null;

    public Playing(HeslingtonHustle game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        this.engine = new PooledEngine();
        this.gameState = new GameState();
        this.world = new World(new Vector2(0, 0), true);

        initWalls();
        initTerrain();

        engine.addEntity(initPlayerEntity(engine));

        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new PlayerInteractionSystem(gameState));
        engine.addSystem(new MapRenderingSystem(game.tiledMap, camera));
        engine.addSystem(new StaticRenderingSystem(game.spriteBatch));
        engine.addSystem(new AnimationSystem(game.spriteBatch));
        if (game.debug) {
            engine.addSystem(new DebugSystem(game.shapeDrawer));
            debugRenderer = new Box2DDebugRenderer();
        }
    }

    void initWalls() {
        var left = new BodyDef();
        left.type = BodyDef.BodyType.StaticBody;
        left.position.set(new Vector2(0, (float) WORLD_HEIGHT / 2));
        var leftBody = world.createBody(left);
        var leftBox = new PolygonShape();
        leftBox.setAsBox(0.025f, (float) WORLD_HEIGHT / 2);
        leftBody.createFixture(leftBox, 0f);
        leftBox.dispose();

        var right = new BodyDef();
        right.type = BodyDef.BodyType.StaticBody;
        right.position.set(new Vector2(WORLD_WIDTH, (float) WORLD_HEIGHT / 2));
        var rightBody = world.createBody(right);
        var rightBox = new PolygonShape();
        rightBox.setAsBox(0.025f, (float) WORLD_HEIGHT / 2);
        rightBody.createFixture(rightBox, 0f);
        rightBox.dispose();

        var top = new BodyDef();
        top.type = BodyDef.BodyType.StaticBody;
        top.position.set(new Vector2((float) WORLD_WIDTH / 2, WORLD_HEIGHT));
        var topBody = world.createBody(top);
        var topBox = new PolygonShape();
        topBox.setAsBox((float) WORLD_WIDTH / 2, 0.025f);
        topBody.createFixture(topBox, 0f);
        topBox.dispose();

        var bottom = new BodyDef();
        bottom.type = BodyDef.BodyType.StaticBody;
        bottom.position.set(new Vector2((float) WORLD_WIDTH / 2, 0));
        var bottomBody = world.createBody(bottom);
        var bottomBox = new PolygonShape();
        bottomBox.setAsBox((float) WORLD_WIDTH / 2, 0.025f);
        bottomBody.createFixture(bottomBox, 0f);
        bottomBox.dispose();
    }

    void initTerrain() {
        var accom1 = new BodyDef();
        accom1.type = BodyDef.BodyType.StaticBody;
        accom1.position.set(5.5f, 42);
        var accom1Body = world.createBody(accom1);
        var accom1Box = new PolygonShape();
        accom1Box.setAsBox(4.5f, 11);
        accom1Body.createFixture(accom1Box, 0f);
        accom1Box.dispose();

        var accom2 = new BodyDef();
        accom2.type = BodyDef.BodyType.StaticBody;
        accom2.position.set(22.5f, 42);
        var accom2Body = world.createBody(accom2);
        var accom2Box = new PolygonShape();
        accom2Box.setAsBox(4.5f, 11);
        accom2Body.createFixture(accom2Box, 0f);
        accom2Box.dispose();

        var accom3 = new BodyDef();
        accom3.type = BodyDef.BodyType.StaticBody;
        accom3.position.set(55.5f, 42);
        var accom3Body = world.createBody(accom3);
        var accom3Box = new PolygonShape();
        accom3Box.setAsBox(4.5f, 11);
        accom3Body.createFixture(accom3Box, 0f);
        accom3Box.dispose();

        var pond = new BodyDef();
        pond.type = BodyDef.BodyType.StaticBody;
        pond.position.set(12.5f, 0);
        var pondBody = world.createBody(pond);
        var pondPolygon = new PolygonShape();
        pondPolygon.set(new Vector2[] {
            new Vector2(0, 0), new Vector2(0, 1.5f), new Vector2(10, 4), new Vector2(16, 5),
            new Vector2(24, 5), new Vector2(26, 4), new Vector2(26, 2), new Vector2(28, 0)
        });
        pondBody.createFixture(pondPolygon, 0f);
        pondPolygon.dispose();

        var csBuilding = new BodyDef();
        csBuilding.type = BodyDef.BodyType.StaticBody;
        csBuilding.position.set(7, 12);
        var csBuildingBody = world.createBody(csBuilding);
        var csBuildingPolygon = new PolygonShape();
        csBuildingPolygon.set(new Vector2[] {
            new Vector2(0, 0), new Vector2(5, -2), new Vector2(12, -2), new Vector2(17, 0),
            new Vector2(17, 11), new Vector2(12, 13.5f), new Vector2(8, 13.5f), new Vector2(0, 9)
        });
        csBuildingBody.createFixture(csBuildingPolygon, 0f);
        csBuildingPolygon.dispose();

        var piazzaBuilding = new BodyDef();
        piazzaBuilding.type = BodyDef.BodyType.StaticBody;
        piazzaBuilding.position.set(45, 11);
        var piazzaBuildingBody = world.createBody(piazzaBuilding);
        var piazzaBuildingPolygon = new PolygonShape();
        piazzaBuildingPolygon.set(new Vector2[] {
            new Vector2(0, 0),
            new Vector2(8, -4),
            new Vector2(20, -4),
            new Vector2(20, 8),
            new Vector2(9, 8),
            new Vector2(6, 16),
            new Vector2(0, 16)
        });
        piazzaBuildingBody.createFixture(piazzaBuildingPolygon, 0f);
        piazzaBuildingPolygon.dispose();
    }

    Fixture initPlayerBody() {
        var player = new BodyDef();
        player.type = BodyDef.BodyType.DynamicBody;
        player.position.set(5, 20);
        var playerBody = world.createBody(player);
        playerBody.setUserData(PlayerConstants.HITBOX_RADIUS);
        var playerCircle = new CircleShape();
        playerCircle.setRadius(PlayerConstants.HITBOX_RADIUS);
        var playerFixture = playerBody.createFixture(playerCircle, 1f);
        playerCircle.dispose();

        return playerFixture;
    }

    Entity initPlayerEntity(Engine engine) {
        var playerAnimations = new AnimationComponent(0.05f);
        playerAnimations.animations.put(
                MoveDirection.STATIONARY,
                new Animation<>(1f, game.playerTextureAtlas.createSprites("stationary"), Animation.PlayMode.LOOP));
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
                .add(playerAnimations)
                .add(new FixtureComponent(initPlayerBody()));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        game.spriteBatch.setProjectionMatrix(camera.combined);
        game.spriteBatch.begin();

        engine.update(delta);
//        if (game.debug) {
//            debugRenderer.render(world, camera.combined);
//        }
        game.spriteBatch.end();

        world.step(delta, 8, 3);
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
    public void dispose() {
        world.dispose();
    }
}
