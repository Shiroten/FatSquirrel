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
    }

    @org.junit.Test
    public void killAndReplace() throws Exception {
    }

    @org.junit.Test
    public void nearestPlayerEntity() throws Exception {
    }

}