package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.uoyeng1g6.components.CounterComponent;
import io.github.uoyeng1g6.models.GameState;

/**
 * System that handles updating counter label text.
 */
public class CounterUpdateSystem extends IteratingSystem {
    private final GameState gameState;

    private final ComponentMapper<CounterComponent> cm = ComponentMapper.getFor(CounterComponent.class);

    public CounterUpdateSystem(GameState gameState) {
        super(Family.all(CounterComponent.class).get());

        this.gameState = gameState;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var cc = cm.get(entity);
        cc.label.setText(cc.valueResolver.resolveValue(gameState));
    }
}
