package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import io.github.uoyeng1g6.models.GameState;

public class InteractionComponent implements Component {
    @FunctionalInterface
    public interface Interactable {
        void interact(GameState state);
    }

    public final Interactable interactable;

    public InteractionComponent(Interactable interactable) {
        this.interactable = interactable;
    }
}
