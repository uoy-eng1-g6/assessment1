package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import io.github.uoyeng1g6.models.GameState;
import java.util.function.Function;

public class CounterComponent implements Component {
    public final Label label;
    public final Function<GameState, String> valueFunction;

    public CounterComponent(Label label, Function<GameState, String> valueFunction) {
        this.label = label;
        this.valueFunction = valueFunction;
    }
}
