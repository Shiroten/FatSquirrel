package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser.GoodBeastChaserFactory;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntitySet;
import de.hsa.games.fatsquirrel.core.entity.character.*;

import static org.junit.Assert.*;

/**
 * Created by shiroten on 22.05.17.
 */
public class CharacterTest {
    private XY size = new XY(180, 80);
    private EntitySet set = new EntitySet(size);
    private BoardConfig config = new BoardConfig(size);
    private Board board = new Board(set, config);
    private FlattenedBoard flat;
    private EntityContext context;

    private GoodBeast gb;
    private BadBeast bb;
    private MasterSquirrel master;
    private MiniSquirrel mini;

    public void setUp(){
        master = new MasterSquirrelBot(101, new XY (20,20), new GoodBeastChaserFactory());
        mini = new MiniSquirrelBot(102, new XY (20,20), 200 ,master);
        gb = new GoodBeast(201, new XY (21,20));
        bb = new BadBeast(202 , new XY(22, 20));

    }

    @org.junit.Test
    public void masterSquirrelTimerTest(){

    }
    @org.junit.Test
    public void miniSquirrelTimerTest(){

    }
    @org.junit.Test
    public void goodBeastSquirrelTimerTest(){

    }
    @org.junit.Test
    public void badBeastSquirrelTimerTest(){

    }

}