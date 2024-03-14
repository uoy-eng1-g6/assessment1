package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import io.github.uoyeng1g6.models.GameState;

import java.util.function.Consumer;

public class InteractionComponent implements Component {
    @FunctionalInterface
    public interface Interactable {
        void interact(GameState state);
    }

    public final Interactable interactable;
    public final Rectangle playerInteractionPosition;

    public InteractionComponent(Interactable interactable, Rectangle playerInteractionPosition) {
        this.interactable = interactable;
        this.playerInteractionPosition = playerInteractionPosition;
    }
}
