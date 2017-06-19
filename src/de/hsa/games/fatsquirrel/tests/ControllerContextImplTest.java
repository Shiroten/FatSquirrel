package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.botapi.SpawnException;
import de.hsa.games.fatsquirrel.botimpls.GoodBeastChaserFactory;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.*;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot;
import de.hsa.games.fatsquirrel.core.entity.character.MiniSquirrelBot;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tillm on 19.05.2017.
 */
public class ControllerContextImplTest {

    private Board board = new Board("test.props");
    private FlattenedBoard flat;
    private EntityContext context;
    private MasterSquirrelBot.ControllerContextImpl viewMaster;
    private MiniSquirrelBot.ControllerContextImpl viewMini;

    private XY positionOfMasterLeftUp = new XY(5, 5);
    private XY positionOfMasterLeftDown = new XY(5, 75);
    private XY positionOfMasterRightDown = new XY(175, 75);
    private XY positionOfMasterRightUp = new XY(175, 5);
    private XY spawnPositionOfMaster1 = new XY(80, 40);
    private XY spawnPositionOfMaster2 = new XY(81, 40);
    private MasterSquirrelBot master1;
    private MasterSquirrelBot master2;

    private XY spawnPositionOfMini1 = new XY(80, 41);
    private XY spawnPositionOfMini2 = new XY(81, 41);
    private XY spawnPositionOfMini3 = new XY(79, 41);
    private MiniSquirrelBot mini1;
    private MiniSquirrelBot mini2;
    private MiniSquirrelBot mini3;

    @Before
    public void setUp() {
        master1 = new MasterSquirrelBot(101, spawnPositionOfMaster1, new GoodBeastChaserFactory());
        master2 = new MasterSquirrelBot(201, spawnPositionOfMaster2, new GoodBeastChaserFactory());
        master1.updateEnergy(100);

        mini1 = new MiniSquirrelBot(102, spawnPositionOfMini1, 200, master1);
        mini2 = new MiniSquirrelBot(202, spawnPositionOfMini2, 200, master2);
        mini3 = new MiniSquirrelBot(103, spawnPositionOfMini3, 200, master1);

        board.add(new GoodPlant(1001, new XY(20, 20)));
        board.add(new Wall(1002, new XY (79,40)));
        board.add(master1);
        board.add(master2);
        board.add(mini1);
        board.add(mini2);
        board.add(mini3);

        flat = board.flatten();
        context = flat;
    }


    public void tearDown() {
        viewMaster = null;
    }

    @Test
    public void getViewLowerLeft() throws Exception {
        master1.setCoordinate(positionOfMasterLeftUp);
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(new XY(0, 20), viewMaster.getViewLowerLeft());

        master1.setCoordinate(positionOfMasterLeftDown);
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(new XY(0, 80), viewMaster.getViewLowerLeft());

        master1.setCoordinate(positionOfMasterRightDown);
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(new XY(160, 80), viewMaster.getViewLowerLeft());

        master1.setCoordinate(positionOfMasterRightUp);
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(new XY(160, 20), viewMaster.getViewLowerLeft());

    }

    @Test
    public void getViewUpperRight() throws Exception {
        master1.setCoordinate(positionOfMasterLeftUp);
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(new XY(20, 0), viewMaster.getViewUpperRight());

        master1.setCoordinate(positionOfMasterLeftDown);
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(new XY(20, 60), viewMaster.getViewUpperRight());

        master1.setCoordinate(positionOfMasterRightDown);
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(new XY(180, 60), viewMaster.getViewUpperRight());

        master1.setCoordinate(positionOfMasterRightUp);
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(new XY(180, 0), viewMaster.getViewUpperRight());

    }

    @Test
    public void locate() throws Exception {
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(spawnPositionOfMaster1, viewMaster.locate());
    }

