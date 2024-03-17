package io.github.uoyeng1g6.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.components.AnimationComponent;
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
import io.github.uoyeng1g6.systems.DebugSystem;
import io.github.uoyeng1g6.systems.MapRenderingSystem;
import io.github.uoyeng1g6.systems.PlayerInputSystem;
import io.github.uoyeng1g6.systems.PlayerInteractionSystem;
import io.github.uoyeng1g6.systems.StaticRenderingSystem;
import io.github.uoyeng1g6.systems.TooltipRenderingSystem;

public class Playing implements Screen {
    final HeslingtonHustle game;

    final OrthographicCamera camera;
    final Viewport viewport;

    final OrthographicCamera uiCamera;
    final Viewport uiViewport;

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

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(800, 600, uiCamera);

        stage = new Stage(uiViewport);

        counters = new Table(game.skin);
        counters.setFillParent(true);
        counters.pad(4);
        counters.setDebug(game.debug);
        stage.addActor(counters);

        var daysLabel = new Label("Monday", game.skin);
        days = new Container<Label>(daysLabel);

        days.setFillParent(true);

        stage.addActor(days);
        days.center().top();
        
        var studyLabel = new Label("0", game.skin);
        var eatLabel = new Label("0", game.skin);
        var funLabel = new Label("0", game.skin);

        // TODO Make appropriate textures when icons for those activities exist 
        var studyIcon = game.interactionIconsTextureAtlas.findRegion("book_icon");
        
        Image studyImage = new Image(studyIcon);
        Image eatImage = new Image(studyIcon);
        Image funImage = new Image(studyIcon);
        
        counters.top().right();
        counters.add(studyImage).width(32).height(32);
        counters.add(studyLabel);
        counters.row();
        counters.add(eatImage).width(32).height(32);
        counters.add(eatLabel);
        counters.row();
        counters.add(funImage).width(32).height(32);
        counters.add(funLabel);


        this.engine = new PooledEngine();
        this.gameState = new GameState();
        this.world = new World(new Vector2(), true);

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
        engine.addSystem(new TooltipRenderingSystem(game.tooltipFont, game.shapeDrawer, game.spriteBatch));
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

            // We know that these will always be static bodies so will always have a density of 0
            body.createFixture(shape, 0f);
            shape.dispose();
        }
    }

    Entity[] initInteractionLocations(Engine engine) {
        var studyIcon = game.interactionIconsTextureAtlas.findRegion("book_icon");
        var study = engine.createEntity()
                .add(new TextureComponent(studyIcon, 2/64f).show())
                .add(new PositionComponent(25, 14))
                .add(new HitboxComponent(new Rectangle(25, 14, studyIcon.getRegionWidth() * (2/64f), studyIcon.getRegionHeight() * (2/64f))))
                .add(new InteractionComponent(state -> state.doActivity(1, 10, ActivityType.STUDY)))
                .add(new TooltipComponent(game.tooltipFont, "[E] Study for exams\nTime: -1h\nEnergy: -10"));

        var foodIcon = game.interactionIconsTextureAtlas.findRegion("food_icon");
        var food = engine.createEntity()
                .add(new TextureComponent(foodIcon, 2/64f).show())
                .add(new PositionComponent(54, 2.5f))
                .add(new HitboxComponent(new Rectangle(54, 2.5f, foodIcon.getRegionWidth() * (2/64f), foodIcon.getRegionHeight() * (2/64f))))
                .add(new InteractionComponent(state -> state.doActivity(1, 10, ActivityType.MEAL)))
                .add(new TooltipComponent(game.tooltipFont, "[E] Eat at Piazza\nTime: -1h\nEnergy: -10"));

        var popcornIcon = game.interactionIconsTextureAtlas.findRegion("popcorn_icon");
        var recreation = engine.createEntity()
                .add(new TextureComponent(popcornIcon, 2/64f).show())
                .add(new PositionComponent(53.5f, 26.5f))
                .add(new HitboxComponent(new Rectangle(54.5f, 26.5f, popcornIcon.getRegionWidth() * (2/64f), popcornIcon.getRegionHeight() * (2/64f))))
                .add(new InteractionComponent(state -> state.doActivity(1, 10, ActivityType.RECREATION)))
                .add(new TooltipComponent(game.tooltipFont, "[E] Watch films with mates\nTime: -1h\nEnergy: -10"));

        var sleepIcon = game.interactionIconsTextureAtlas.findRegion("bed_icon");
        var sleep = engine.createEntity()
                .add(new TextureComponent(sleepIcon, 2/64f).show())
                .add(new PositionComponent(3.5f, 26.5f))
                .add(new HitboxComponent(new Rectangle(3.5f, 26.5f, sleepIcon.getRegionWidth() * (2/64f), sleepIcon.getRegionHeight() * (2/64f))))
                .add(new InteractionComponent(GameState::advanceDay))
                .add(new TooltipComponent(game.tooltipFont, "[E] Go to sleep\nEnds the current day"));  // TODO - maybe confirmation popup?

        return new Entity[]{study, food, recreation, sleep};
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
        ScreenUtils.clear(0, 0, 0.2f, 1);

        game.spriteBatch.setProjectionMatrix(camera.combined);
        game.spriteBatch.begin();

        engine.update(delta);
        if (game.physicsDebug) {
            debugRenderer.render(world, camera.combined);
        }
        game.spriteBatch.end();

        world.step(delta, 8, 3);

        stage.act();
        stage.draw();

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
