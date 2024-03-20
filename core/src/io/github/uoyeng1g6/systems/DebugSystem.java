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
import io.github.uoyeng1g6.constants.PlayerConstants;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * System that handles drawing hitboxes around interactable objects and the player if
 * the game was run using debug mode.
 */
public class DebugSystem extends EntitySystem {
    /**
     * The line width to use to draw the hitboxes.
     */
    private static final float DEBUG_LINE_WIDTH = 0.2f;

    /**
     * The shapedrawer to use to draw the hitboxes.
     */
    private final ShapeDrawer shapeDrawer;

    private final ComponentMapper<HitboxComponent> hm = ComponentMapper.getFor(HitboxComponent.class);
    private final ComponentMapper<FixtureComponent> fm = ComponentMapper.getFor(FixtureComponent.class);

    private ImmutableArray<Entity> interactables;
    private Entity playerEntity;

    public DebugSystem(ShapeDrawer shapeDrawer) {
        this.shapeDrawer = shapeDrawer;
    }

    @Override
    public void addedToEngine(Engine engine) {
        interactables = engine.getEntitiesFor(
                Family.all(InteractionComponent.class, HitboxComponent.class).get());
        playerEntity = engine.getEntitiesFor(
                        Family.all(PlayerComponent.class, FixtureComponent.class, AnimationComponent.class)
                                .get())
                .first();
    }

    @Override
    public void update(float deltaTime) {
        var fixture = fm.get(playerEntity).fixture;
        var playerPosition = fixture.getBody().getPosition();
        shapeDrawer.circle(playerPosition.x, playerPosition.y, PlayerConstants.HITBOX_RADIUS, DEBUG_LINE_WIDTH);

        for (var entity : interactables) {
            var hc = hm.get(entity);

            for (var rect : hc.rects) {
                shapeDrawer.rectangle(rect, Color.YELLOW, DEBUG_LINE_WIDTH);
            }
        }
    }
}
