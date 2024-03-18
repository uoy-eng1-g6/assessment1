package io.github.uoyeng1g6.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.CounterComponent;
import io.github.uoyeng1g6.components.FixtureComponent;
import io.github.uoyeng1g6.components.HitboxComponent;
import io.github.uoyeng1g6.components.InteractionComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.components.TextureComponent;
import io.github.uoyeng1g6.components.TooltipComponent;
import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.constants.MoveDirection;
import io.github.uoyeng1g6.constants.PlayerConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.models.PhysicsPolygon;
import io.github.uoyeng1g6.systems.AnimationSystem;
import io.github.uoyeng1g6.systems.CounterUpdateSystem;
import io.github.uoyeng1g6.systems.DebugSystem;
import io.github.uoyeng1g6.systems.InteractionOverlayRenderingSystem;
import io.github.uoyeng1g6.systems.MapRenderingSystem;
import io.github.uoyeng1g6.systems.PlayerInputSystem;
import io.github.uoyeng1g6.systems.PlayerInteractionSystem;
import io.github.uoyeng1g6.systems.StaticRenderingSystem;
import io.github.uoyeng1g6.systems.TooltipRenderingSystem;
import java.util.Map;

public class Playing implements Screen {
    final HeslingtonHustle game;

    final OrthographicCamera camera;
    final Viewport viewport;

    Stage stage;
    Table counters;
    Container<Label> days;

    Engine engine;
    GameState gameState;
    World world;

    Box2DDebugRenderer debugRenderer = null;

    public Playing(HeslingtonHustle game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
        viewport = new FitViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, camera);

        stage = new Stage(viewport);

        counters = new Table(game.skin);
        counters.setFillParent(true);
        counters.pad(1);
        counters.setDebug(game.debug);
        stage.addActor(counters);

        var labelStyle = new Label.LabelStyle(game.tooltipFont, Color.WHITE);
        var daysLabel = new Label("Monday", labelStyle);
        daysLabel.setFontScale(0.17f);

        days = new Container<>(daysLabel);
        days.setFillParent(true);
        stage.addActor(days);
        days.center().top();

        var studyLabel = new Label("0", labelStyle);
        studyLabel.setFontScale(0.15f);
        var eatLabel = new Label("0", labelStyle);
        eatLabel.setFontScale(0.15f);
        var recreationLabel = new Label("0", labelStyle);
        recreationLabel.setFontScale(0.15f);

        var studyIcon = game.interactionIconsTextureAtlas.findRegion("book_icon");
        var eatIcon = game.interactionIconsTextureAtlas.findRegion("food_icon");
        var recreationIcon = game.interactionIconsTextureAtlas.findRegion("popcorn_icon");

        var studyImage = new Image(studyIcon);
        var eatImage = new Image(eatIcon);
        var recreationImage = new Image(recreationIcon);

        counters.top().right();
        counters.add(studyImage).width(3).height(3).padRight(0.25f);
        counters.add(studyLabel);
        counters.row();
        counters.add(eatImage).width(3).height(3).padRight(0.25f);
        counters.add(eatLabel);
        counters.row();
        counters.add(recreationImage).width(3).height(3).padRight(0.25f);
        counters.add(recreationLabel);

        this.engine = new PooledEngine();
        this.gameState = new GameState();
        this.world = new World(new Vector2(), true);

        initTerrain();

        engine.addEntity(initPlayerEntity(engine));

        for (var entity : initInteractionLocations(engine)) {
            engine.addEntity(entity);
        }

        engine.addEntity(
                engine.createEntity().add(new CounterComponent(daysLabel, new CounterComponent.CounterValueResolver() {
                    // spotless:off
                    private final Map<Integer, String> dayNameMap = Map.of(
                            7, "Monday", 6, "Tuesday", 5, "Wednesday",
                            4, "Thursday", 3, "Friday", 2, "Saturday",
                            1, "Sunday - Exam Tomorrow"
                    );
                    // spotless:on

                    @Override
                    public String resolveValue(GameState gameState) {
                        return dayNameMap.get(gameState.daysRemaining);
                    }
                })));
        engine.addEntity(engine.createEntity()
                .add(new CounterComponent(
                        studyLabel, state -> String.valueOf(state.currentDay.statFor(ActivityType.STUDY)))));
        engine.addEntity(engine.createEntity()
                .add(new CounterComponent(
                        eatLabel, state -> String.valueOf(state.currentDay.statFor(ActivityType.MEAL)))));
        engine.addEntity(engine.createEntity()
                .add(new CounterComponent(
                        recreationLabel, state -> String.valueOf(state.currentDay.statFor(ActivityType.RECREATION)))));

