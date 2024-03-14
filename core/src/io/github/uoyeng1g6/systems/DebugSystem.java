package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.InteractionComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.PositionComponent;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class DebugSystem extends EntitySystem {
    private final ShapeDrawer shapeDrawer;

    private final ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<InteractionComponent> im = ComponentMapper.getFor(InteractionComponent.class);

    private ImmutableArray<Entity> interactables;
    private Entity playerEntity;

    public DebugSystem(ShapeDrawer shapeDrawer) {
        this.shapeDrawer = shapeDrawer;
    }

    @Override
    public void addedToEngine(Engine engine) {
        interactables =
                engine.getEntitiesFor(Family.all(InteractionComponent.class).get());
        playerEntity = engine.getEntitiesFor(
                        Family.all(PlayerComponent.class, PositionComponent.class, AnimationComponent.class)
                                .get())
                .first();
    }

    @Override
    public void update(float deltaTime) {
        var playerAnim = am.get(playerEntity);
        var playerSprite =
                playerAnim.animations.get(playerAnim.currentAnimation).getKeyFrame(playerAnim.time);
        var playerPos = pm.get(playerEntity);

        shapeDrawer.rectangle(
                playerPos.x,
                playerPos.y,
                playerSprite.getWidth() * playerAnim.spriteScale,
                playerSprite.getHeight() * playerAnim.spriteScale,
                Color.RED,
                0.25f);

        for (var entity : interactables) {
            shapeDrawer.rectangle(im.get(entity).playerInteractionPosition, Color.BLUE, 0.25f);
        }
    }
}
