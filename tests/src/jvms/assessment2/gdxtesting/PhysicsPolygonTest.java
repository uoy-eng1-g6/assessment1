package jvms.assessment2.gdxtesting;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.github.uoyeng1g6.models.PhysicsPolygon;

import com.badlogic.ashley.core.EntitySystem;

import org.junit.Test;

import static org.junit.Assert.*;

public class PhysicsPolygonTest {

    @Test
    public void testPhysicsPolygon (){
        BodyDef body = new BodyDef();
        body.type = BodyDef.BodyType.StaticBody;
        Vector2[] vertices = {new Vector2(0, 0), new Vector2(1, 0)};
        PhysicsPolygon p = new PhysicsPolygon("name", body.type, new Vector2(0, 0), vertices);

        assertEquals(p.getName(), "name");
        assertEquals(p.getType(), BodyDef.BodyType.StaticBody);
        assertEquals(p.getPosition(), new Vector2(0, 0));
        assertArrayEquals(p.getVertices(), vertices);
    }
}