        engine.addSystem(new PlayerInputSystem(gameState));
        engine.addSystem(new PlayerInteractionSystem(gameState));
        engine.addSystem(new MapRenderingSystem(game.tiledMap, camera));
        engine.addSystem(new StaticRenderingSystem(game.spriteBatch));
        engine.addSystem(new AnimationSystem(game.spriteBatch, gameState));
        engine.addSystem(new TooltipRenderingSystem(game.tooltipFont, game.shapeDrawer, game.spriteBatch, gameState));
        engine.addSystem(new CounterUpdateSystem(gameState));
        if (game.debug) {
            engine.addSystem(new DebugSystem(game.shapeDrawer));
            debugRenderer = new Box2DDebugRenderer();
        }
        engine.addSystem(
                new InteractionOverlayRenderingSystem(game.spriteBatch, game.overlayFont, game.shapeDrawer, gameState));
    }

    void initTerrain() {
        var json = new Json();
        var objects = json.fromJson(PhysicsPolygon[].class, Gdx.files.internal("terrain.json"));

        for (var object : objects) {
            var bodyDef = new BodyDef();
            bodyDef.type = object.getType();
            bodyDef.position.set(object.getPosition());

            var body = world.createBody(bodyDef);
            var shape = new PolygonShape();
            shape.set(object.getVertices());

            // We know that these will always be static bodies so will always have a density of 0
            body.createFixture(shape, 0f);
            shape.dispose();
        }
    }

    Entity[] initInteractionLocations(Engine engine) {
        final var iconSize = 2 / 64f;

        var studyIcon = game.interactionIconsTextureAtlas.findRegion("book_icon");
        var study = engine.createEntity()
                .add(new TextureComponent(studyIcon, iconSize).show())
                .add(new PositionComponent(25, 14))
                .add(new HitboxComponent(new Rectangle(
                        25, 14, studyIcon.getRegionWidth() * iconSize, studyIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(1, 10, ActivityType.STUDY, "Studying...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "[E] Study for exams\nTime: -1h\nEnergy: -10"));

        var foodIcon = game.interactionIconsTextureAtlas.findRegion("food_icon");
        var food = engine.createEntity()
                .add(new TextureComponent(foodIcon, iconSize).show())
                .add(new PositionComponent(54, 2.5f))
                .add(new HitboxComponent(new Rectangle(
                        54, 2.5f, foodIcon.getRegionWidth() * iconSize, foodIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(1, 10, ActivityType.MEAL, "Eating...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "[E] Eat at Piazza\nTime: -1h\nEnergy: -10"));

        var popcornIcon = game.interactionIconsTextureAtlas.findRegion("popcorn_icon");
        var recreation = engine.createEntity()
                .add(new TextureComponent(popcornIcon, iconSize).show())
                .add(new PositionComponent(53.5f, 26.5f))
                .add(new HitboxComponent(new Rectangle(
                        53.5f,
                        26.5f,
                        popcornIcon.getRegionWidth() * iconSize,
                        popcornIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(1, 10, ActivityType.RECREATION, "Watching films...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "[E] Watch films with mates\nTime: -1h\nEnergy: -10"));

        var sleepIcon = game.interactionIconsTextureAtlas.findRegion("bed_icon");
        var sleep = engine.createEntity()
                .add(new TextureComponent(sleepIcon, iconSize).show())
                .add(new PositionComponent(3.5f, 26.5f))
                .add(new HitboxComponent(new Rectangle(
                        3.5f, 26.5f, sleepIcon.getRegionWidth() * iconSize, sleepIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(GameState::advanceDay))
                .add(new TooltipComponent(game.tooltipFont, "[E] Go to sleep\nEnds the current day"));

        return new Entity[] {study, food, recreation, sleep};
    }

    Fixture initPlayerBody() {
        var player = new BodyDef();
        player.type = BodyDef.BodyType.DynamicBody;
        player.position.set(PlayerConstants.START_POSITION);
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
        ScreenUtils.clear(0, 0, 0, 1);

        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        game.spriteBatch.setProjectionMatrix(camera.combined);
        game.spriteBatch.begin();

        engine.update(delta);
        if (game.physicsDebug) {
            debugRenderer.render(world, camera.combined);
        }
        game.spriteBatch.end();

        stage.act();
        stage.draw();

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
        stage.dispose();
        world.dispose();
    }
}
