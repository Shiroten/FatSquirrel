package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser.GoodBeastChaserFactory;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntitySet;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.GoodPlant;
import de.hsa.games.fatsquirrel.core.entity.character.GoodBeast;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by tillm on 19.05.2017.
 */
public class ControllerContextImplTest {
    private XY size = new XY(180, 80);
    private EntitySet set = new EntitySet(size);
    private BoardConfig config = new BoardConfig(size);
    private Board board = new Board(set, config);
    private FlattenedBoard flat;

    private EntityContext context;
    private XY spawnPositionOfMaster = new XY(80, 40);
    private MasterSquirrelBot master;
    private MasterSquirrelBot.ControllerContextImpl view;
    private GoodBeast mockedGoodBeast;

    private XY positionOfMasterLeftUp = new XY(5, 5);
    private XY positionOfMasterLeftDown = new XY(5, 75);
    private XY positionOfMasterRightDown = new XY(175, 75);
    private XY positionOfMasterRightUp = new XY(175, 5);

    @Before
    public void setUp() {
        board.add(new GoodPlant(0, new XY(20, 20)));

        mockedGoodBeast = mock(GoodBeast.class);
        when(mockedGoodBeast.getCoordinate()).thenReturn(new XY(75, 25));
        when(mockedGoodBeast.getEntityType()).thenReturn(EntityType.GOODBEAST);
        when(mockedGoodBeast.getId()).thenReturn(100);
        board.add(mockedGoodBeast);

        flat = board.flatten();
        context = flat;


        master = new MasterSquirrelBot(105, spawnPositionOfMaster, new GoodBeastChaserFactory());
        master.updateEnergy(100);
        view = new MasterSquirrelBot.ControllerContextImpl(context, master.getCoordinate(), master);
    }

    @Test
    public void getViewLowerLeft() throws Exception {
        master.setCoordinate(positionOfMasterLeftUp);
        view = new MasterSquirrelBot.ControllerContextImpl(context, master.getCoordinate(), master);
        assertEquals(new XY(0, 20), view.getViewLowerLeft());

        master.setCoordinate(positionOfMasterLeftDown);
        view = new MasterSquirrelBot.ControllerContextImpl(context, master.getCoordinate(), master);
        assertEquals(new XY(0, 80), view.getViewLowerLeft());

        master.setCoordinate(positionOfMasterRightDown);
        view = new MasterSquirrelBot.ControllerContextImpl(context, master.getCoordinate(), master);
        assertEquals(new XY(160, 80), view.getViewLowerLeft());

        master.setCoordinate(positionOfMasterRightUp);
        view = new MasterSquirrelBot.ControllerContextImpl(context, master.getCoordinate(), master);
        assertEquals(new XY(160, 20), view.getViewLowerLeft());

    }

    @Test
    public void getViewUpperRight() throws Exception {
        master.setCoordinate(positionOfMasterLeftUp);
        view = new MasterSquirrelBot.ControllerContextImpl(context, master.getCoordinate(), master);
        assertEquals(new XY(20, 0), view.getViewUpperRight());

        master.setCoordinate(positionOfMasterLeftDown);
        view = new MasterSquirrelBot.ControllerContextImpl(context, master.getCoordinate(), master);
        assertEquals(new XY(20, 60), view.getViewUpperRight());

        master.setCoordinate(positionOfMasterRightDown);
        view = new MasterSquirrelBot.ControllerContextImpl(context, master.getCoordinate(), master);
        assertEquals(new XY(180, 60), view.getViewUpperRight());

        master.setCoordinate(positionOfMasterRightUp);
        view = new MasterSquirrelBot.ControllerContextImpl(context, master.getCoordinate(), master);
        assertEquals(new XY(180, 0), view.getViewUpperRight());

    }

    @Test
    public void locate() throws Exception {
        assertEquals(spawnPositionOfMaster, view.locate());
    }

    @Test
    public void isMine() throws Exception {
    }

    @Test
    public void directionOfMaster() throws Exception {
    }

    @Test
    public void getRemainingSteps() throws Exception {
    }

    @Test
    public void getEntityAt() throws Exception {
    }

    @Test
    public void move() throws Exception {
    }

    @Test
    public void spawnMiniBot() throws Exception {
    }

    @Test
    public void getEnergy() throws Exception {
        assertEquals(view.getEnergy(), 1100);

    }

}