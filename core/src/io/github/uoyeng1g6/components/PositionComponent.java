package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;

/**
 * Component that enables an entity to be positioned on the game screen.
 */
public class PositionComponent implements Component {
    /**
     * The x coordinate.
     */
    public float x;
    /**
     * The y coordinate.
     */
    public float y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
