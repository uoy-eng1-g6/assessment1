package io.github.uoyeng1g6;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.uoyeng1g6.screens.MainMenu;
import io.github.uoyeng1g6.screens.Playing;

public class HeslingtonHustle extends Game {
    public enum State {
        MAIN_MENU,
        PLAYING,
        END_SCREEN
    }

    public final boolean debug;

    public Skin skin;
    public TextureAtlas playerTextureAtlas;
    public TiledMap tiledMap;

    public SpriteBatch spriteBatch;

    Screen mainMenu;
    Screen playing;

    private State currentState = State.MAIN_MENU;

    public HeslingtonHustle() {
        debug = System.getProperty("game.debug", "false").equals("true");
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

    public State getCurrentState() {
        return this.currentState;
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("skins/default/uiskin.json"));
        playerTextureAtlas = new TextureAtlas(Gdx.files.internal("sprites/player.txt"));
        tiledMap = new TmxMapLoader().load("maps/campus-east.tmx");

        spriteBatch = new SpriteBatch();

        mainMenu = new MainMenu(this);
        playing = new Playing(this);

        this.setScreen(playing);
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
        tiledMap.dispose();
        spriteBatch.dispose();
    }
}
