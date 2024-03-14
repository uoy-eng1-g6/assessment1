package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.uoyeng1g6.components.AnimationComponent;

public class AnimationSystem extends IteratingSystem {
    private final ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
