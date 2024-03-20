package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import io.github.uoyeng1g6.models.GameState;

/**
 * Component that allows an entity to be interacted with by the player.
 */
public class InteractionComponent implements Component {
    /**
     * Functional interface representing interaction logic.
     */
    @FunctionalInterface
    public interface Interactable {
        /**
         * Interact with this interactible.
         *
         * @param state the current game state.
         */
        void interact(GameState state);
    }

    /**
     * The interaction logic to run upon this entity being interacted with.
     */
    public final Interactable interactable;

    public InteractionComponent(Interactable interactable) {
        this.interactable = interactable;
    }
}
