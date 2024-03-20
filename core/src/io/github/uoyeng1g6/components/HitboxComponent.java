package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import java.util.Arrays;

/**
 * Component allowing an entity to have one or more hitboxes. Intended for use with interactables.
 */
public class HitboxComponent implements Component {
    /**
     * The rectangles making up the entity's hitbox.
     */
    public final Rectangle[] rects;
    /**
     * The rectangular region enclosing all the entity's hitbox rectangles. Reduces
     * the number of checks that need to be run each frame - all the hitboxes only need
     * to be iterated through if the object causing the collision is within this region.
     */
    public final Rectangle region;

    public HitboxComponent(Rectangle... rects) {
        this.rects = rects;

        // Calculate the vertices of the full hitbox enclosing region
        var left = Arrays.stream(rects).map(r -> r.x).min(Float::compareTo).get();
        var right = Arrays.stream(rects)
                .map(r -> r.x + r.width)
                .max(Float::compareTo)
                .get();
        var top = Arrays.stream(rects)
                .map(r -> r.y + r.height)
                .max(Float::compareTo)
                .get();
        var bottom = Arrays.stream(rects).map(r -> r.y).min(Float::compareTo).get();

        this.region = new Rectangle(left, bottom, right - left, top - bottom);
    }
}
