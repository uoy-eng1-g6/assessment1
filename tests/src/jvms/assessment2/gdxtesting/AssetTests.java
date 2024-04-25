package jvms.assessment2.gdxtesting;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testPlayerAssetExists() {
        assertTrue("The asset for the player exists", Gdx.files.internal("../assets/sprites/player.png").exists());

    }

    @Test
    public void testTilemapAssetExists() {
        assertTrue("The asset for the TileMap exists", Gdx.files.internal("../assets/maps/campus-east.tmx").exists());
    }
}
