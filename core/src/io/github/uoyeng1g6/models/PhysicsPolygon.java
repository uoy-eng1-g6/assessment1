package io.github.uoyeng1g6.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class PhysicsPolygon {
    private String name;
    private BodyDef.BodyType type;
    private Vector2 position;
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
