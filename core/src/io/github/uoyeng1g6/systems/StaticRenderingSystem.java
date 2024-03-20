package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.components.TextureComponent;

/**
 * System that handles rendering static graphics. For a static graphic to be rendered its entity must
 * have both a {@link TextureComponent} and a {@link PositionComponent}, as well as have
 * {@link TextureComponent#visible} be {@code true}.
 */
public class StaticRenderingSystem extends IteratingSystem {
    private final SpriteBatch batch;

    private final ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public StaticRenderingSystem(SpriteBatch batch) {
        super(Family.all(TextureComponent.class, PositionComponent.class).get());

        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var tc = tm.get(entity);
        if (!tc.visible) {
            return;
        }

        var pc = pm.get(entity);
        batch.draw(
                tc.region, pc.x, pc.y, tc.region.getRegionWidth() * tc.scale, tc.region.getRegionHeight() * tc.scale);
    }
}