    @Test(expected = OutOfViewException.class)
    public void isMine() throws Exception {
        //Master Mini Tests
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertTrue(viewMaster.isMine(spawnPositionOfMini1));

        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertFalse(viewMaster.isMine(spawnPositionOfMini2));

        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master2);
        assertTrue(viewMaster.isMine(spawnPositionOfMini2));

        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context,  master2);
        assertFalse(viewMaster.isMine(spawnPositionOfMini1));

        //Mini Master Test
        viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini1);
        assertTrue(viewMini.isMine(spawnPositionOfMaster1));

        viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini1);
        assertFalse(viewMini.isMine(spawnPositionOfMaster2));

        viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini2);
        assertTrue(viewMini.isMine(spawnPositionOfMaster2));

        viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini2);
        assertFalse(viewMini.isMine(spawnPositionOfMaster1));

        //Mini Mini Test
        //viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini1.getCoordinate(), mini1);
        //assertTrue(viewMini.isMine(spawnPositionOfMini3));

        //viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini1.getCoordinate(), mini2);
        //assertFalse(viewMini.isMine(spawnPositionOfMini3));


        //OutOfViewException Test
        viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini1);
        boolean outOfViewExceptionTest = false;
        try {
            viewMaster.isMine(new XY(20, 20));
        } catch (OutOfViewException oove) {
            outOfViewExceptionTest = true;
        }
        assertTrue(outOfViewExceptionTest);
    }

    @Test
    public void directionOfMaster() throws Exception {
        viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini1);
        assertEquals(XY.UP, viewMini.directionOfMaster());
        viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini2);
        assertEquals(XY.UP, viewMini.directionOfMaster());
        viewMini = new MiniSquirrelBot.ControllerContextImpl(context, mini3);
        assertEquals(XY.RIGHT_UP, viewMini.directionOfMaster());
    }

    @Test
    public void getRemainingSteps() throws Exception {
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(board.getConfig().getGAME_DURATION_AT_START(), viewMaster.getRemainingSteps());
    }

    @Test
    public void getEntityAt() throws Exception {
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(EntityType.MINISQUIRREL, viewMaster.getEntityAt(spawnPositionOfMini1));

        boolean outOfViewExceptionTest = false;
        try {
            viewMaster.getEntityAt(new XY(20, 20));
        } catch (OutOfViewException oove) {
            outOfViewExceptionTest = true;
        }
        assertTrue(outOfViewExceptionTest);
    }

    @Test
    public void move() throws Exception {
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);

        XY directionTest1 = new XY(0, -2);
        viewMaster.move(directionTest1);
        flat = board.flatten();
        context = flat;
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(spawnPositionOfMaster1, viewMaster.locate());

        XY directionTest2 = XY.LEFT;
        viewMaster.move(directionTest2);
        flat = board.flatten();
        context = flat;
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(spawnPositionOfMaster1, viewMaster.locate());

        XY directionTest3 = XY.UP;
        viewMaster.move(directionTest3);
        flat = board.flatten();
        context = flat;
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(spawnPositionOfMaster1.plus(directionTest3), viewMaster.locate());
    }

    @Test
    public void spawnMiniBot() throws Exception {
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        viewMini = new MiniSquirrelBot.ControllerContextImpl(context,  mini1);

        viewMaster.spawnMiniBot(XY.UP, 200);
        viewMaster.spawnMiniBot(XY.LEFT_UP, 200);
        viewMaster.spawnMiniBot(XY.RIGHT_UP, 200);

        //view mit neueren FlattenBoard akutallisieren.
        flat = board.flatten();
        context = flat;
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);

        assertEquals(EntityType.MINISQUIRREL, viewMaster.getEntityAt(master1.getCoordinate().plus(XY.UP)));
        assertTrue(viewMaster.isMine(master1.getCoordinate().plus(XY.LEFT_UP)));
        //assertTrue(viewMini.isMine(master1.getCoordinate().plus(XY.RIGHT_UP)));

        boolean notFreeTest = false;
        try {
            viewMaster.spawnMiniBot(XY.DOWN, 200);
        } catch (SpawnException se) {
            notFreeTest = true;
        }
        assertTrue(notFreeTest);

    }

    @Test
    public void getEnergy() throws Exception {
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(context, master1);
        assertEquals(viewMaster.getEnergy(), 1100);
    }

}