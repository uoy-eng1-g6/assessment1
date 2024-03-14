package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.components.VelocityComponent;

public class MovementSystem extends IteratingSystem {
    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
