package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import io.github.uoyeng1g6.models.GameState;

/**
 * Component allowing an entity to have a dynamic value computation function. Intended
 * for use within overlaid counters backed by scene2d labels.
 */
public class CounterComponent implements Component {
    /**
     * Interface representing a counter value resolver. Represented as a functional
     * interface instead of a function to allow entities to easily contain their own state.
     */
    @FunctionalInterface
    public interface CounterValueResolver {
        /**
         * Get the new value for the label's text.
         *
         * @param gameState the current state of the game.
         * @return the new label's text.
         */
        String resolveValue(GameState gameState);
    }

    /**
     * The label for the entity;
     */
    public final Label label;
    /**
     * The resolver to use to get the updated label text.
     */
    public final CounterValueResolver valueResolver;

    public CounterComponent(Label label, CounterValueResolver valueResolver) {
        this.label = label;
        this.valueResolver = valueResolver;
    }
}
