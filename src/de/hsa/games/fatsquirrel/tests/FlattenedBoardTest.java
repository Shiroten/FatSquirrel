package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser.GoodBeastChaserFactory;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.character.GoodBeast;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot;
import org.junit.Before;

import static org.mockito.Mockito.*;

/**
 * Created by tillm on 17.05.2017.
 */
public class FlattenedBoardTest {

    private FlattenedBoard mockedfBoard = mock(FlattenedBoard.class);
    private GoodBeast mockedBeast = new GoodBeast(1, new XY(2, 3));
    private MasterSquirrelBot mockedMaster = new MasterSquirrelBot(2, new XY(3,3), new GoodBeastChaserFactory());

    @Before
    public void setup(){
        Board board = new Board();
        board.addEntity(EntityType.WALL, new XY(2,2));
        board.add(mockedBeast);
        board.add(mockedMaster);
    }

    @org.junit.Test
    public void getEntityType() throws Exception {
        when(mockedfBoard.getEntityType(new XY(10, 10))).thenReturn(EntityType.GOODBEAST);
        System.out.println(mockedfBoard.getEntityType(new XY(10, 10)));
    }

    @org.junit.Test
    public void getEntity() throws Exception {
    }

    @org.junit.Test
    public void tryMoveMasterSquirrel() throws Exception {

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