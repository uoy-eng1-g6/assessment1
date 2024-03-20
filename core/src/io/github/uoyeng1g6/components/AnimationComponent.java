package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.IntMap;

/**
 * Component that enables an entity to have an animated graphic.
 */
public class AnimationComponent implements Component {
    /**
     * The scale multiplier of the drawn sprite.
     */
    public final float spriteScale;
    /**
     * Map of animation ID to animation.
     */
    public IntMap<Animation<Sprite>> animations = new IntMap<>();
    /**
     * The ID of the current animation. Set to {@code -1} to hide the sprite.
     */
    public int currentAnimation = -1;
    /**
     * Elapsed animation time. Used to calculate which frame of the current animation to show.
     */
    public float time = 0;

    public AnimationComponent(float spriteScale) {
        this.spriteScale = spriteScale;
    }
}
