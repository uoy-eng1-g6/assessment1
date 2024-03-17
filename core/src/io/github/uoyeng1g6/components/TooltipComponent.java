package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class TooltipComponent implements Component {
    public GlyphLayout tooltip;

    public TooltipComponent(BitmapFont font, String tooltip) {
        this.tooltip = new GlyphLayout(font, tooltip);
    }
}
