package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Component attaching a box2d fixture to an entity.
 */
public class FixtureComponent implements Component {
    /**
     * The box2d fixture for the entity.
     */
    public final Fixture fixture;

    public FixtureComponent(Fixture fixture) {
        this.fixture = fixture;
    }
}
