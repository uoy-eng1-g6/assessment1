package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {
    public final TextureRegion region;
    public float scale;
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
