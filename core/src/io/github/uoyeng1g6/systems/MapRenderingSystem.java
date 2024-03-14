package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class MapRenderingSystem extends EntitySystem {
    private final OrthographicCamera camera;

    private final OrthogonalTiledMapRenderer renderer;

    public MapRenderingSystem(TiledMap tiledMap, OrthographicCamera camera) {
        this.camera = camera;

        this.renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 16f);
    }

    @Override
    public void update(float deltaTime) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        renderer.setView(camera);
        renderer.render();
    }
}
