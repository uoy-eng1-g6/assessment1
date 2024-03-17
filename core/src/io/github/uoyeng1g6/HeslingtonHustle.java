package io.github.uoyeng1g6;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.uoyeng1g6.screens.MainMenu;
import io.github.uoyeng1g6.screens.Playing;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class HeslingtonHustle extends Game {
    public enum State {
        MAIN_MENU,
        PLAYING,
        END_SCREEN
    }

    public final boolean debug;
    public final boolean physicsDebug;

    public Skin skin;

    public TextureAtlas playerTextureAtlas;
    public TextureAtlas interactionIconsTextureAtlas;
    public Texture whitePixel;

    public TiledMap tiledMap;

    public BitmapFont tooltipFont;
    public SpriteBatch spriteBatch;
    public ShapeDrawer shapeDrawer;

    Screen mainMenu;
    Screen playing;

    private State currentState = State.MAIN_MENU;

    public HeslingtonHustle() {
        debug = System.getProperty("game.debug", "false").equals("true");
        physicsDebug = System.getProperty("game.physicsDebug", "false").equals("true");
    }

    public void quit() {
        Gdx.app.exit();
    }

    public void setState(State state) {
        switch (state) {
            case MAIN_MENU:
                this.setScreen(mainMenu);
                break;
            case PLAYING:
                this.setScreen(playing);
                break;
            case END_SCREEN:
                break;
        }
        currentState = state;
    }

    @Override
    public void create() {
        Box2D.init();

        skin = new Skin(Gdx.files.internal("skins/default/uiskin.json"));

        playerTextureAtlas = new TextureAtlas(Gdx.files.internal("sprites/player.txt"));
        interactionIconsTextureAtlas = new TextureAtlas(Gdx.files.internal("sprites/interaction_icons.txt"));

        tiledMap = new TmxMapLoader().load("maps/campus-east.tmx");

        tooltipFont = new BitmapFont();
        tooltipFont.getData().setScale(0.07f);
        tooltipFont.setUseIntegerPositions(false);
        tooltipFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tooltipFont.setColor(Color.BLACK);

        spriteBatch = new SpriteBatch();
        whitePixel = new Texture(Gdx.files.internal("white_pixel.png"));
        shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(whitePixel, 0, 0, 1, 1));

        mainMenu = new MainMenu(this);
        playing = new Playing(this);

        this.setScreen(mainMenu);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        mainMenu.dispose();
        playing.dispose();

        skin.dispose();

        playerTextureAtlas.dispose();
        interactionIconsTextureAtlas.dispose();
        whitePixel.dispose();

        tiledMap.dispose();

        tooltipFont.dispose();
        spriteBatch.dispose();
    }
}
