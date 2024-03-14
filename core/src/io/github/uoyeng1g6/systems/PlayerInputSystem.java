package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.VelocityComponent;

public class PlayerInputSystem extends EntitySystem {
    private static final float PLAYER_SPEED = 80;

    private Entity playerEntity;

    private final ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    public PlayerInputSystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
    }

    @Override
    public void update(float deltaTime) {
        var velocityComponent = vm.get(playerEntity);

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            velocityComponent.x -= PLAYER_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            velocityComponent.x += PLAYER_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            velocityComponent.y += PLAYER_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            velocityComponent.y -= PLAYER_SPEED;
        }

        pm.get(playerEntity).isInteracting = Gdx.input.isKeyJustPressed(Input.Keys.E);
    }
}
