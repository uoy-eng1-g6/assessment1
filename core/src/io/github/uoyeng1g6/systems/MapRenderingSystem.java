package io.github.uoyeng1g6.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapRenderingSystem extends EntitySystem {
    private final OrthographicCamera camera;

    private final OrthogonalTiledMapRenderer renderer;

    public MapRenderingSystem(TiledMap tiledMap, OrthographicCamera camera) {
        this.camera = camera;
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap, 1/32f);

        System.out.println(Gdx.graphics.getWidth());
        System.out.println(Gdx.graphics.getHeight());
    }

    @Override
    public void update(float deltaTime) {
        renderer.setView(camera);
        renderer.render();
    }
}
