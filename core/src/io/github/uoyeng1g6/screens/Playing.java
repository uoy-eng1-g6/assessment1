package io.github.uoyeng1g6.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.FixtureComponent;
import io.github.uoyeng1g6.components.HitboxComponent;
import io.github.uoyeng1g6.components.InteractionComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.components.TextureComponent;
import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.MoveDirection;
import io.github.uoyeng1g6.constants.PlayerConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.models.PhysicsPolygon;
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

        initTerrain();

        engine.addEntity(initPlayerEntity(engine));

        for (var entity : initInteractionLocations(engine)) {
            engine.addEntity(entity);
        }

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

    void initTerrain() {
        var json = new Json();
        var objects = json.fromJson(PhysicsPolygon[].class, Gdx.files.internal("terrain.json"));

        for (var object: objects) {
            var bodyDef = new BodyDef();
            bodyDef.type = object.getType();
            bodyDef.position.set(object.getPosition());

            var body = world.createBody(bodyDef);
            var shape = new PolygonShape();
            shape.set(object.getVertices());

            body.createFixture(shape, 0f);
            shape.dispose();
        }
    }

    Entity[] initInteractionLocations(Engine engine) {
        var studyIcon = game.interactionIconsTextureAtlas.findRegion("book_icon");
        var study = engine.createEntity()
                .add(new TextureComponent(studyIcon, 2/64f).show())
                .add(new PositionComponent(25, 15))
                .add(new HitboxComponent(new Rectangle(25, 15, studyIcon.getRegionWidth() * (2/64f), studyIcon.getRegionHeight() * (2/64f))))
                .add(new InteractionComponent(state -> state.doActivity(1, 10, ActivityType.STUDY)));

        return new Entity[]{study};
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
        if (game.physicsDebug) {
            debugRenderer.render(world, camera.combined);
        }
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
