import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.models.GameState;
import jvms.assessment2.gdxtesting.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class GameStateTest {
    @Test
    public void testAdvanceDay() {
        GameState gameState = new GameState();
        assertEquals(7, gameState.daysRemaining);
        assertEquals(GameConstants.MAX_ENERGY, gameState.energyRemaining);
        assertEquals(GameConstants.MAX_HOURS, gameState.hoursRemaining);
        assertEquals(new ArrayList<GameState.Day>(7), gameState.days);
        gameState.advanceDay();
        assertEquals(6, gameState.daysRemaining);
        assertEquals(GameConstants.MAX_ENERGY, gameState.energyRemaining);
        assertEquals(GameConstants.MAX_HOURS, gameState.hoursRemaining);
        assertNotSame(new ArrayList<GameState.Day>(7), gameState.days);
        assertNotNull(gameState.interactionOverlay);
    }
}
