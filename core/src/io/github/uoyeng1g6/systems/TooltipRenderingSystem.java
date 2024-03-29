package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import io.github.uoyeng1g6.components.FixtureComponent;
import io.github.uoyeng1g6.components.HitboxComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.TooltipComponent;
import io.github.uoyeng1g6.constants.PlayerConstants;
import io.github.uoyeng1g6.models.GameState;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * System that handles rendering an appropriate tooltip while the player is standing within the
 * hitbox for the tooltip-enabled entity.
 */
public class TooltipRenderingSystem extends EntitySystem {
    /**
     * The font to use to write on the tooltip.
     */
    private final BitmapFont font;
    /**
     * The shapedrawer to use to draw the tooltip.
     */
    private final ShapeDrawer shapeDrawer;
    /**
     * The sprite batch to use to draw the tooltip.
     */
    private final SpriteBatch batch;
    /**
     * The game state;
     */
    private final GameState gameState;

    private final ComponentMapper<HitboxComponent> hm = ComponentMapper.getFor(HitboxComponent.class);
    private final ComponentMapper<TooltipComponent> tm = ComponentMapper.getFor(TooltipComponent.class);
    private final ComponentMapper<FixtureComponent> fm = ComponentMapper.getFor(FixtureComponent.class);

    private Entity playerEntity;
    private ImmutableArray<Entity> tooltipEnabled;

    public TooltipRenderingSystem(BitmapFont font, ShapeDrawer shapeDrawer, SpriteBatch batch, GameState gameState) {
        this.font = font;
        this.shapeDrawer = shapeDrawer;
        this.batch = batch;
        this.gameState = gameState;
    }

    @Override
    public void addedToEngine(Engine engine) {
        playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class, FixtureComponent.class)
                        .get())
                .first();
        tooltipEnabled = engine.getEntitiesFor(
                Family.all(HitboxComponent.class, TooltipComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        if (gameState.interactionOverlay != null) {
            // We don't need to render interaction overlays if the player is currently doing an interaction
            return;
        }

        var fixture = fm.get(playerEntity).fixture;
        var playerHitbox = new Circle(fixture.getBody().getPosition(), PlayerConstants.HITBOX_RADIUS);

        for (var entity : tooltipEnabled) {
            var hc = hm.get(entity);
            if (!Intersector.overlaps(playerHitbox, hc.region)) {
                continue;
            }

            var tc = tm.get(entity);

            var drawX = hc.region.x;
            var drawY = hc.region.y;

            drawX -= (tc.tooltip.width / 2) - (hc.region.width / 2); // Center text with hitbox
            drawY += tc.tooltip.height + hc.region.height + 0.8f; // Move above hitbox and add padding

            shapeDrawer.filledRectangle(
                    drawX - 0.1f,
                    drawY - tc.tooltip.height - 0.1f,
                    tc.tooltip.width + 0.2f,
                    tc.tooltip.height + 0.2f,
                    Color.LIGHT_GRAY);
            font.draw(batch, tc.tooltip, drawX, drawY);
        }
    }
}
