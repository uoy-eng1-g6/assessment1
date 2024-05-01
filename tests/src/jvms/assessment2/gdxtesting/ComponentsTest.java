package jvms.assessment2.gdxtesting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.constants.MoveDirection;
import io.github.uoyeng1g6.models.GameState;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class ComponentsTest {

    Engine engine;
    GameState gameState;

    @Before
    public void initialiseEngine() {
        engine = new PooledEngine();
        gameState = new GameState();
    }

    @Test
    public void AnimationComponentTest() {

        TextureAtlas playerTextureAtlas = new TextureAtlas(Gdx.files.internal("sprites/player.txt"));

        float spriteScale = 1f;

        AnimationComponent ac = new AnimationComponent(spriteScale);

        ac.animations.put(
                MoveDirection.STATIONARY,
                new Animation<>(1f, playerTextureAtlas.createSprites("stationary"), Animation.PlayMode.LOOP));
        Entity testEntity = engine.createEntity().add(ac);

        assertEquals("Animation component successfully added to entity and engine", ac, testEntity.getComponent(AnimationComponent.class));
    }

}
