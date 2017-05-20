package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser.GoodBeastChaserFactory;
import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.core.entity.*;
import de.hsa.games.fatsquirrel.core.entity.character.*;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FlattenedBoardTest {
    private EntitySet set = new EntitySet(new XY(40, 30));
    private BoardConfig config = new BoardConfig();
    private Board board = new Board(set, config);
    private FlattenedBoard flat;

    //private FlattenedBoard mockedfBoard = mock(FlattenedBoard.class);
    private GoodBeast mockedGoodBeast = mock(GoodBeast.class);

    @Before
    public void setUp() {
        board.add(new GoodPlant(0, new XY(20, 20)));
        when(mockedGoodBeast.getCoordinate()).thenReturn(new XY(10, 15));
        when(mockedGoodBeast.getEntityType()).thenReturn(EntityType.GOODBEAST);
        when(mockedGoodBeast.getId()).thenReturn(100);
        board.add(mockedGoodBeast);
        flat = board.flatten();
    }
    @org.junit.Test
    public void tearDown(){
        set = new EntitySet(new XY(40, 30));
        config = new BoardConfig();
        board = new Board(set, config);
    }

    @org.junit.Test
    public void getEntityType() throws Exception {
        assertEquals(EntityType.GOODPLANT, flat.getEntityType(new XY(20, 20)));
        assertEquals(EntityType.GOODBEAST, flat.getEntityType(new XY(10, 15)));
    }

    @org.junit.Test
    public void getEntity() throws Exception {
        Entity test = new GoodBeast(100, new XY(10, 15));
        assertEquals(test, flat.getEntity(new XY(10, 15)));
    }

    @org.junit.Test
    public void tryMoves() {
        for (int i = 0; i < 20; i++) {
            try {
                tryMove();
                tearDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @org.junit.Test
    public void tryMove() throws Exception {
        board.add(new GoodBeast(0, new XY(10, 15)));
        MasterSquirrelBot master = new MasterSquirrelBot(105, new XY(0, 0), new GoodBeastChaserFactory());
        MiniSquirrel mini = new MiniSquirrel(101, new XY(10, 10), 20000, master);
        board.add(mini);
        flat = board.flatten();

        int maxTests = 10000;
        int counter = 0;
        XY toMove;

        Entity goodBeast = flat.getEntity(new XY(10, 15));

        for (int k = 0; k < maxTests; k++) {

            while ((goodBeast.getCoordinate().distanceFrom(mini.getCoordinate())) >= 1) {
                toMove = goodBeast.getCoordinate().minus(mini.getCoordinate());
                flat.tryMove(mini, toMove);
            }

            for (int j = 0; j < flat.getSize().getY(); j++) {
                for (int i = 0; i < flat.getSize().getX(); i++) {
                    if (goodBeast.getEntityType() == flat.getEntityType(new XY(i, j))) {
                        goodBeast = flat.getEntity(new XY(i, j));
                        //System.out.println("Test: " + new XY(i, j));
                        //System.out.println("Mini: " + mini.getCoordinate().toString());
                        counter++;
                    }
                }
            }
        }

        int counterEntities = 0;
        for (int j = 0; j < flat.getSize().getY(); j++) {
            for (int i = 0; i < flat.getSize().getX(); i++) {
                if (flat.getEntity(new XY(i, j)) != null)
                    counterEntities++;
            }
        }

        System.out.println(counterEntities);
        System.out.println(counter);
        assertTrue(counter == maxTests);
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
        Entity toKill = flat.getEntity(new XY(20, 20));
        flat.killEntity(toKill);
        assertTrue(flat.getEntity(new XY(20, 20)) == null);
    }

    @org.junit.Test
    public void killAndReplace() throws Exception {
        int counter = 0;
        Entity toKill = flat.getEntity(new XY(10, 15));

        for (int k = 0; k < 250000; k++) {
            flat.killAndReplace(toKill);
            for (int j = 0; j < flat.getSize().getY(); j++) {
                for (int i = 0; i < flat.getSize().getX(); i++) {
                    if (toKill.getEntityType() == flat.getEntityType(new XY(i, j))) {
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