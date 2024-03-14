package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import io.github.uoyeng1g6.components.InteractionComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.models.GameState;

public class InteractionSystem extends EntitySystem {
    private final ComponentMapper<PlayerComponent> plm = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<InteractionComponent> im = ComponentMapper.getFor(InteractionComponent.class);

    private final GameState gameState;

    private Entity playerEntity;
    private ImmutableArray<Entity> interactables;

    public InteractionSystem(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void addedToEngine(Engine engine) {
        playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class, PositionComponent.class).get()).first();
        interactables = engine.getEntitiesFor(Family.all(InteractionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        var plc = plm.get(playerEntity);
        if (!plc.isInteracting) {
            return;
        }

        var pc = pm.get(playerEntity);
        for (var entity: interactables) {
            var ic = im.get(entity);

            if (!ic.playerInteractionPosition.contains(pc.x, pc.y)) {
                return;
            }

            ic.interactable.interact(this.gameState);
        }
    }
}
