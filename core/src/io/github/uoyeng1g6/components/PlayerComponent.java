package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;

/**
 * Component containing information about the player. There should only ever
 * be a single entity that has this component added to it.
 */
public class PlayerComponent implements Component {
    /**
     * Whether the player is currently trying to interact.
     */
    public boolean isInteracting = false;
}
