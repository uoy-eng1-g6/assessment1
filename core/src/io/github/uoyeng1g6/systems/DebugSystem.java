package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.FixtureComponent;
import io.github.uoyeng1g6.components.HitboxComponent;
import io.github.uoyeng1g6.components.InteractionComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.PositionComponent;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class DebugSystem extends EntitySystem {
    private static final float DEBUG_LINE_WIDTH = 0.2f;

    private final ShapeDrawer shapeDrawer;

    private final ComponentMapper<HitboxComponent> hm = ComponentMapper.getFor(HitboxComponent.class);

    private ImmutableArray<Entity> interactables;
    private ImmutableArray<Entity> hitboxEnabled;
    private Entity playerEntity;

    public DebugSystem(ShapeDrawer shapeDrawer) {
        this.shapeDrawer = shapeDrawer;
    }

    @Override
    public void addedToEngine(Engine engine) {
        interactables = engine.getEntitiesFor(
                Family.all(InteractionComponent.class, HitboxComponent.class).get());
        hitboxEnabled = engine.getEntitiesFor(Family.all(HitboxComponent.class)
                .exclude(InteractionComponent.class)
                .get());
        playerEntity = engine.getEntitiesFor(
                        Family.all(PlayerComponent.class, FixtureComponent.class, AnimationComponent.class)
                                .get())
                .first();
    }

    @Override
    public void update(float deltaTime) {
        //        shapeDrawer.rectangle(
        //                Utils.getPlayerHitbox(playerEntity),
        //                Color.RED,
        //                DEBUG_LINE_WIDTH);

        for (var entity : interactables) {
            for (var rect : hm.get(entity).rects) {
                shapeDrawer.rectangle(rect, Color.BLUE, DEBUG_LINE_WIDTH);
            }
        }

        for (var entity : hitboxEnabled) {
            var hc = hm.get(entity);

            for (var rect : hc.rects) {
                shapeDrawer.rectangle(rect, hc.collidable ? Color.GREEN : Color.YELLOW, DEBUG_LINE_WIDTH);
            }
        }
    }
}
