package io.github.uoyeng1g6.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.function.BiConsumer;

/**
 * Utility class to reduce the boilerplate required to register a change listener to
 * {@code scene2d.ui} components. Instead of having to subclass the scene2d change listener
 * every time you can instead just use {@link ChangeListener#of(BiConsumer)} and pass an anonymous function.
 */
public class ChangeListener extends com.badlogic.gdx.scenes.scene2d.utils.ChangeListener {
    private final BiConsumer<ChangeEvent, Actor> consumer;

    ChangeListener(BiConsumer<ChangeEvent, Actor> consumer) {
        this.consumer = consumer;
    }

    public static ChangeListener of(BiConsumer<ChangeEvent, Actor> consumer) {
        return new ChangeListener(consumer);
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        this.consumer.accept(event, actor);
    }
}
