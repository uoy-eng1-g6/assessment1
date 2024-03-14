package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component {
    public float x;
    public float y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PositionComponent() {
        this(0, 0);
    }
}
