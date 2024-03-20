package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Component that enables an entity to have a static texture.
 */
public class TextureComponent implements Component {
    /**
     * The texture region to use for the entity's texture.
     */
    public final TextureRegion region;
    /**
     * The scale multiplier to use to draw the texture.
     */
    public float scale;
    /**
     * Whether the texture is currently visible.
     */
    public boolean visible = false;

    public TextureComponent(TextureRegion region, float scale) {
        this.region = region;
        this.scale = scale;
    }

    public TextureComponent show() {
        this.visible = true;
        return this;
    }

    public TextureComponent hide() {
        this.visible = false;
        return this;
    }
}
