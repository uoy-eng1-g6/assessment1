package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.models.GameState;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class InteractionOverlayRenderingSystem extends EntitySystem {
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 0.35f);

    private final SpriteBatch batch;
    private final BitmapFont font;
    private final ShapeDrawer shapeDrawer;
    private final GameState gameState;

    private float elapsed = -1;

    public InteractionOverlayRenderingSystem(
            SpriteBatch batch, BitmapFont font, ShapeDrawer shapeDrawer, GameState gameState) {
        this.batch = batch;
        this.font = font;
        this.shapeDrawer = shapeDrawer;
        this.gameState = gameState;
    }

    @Override
    public void update(float deltaTime) {
        if (gameState.interactionOverlay == null) {
            // We don't need to render anything if there is no current interaction
            return;
        }

        if (elapsed == -1) {
            elapsed = 0;
        } else {
            elapsed += deltaTime;
        }

        shapeDrawer.filledRectangle(0, 0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, OVERLAY_COLOR);

        var halfWorldWidth = (float) GameConstants.WORLD_WIDTH / 2;
        var halfWorldHeight = (float) GameConstants.WORLD_HEIGHT / 2;

        var layout = new GlyphLayout(font, gameState.interactionOverlay.text);
        font.draw(batch, layout, halfWorldWidth - (layout.width / 2), halfWorldHeight + (layout.height / 2));

        shapeDrawer.filledRectangle(
                halfWorldWidth - (halfWorldWidth / 2) - 0.5f,
                (halfWorldHeight / 2),
                halfWorldWidth + 1,
                3,
                Color.BLACK);

        var progressBarSize = (elapsed / gameState.interactionOverlay.displayFor) * halfWorldWidth;
        shapeDrawer.filledRectangle(
                halfWorldWidth - (halfWorldWidth / 2), (halfWorldHeight / 2) + 0.5f, progressBarSize, 2, Color.WHITE);

        if (elapsed >= gameState.interactionOverlay.displayFor) {
            gameState.interactionOverlay = null;
            elapsed = -1;
        }
    }
}
