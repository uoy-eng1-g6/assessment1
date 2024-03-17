package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

public class FixtureComponent implements Component {
    public final Fixture fixture;

    public FixtureComponent(Fixture fixture) {
        this.fixture = fixture;
    }
}
