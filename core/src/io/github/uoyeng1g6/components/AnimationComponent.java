package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.IntMap;

public class AnimationComponent implements Component {
    public IntMap<Animation<Sprite>> animations = new IntMap<>();
    public int currentAnimation = -1;
}
