package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.components.TextureComponent;

public class RenderingSystem extends IteratingSystem {
    private final SpriteBatch batch;

    private final ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public RenderingSystem(SpriteBatch batch) {
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
