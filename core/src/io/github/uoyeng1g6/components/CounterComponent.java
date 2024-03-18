package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import io.github.uoyeng1g6.models.GameState;

public class CounterComponent implements Component {
    @FunctionalInterface
    public interface CounterValueResolver {
        String resolveValue(GameState gameState);
    }

    public final Label label;
    public final CounterValueResolver valueResolver;

    public CounterComponent(Label label, CounterValueResolver valueResolver) {
        this.label = label;
        this.valueResolver = valueResolver;
    }
}
