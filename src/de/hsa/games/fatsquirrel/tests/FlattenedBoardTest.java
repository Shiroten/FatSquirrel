package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import static org.mockito.Mockito.*;

/**
 * Created by tillm on 17.05.2017.
 */
public class FlattenedBoardTest {

    private FlattenedBoard mockedfBoard = mock(FlattenedBoard.class);

    @org.junit.Test
    public void getEntityType() throws Exception {
        when(mockedfBoard.getEntityType(new XY(10, 10))).thenReturn(EntityType.GOODBEAST);
        System.out.println(mockedfBoard.getEntityType(new XY(10, 10)));
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