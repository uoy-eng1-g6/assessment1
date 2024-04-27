package jvms.assessment2.gdxtesting;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.github.uoyeng1g6.models.PhysicsPolygon;
import com.badlogic.ashley.core.Entity;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PhysicsPolygonTest {

    @Test
    public void testPhysicsPolygon (){
        BodyDef body = new BodyDef();
        body.type = BodyDef.BodyType.StaticBody;
        Vector2[] vertices = {new Vector2(0, 0), new Vector2(1, 0)};
        PhysicsPolygon p = new PhysicsPolygon("name", body.type, new Vector2(0, 0), vertices);

        assertTrue(true);
    }
}
