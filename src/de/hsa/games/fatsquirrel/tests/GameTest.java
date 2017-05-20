package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.Game;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Shiroten on 20.05.2017.
 */
public class GameTest {
    double ticklengthAtStart = 125;
    Game game = new Game();

    @Test
    public void setGameSpeed() throws Exception {
        game.setGameSpeed(ticklengthAtStart);
        assertEquals(ticklengthAtStart, game.getTickLength(), 0.001);

    }

    @Test
    public void addGameSpeed() throws Exception {
        game.setGameSpeed(ticklengthAtStart);
        game.addGameSpeed(3);
        assertTrue(game.getTickLength() > ticklengthAtStart);

        game.setGameSpeed(ticklengthAtStart);
        game.addGameSpeed(-3);
        assertFalse(game.getTickLength() > ticklengthAtStart);
    }

    @Test
    public void getTickLength() throws Exception {
        game.setGameSpeed(ticklengthAtStart);
        assertEquals(ticklengthAtStart, game.getTickLength(), 0.001);
    }

}