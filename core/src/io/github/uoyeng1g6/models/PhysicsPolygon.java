package io.github.uoyeng1g6.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Dataclass model representing a physics polygon. Intended to be loaded from file
 * to reduce the amount of duplication needed to create many polygons in code.
 */
public class PhysicsPolygon {
    /**
     * The name of the polygon.
     */
    private String name;
    /**
     * The type of the polygon. This should always be {@code StaticBody}.
     */
    private BodyDef.BodyType type;
    /**
     * The position of the polygon. This is the bottom left corner and all vertices are positioned
     * relative to this point.
     */
    private Vector2 position;
    /**
     * The vertices of the polygon.
     */
    private Vector2[] vertices;

    public PhysicsPolygon() {}

    public PhysicsPolygon(String name, BodyDef.BodyType type, Vector2 position, Vector2... vertices) {
        this.name = name;
        this.type = type;
        this.position = position;
        this.vertices = vertices;
    }

    public String getName() {
        return name;
    }

    public BodyDef.BodyType getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2[] getVertices() {
        return vertices;
    }
}
