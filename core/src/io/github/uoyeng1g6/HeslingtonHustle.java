package io.github.uoyeng1g6;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.uoyeng1g6.screens.MainMenu;

public class HeslingtonHustle extends Game {
    public enum State {
        MAIN_MENU, PLAYING, END_SCREEN
    }

    public final boolean debug;

    public Skin skin;

    Screen mainMenu;

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

        mainMenu = new MainMenu(this);

        this.setScreen(mainMenu);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        mainMenu.dispose();
        skin.dispose();
    }
}
