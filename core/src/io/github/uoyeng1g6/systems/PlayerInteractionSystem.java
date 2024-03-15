package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.InteractionComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.models.GameState;

public class PlayerInteractionSystem extends EntitySystem {
    private final ComponentMapper<PlayerComponent> plm = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private final ComponentMapper<InteractionComponent> im = ComponentMapper.getFor(InteractionComponent.class);

    private final GameState gameState;

    private Entity playerEntity;
    private ImmutableArray<Entity> interactables;

    public PlayerInteractionSystem(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void addedToEngine(Engine engine) {
        playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class, PositionComponent.class)
                        .get())
                .first();
        interactables =
                engine.getEntitiesFor(Family.all(InteractionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        var plc = plm.get(playerEntity);
        if (!plc.isInteracting) {
            return;
        }

        var ac = am.get(playerEntity);
        var playerBoundingBox =
                ac.animations.get(ac.currentAnimation).getKeyFrame(ac.time).getBoundingRectangle();
        for (var entity : interactables) {
            var ic = im.get(entity);
            if (!Intersector.overlaps(playerBoundingBox, ic.playerInteractionPosition)) {
                return;
            }

            ic.interactable.interact(this.gameState);
        }
    }
}
