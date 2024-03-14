package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.VelocityComponent;
import io.github.uoyeng1g6.enums.MoveDirection;

public class PlayerInputSystem extends EntitySystem {
    private static final float PLAYER_SPEED = 0.5f;

    private Entity playerEntity;

    private final ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);

    public PlayerInputSystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        playerEntity = engine.getEntitiesFor(
                        Family.all(PlayerComponent.class, VelocityComponent.class, AnimationComponent.class)
                                .get())
                .first();
    }

    @Override
    public void update(float deltaTime) {
        var vc = vm.get(playerEntity);

        boolean left, right, up, down;
        left = right = up = down = false;

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            vc.x = -PLAYER_SPEED;
            left = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            vc.x = PLAYER_SPEED;
            right = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            vc.y = PLAYER_SPEED;
            up = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            vc.y = -PLAYER_SPEED;
            down = true;
        }

        if ((left && right) || (!left && !right)) {
            vc.x = 0;
        }
        if ((up && down) || (!up && !down)) {
            vc.y = 0;
        }

        var ac = am.get(playerEntity);
        if (vc.x == 0 && vc.y == 0) {
            ac.currentAnimation = MoveDirection.STATIONARY;
        } else if (vc.x != 0 && vc.y == 0) {
            ac.currentAnimation = vc.x > 0 ? MoveDirection.RIGHT : MoveDirection.LEFT;
        } else {
            ac.currentAnimation = vc.y > 0 ? MoveDirection.UP : MoveDirection.DOWN;
        }

        pm.get(playerEntity).isInteracting = Gdx.input.isKeyJustPressed(Input.Keys.E);
    }
}
