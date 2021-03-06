package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.GoodBeastChaserFactory;
import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.core.entity.*;
import de.hsa.games.fatsquirrel.core.entity.squirrels.*;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FlattenedBoardTest {
    private Board board = new Board();
    private FlattenedBoard flat;

    @SuppressWarnings("EmptyMethod")
    @Before
    public void setUp() {
    }

    @org.junit.Test
    public void tearDown() {
        board = new Board();
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
                tearDown();
                tryMoveMini();
                tearDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @org.junit.Test
    public void tryMoveMini() throws Exception {
        board.add(new GoodBeast(0, new XY(10, 15)));
        MasterSquirrelBot master = new MasterSquirrelBot(105, new XY(0, 0), new GoodBeastChaserFactory());
        MiniSquirrelBot mini = new MiniSquirrelBot(101, new XY(10, 10), 20000, master);
        board.add(mini);
        flat = board.flatten();

        int maxTests = 1000;
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
    public void tryMoveGoodBeast() throws Exception {
        GoodBeast goodBeast = new GoodBeast(1, new XY(3,3));
        Wall wall = new Wall(2, new XY(3,2));
        HandOperatedMasterSquirrel handOperatedMasterSquirrel = new HandOperatedMasterSquirrel(4, new XY(4,2));
        MiniSquirrelBot miniSquirrel = new MiniSquirrelBot(3, new XY(4,3), 100, handOperatedMasterSquirrel );

        board.add(goodBeast);
        board.add(wall);
        board.add(handOperatedMasterSquirrel);
        board.add(miniSquirrel);

        flat = board.flatten();

        flat.tryMove(goodBeast, XY.UP);
        assertEquals(new XY(3,3), goodBeast.getCoordinate());

        flat.tryMove(goodBeast, XY.LEFT);
        flat.tryMove(goodBeast, XY.RIGHT);
        assertEquals(new XY(3,3), goodBeast.getCoordinate());

        flat.tryMove(goodBeast, XY.RIGHT_UP);
        assertEquals(flat.getEntityType(new XY(3,3)), EntityType.NONE);

    }

    @org.junit.Test
    public void tryMoveBadBeast() throws Exception {
        BadBeast badBeast = new BadBeast(1, new XY(3,3));
        HandOperatedMasterSquirrel handOperatedMasterSquirrel = new HandOperatedMasterSquirrel(2, new XY(2,3));
        MiniSquirrelBot miniSquirrel = new MiniSquirrelBot(3, new XY(2,2), 100, handOperatedMasterSquirrel);

        board.add(badBeast);
        board.add(handOperatedMasterSquirrel);
        board.add(miniSquirrel);

        flat = board.flatten();

        flat.tryMove(badBeast, XY.LEFT);

        assertEquals(new XY(3,3), badBeast.getCoordinate());
        assertEquals(handOperatedMasterSquirrel.getEnergy(), 850);

        flat.tryMove(badBeast, XY.LEFT_UP);
        assertEquals(flat.getEntityType(new XY(2,2)), EntityType.NONE);
        assertEquals(badBeast.getLives(), 5);
    }

    @org.junit.Test
    public void tryMoveMasterSquirrel() throws Exception {
        BadBeast badBeast = new BadBeast(1, new XY(3,3));
        HandOperatedMasterSquirrel handOperatedMasterSquirrel = new HandOperatedMasterSquirrel(2, new XY(2,3));
        MiniSquirrelBot miniSquirrel = new MiniSquirrelBot(3, new XY(2,2), 100, handOperatedMasterSquirrel);

        board.add(badBeast);
        board.add(handOperatedMasterSquirrel);
        board.add(miniSquirrel);

        flat = board.flatten();

        flat.tryMove(handOperatedMasterSquirrel, XY.RIGHT);
        assertEquals(new XY(2,3), handOperatedMasterSquirrel.getCoordinate());
        assertEquals(850, handOperatedMasterSquirrel.getEnergy());

        flat.tryMove(handOperatedMasterSquirrel, XY.UP);
        assertEquals(flat.getEntity(new XY(2,2)), handOperatedMasterSquirrel);
        assertEquals(950, handOperatedMasterSquirrel.getEnergy());

    }

    @org.junit.Test
    public void spawnMiniSquirrel() throws Exception {
        HandOperatedMasterSquirrel handOperatedMasterSquirrel = new HandOperatedMasterSquirrel(1, new XY(3,3));
        Wall wall = new Wall(2, new XY(3,2));
        board.add(handOperatedMasterSquirrel, wall);
        flat = board.flatten();
        flat.spawnMiniSquirrel(XY.UP, 100, handOperatedMasterSquirrel);
        flat.spawnMiniSquirrel(XY.RIGHT, -100, handOperatedMasterSquirrel);
        flat.spawnMiniSquirrel(XY.DOWN, 100, handOperatedMasterSquirrel);

        int numberOfMiniSquirrels = 0;
        for(Entity e : board.getEntityList()){
            if(e.getEntityType() == EntityType.MINISQUIRREL)
                numberOfMiniSquirrels++;
        }

        assertTrue(numberOfMiniSquirrels == 1);
        assertEquals(900, handOperatedMasterSquirrel.getEnergy());
    }

    @org.junit.Test
    public void implode() throws Exception {
        HandOperatedMasterSquirrel handOperatedMasterSquirrel = new HandOperatedMasterSquirrel(1, new XY(20, 20));
        MiniSquirrelBot miniSquirrel = new MiniSquirrelBot(2, new XY(3,3), 400, handOperatedMasterSquirrel);

        GoodBeast goodBeastClose = new GoodBeast(3, new XY(4,3));
        GoodBeast goodBeastMiddle = new GoodBeast(4, new XY(7, 3));
        GoodBeast goodBeastFar = new GoodBeast(5, new XY(13, 3));
        GoodBeast goodBeastOut = new GoodBeast(6, new XY(13, 4));

        board.add(handOperatedMasterSquirrel, miniSquirrel, goodBeastClose, goodBeastMiddle, goodBeastFar, goodBeastOut);

        flat = board.flatten();

        int impactRadius = 10;
        double impactArea = (impactRadius * impactRadius) * Math.PI;

        double energyLoss = 200 * (miniSquirrel.getEnergy()/impactArea) * (1.0- (4.0/impactRadius));

        flat.implode(miniSquirrel, impactRadius);
        assertTrue(!board.getEntityList().contains(miniSquirrel));
        assertTrue(!board.getEntityList().contains(goodBeastClose));

        assertEquals((200 - energyLoss), goodBeastMiddle.getEnergy(), 0.8);

        assertEquals(200, goodBeastOut.getEnergy());

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
        HandOperatedMasterSquirrel one = new HandOperatedMasterSquirrel(2, new XY(2,2));
        HandOperatedMasterSquirrel two = new HandOperatedMasterSquirrel(2, new XY(30,2));
        board.add(one);
        board.add(two);

        flat = board.flatten();

        assertEquals(one, flat.nearestPlayerEntity(new XY(15,2)));
        assertEquals(two, flat.nearestPlayerEntity(new XY(30, 8)));
    }

    @org.junit.Test
    public void randomWithRange() {
        int randomNumber;
        for (int i = 0; i < 10000; i++) {
            randomNumber = flat.randomWithRange(0, 10);
            assertTrue(randomNumber <= 10 && randomNumber >= 0);
        }
    }

}