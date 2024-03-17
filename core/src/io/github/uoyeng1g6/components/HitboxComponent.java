package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import java.util.Arrays;

public class HitboxComponent implements Component {

    public final boolean collidable;
    public final Rectangle[] rects;
    public final Rectangle region;

    public HitboxComponent(boolean collidable, Rectangle... rects) {
        this.collidable = collidable;
        this.rects = rects;

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
