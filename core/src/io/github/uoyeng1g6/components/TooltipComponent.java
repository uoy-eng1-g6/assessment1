package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Component allowing an entity to have static text. Intended for use with in-game tooltips - pop ups
 * that appear when the player is standing in particular locations.
 */
public class TooltipComponent implements Component {
    /**
     * The text to use for the tooltip.
     */
    public GlyphLayout tooltip;

    public TooltipComponent(BitmapFont font, String tooltip) {
        this.tooltip = new GlyphLayout(font, tooltip);
    }
}
