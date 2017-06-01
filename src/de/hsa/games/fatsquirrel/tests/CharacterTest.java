package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botimpls.GoodBeastChaserFactory;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntitySet;
import de.hsa.games.fatsquirrel.core.entity.Wall;
import de.hsa.games.fatsquirrel.core.entity.character.*;

import static org.junit.Assert.*;

public class CharacterTest {
    private Board board = new Board();
    private FlattenedBoard flat;

    private GoodBeast gb;
    private BadBeast bb;
    private MasterSquirrel master;
    private XY masterSpawnLocation = new XY(20, 20);
    private MiniSquirrel mini;
    private Wall[] walls;
    private ControllerContext viewMaster;

    public void setUp() {
        walls = new Wall[4];
        for (int i = 0; i < 4; i++) {
            walls[i] = new Wall(0, new XY(20 + i, 19));
            board.add(walls[i]);
        }
        master = new MasterSquirrelBot(101, masterSpawnLocation, new GoodBeastChaserFactory());
        mini = new MiniSquirrelBot(102, new XY(21, 20), 200, master);
        gb = new GoodBeast(201, new XY(22, 20));
        bb = new BadBeast(202, new XY(23, 20));

        board.add(master);
        board.add(mini);
        board.add(gb);
        board.add(bb);

        flat = board.flatten();
    }

    @org.junit.Test
    public void masterSquirrelTimerTest() {
        setUp();
        viewMaster = new MasterSquirrelBot.ControllerContextImpl(flat, master);

        XY toMove = XY.UP;
        viewMaster.move(toMove);

        for (int i = 0; i < 4; i++) {
            assertEquals(3 - i, master.getStunTime());

            toMove = XY.DOWN;
            viewMaster.move(toMove);

            flat = board.flatten();
            master.nextStep(flat);
            viewMaster = new MasterSquirrelBot.ControllerContextImpl(flat, master);
        }


    }

    @org.junit.Test
    public void miniSquirrelTimerTest() {

    }

    @org.junit.Test
    public void goodBeastSquirrelTimerTest() {
        setUp();
        for (int i = 0; i < 20; i++) {
            assertEquals(i % 5, gb.moveCounter);
            gb.nextStep(flat);
            flat = board.flatten();
        }
    }

    @org.junit.Test
    public void badBeastSquirrelTimerTest() {
        setUp();
        for (int i = 0; i < 20; i++) {
            assertEquals(i % 5, bb.moveCounter);
            bb.nextStep(flat);
            flat = board.flatten();
        }

    }

}