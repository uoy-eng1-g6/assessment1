package jvms.assessment2.gdxtesting;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.FixtureComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.constants.PlayerConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.systems.PlayerInputSystem;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class PlayerInputSystemTest {

    Engine engine;
    GameState gameState;
    Entity playerEntity;

    @Before
    public void createPlayerEntity() {

        engine = new PooledEngine();
        gameState = new GameState();

        World world = new World(new Vector2(), true);

        var player = new BodyDef();
        player.type = BodyDef.BodyType.DynamicBody;
        player.position.set(PlayerConstants.START_POSITION);
        var playerBody = world.createBody(player);
        playerBody.setUserData(PlayerConstants.HITBOX_RADIUS);
        var playerCircle = new CircleShape();
        playerCircle.setRadius(PlayerConstants.HITBOX_RADIUS);
        var playerFixture = playerBody.createFixture(playerCircle, 1f);
        playerCircle.dispose();

        var playerAnimations = new AnimationComponent(0.05f);

        playerEntity = engine.createEntity().add(new PlayerComponent()).add(playerAnimations).add(new FixtureComponent(playerFixture));
        engine.addEntity(playerEntity);
    }

    @Test
    public void testSuccessfulAddToEngine() {
        engine.addSystem(new PlayerInputSystem(gameState));

        //Test the system has successfully found the player entity.
        assertEquals(playerEntity, engine.getSystem(PlayerInputSystem.class).getPlayerEntity());
    }

    @Test
    public void testVelocity() {
        engine.addSystem(new PlayerInputSystem(gameState));

        engine.update(0.01f);

        //Test if the velocity is 0 when no key is being pressed.
        assertEquals(new Vector2(0, 0), engine.getSystem(PlayerInputSystem.class).getVelocity());
    }

}