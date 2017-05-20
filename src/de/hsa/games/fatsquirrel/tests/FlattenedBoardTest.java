package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.EntitySet;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.GoodPlant;
import de.hsa.games.fatsquirrel.core.entity.character.GoodBeast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FlattenedBoardTest {
    EntitySet set = new EntitySet(new XY(40, 30));
    BoardConfig config = new BoardConfig();
    Board board = new Board(set, config);
    FlattenedBoard flat;

    private FlattenedBoard mockedfBoard = mock(FlattenedBoard.class);
    private GoodBeast mockedGoodBeast = mock(GoodBeast.class);

    @org.junit.Test
    public void setUp() {
        board.add(new GoodPlant(0, new XY(20, 20)));
        when(mockedGoodBeast.getCoordinate()).thenReturn(new XY(10, 15));
        when(mockedGoodBeast.getEntityType()).thenReturn(EntityType.GOODBEAST);
        when(mockedGoodBeast.getId()).thenReturn(100);
        board.add(mockedGoodBeast);
        flat = board.flatten();
    }

    @org.junit.Test
    public void getEntityType() throws Exception {
        setUp();
        assertEquals(EntityType.GOODPLANT, flat.getEntityType(new XY(20, 20)));
        assertEquals(EntityType.GOODBEAST, flat.getEntityType(new XY(10, 15)));
    }

    @org.junit.Test
    public void getEntity() throws Exception {
        setUp();
        Entity test = new GoodBeast(100, new XY(10, 15));
        assertEquals(test, flat.getEntity(new XY(10, 15)));
    }

    @org.junit.Test
    public void tryMove() throws Exception {
    }

    @org.junit.Test
    public void tryMove1() throws Exception {
    }

    @org.junit.Test
    public void tryMove2() throws Exception {
    }

    @org.junit.Test
    public void tryMove3() throws Exception {
    }

    @org.junit.Test
    public void spawnMiniSquirrel() throws Exception {
    }

    @org.junit.Test
    public void implode() throws Exception {
    }

    @org.junit.Test
    public void killEntity() throws Exception {
        setUp();
        Entity toKill = flat.getEntity(new XY(20, 20));
        flat.killEntity(toKill);
        assertTrue(flat.getEntity(new XY(20, 20)) == null);
    }

    @org.junit.Test
    public void killAndReplace() throws Exception {
        setUp();
        int counter = 0;
        Entity toKill = flat.getEntity(new XY(10, 15));

        for (int k = 0; k < 250000; k++) {
            flat.killAndReplace(toKill);
            for (int j = 0; j < flat.getSize().getY(); j++) {
                for (int i = 0; i < flat.getSize().getX(); i++) {
                    if (flat.getEntity(new XY(i, j)) == null)
                        continue;
                    else if (toKill.getEntityType() == flat.getEntityType(new XY(i, j))) {
                        toKill = flat.getEntity(new XY(i, j));
                        counter++;
                    }
                }
            }
        }
        System.out.println(counter);
        assertTrue(counter == 250000);
    }

    @org.junit.Test
    public void nearestPlayerEntity() throws Exception {
    }

}