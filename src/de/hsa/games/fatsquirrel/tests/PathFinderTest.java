package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.BotCom;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.GoodPlant;
import de.hsa.games.fatsquirrel.core.entity.Wall;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.PathFinder;
import de.hsa.games.fatsquirrel.core.entity.squirrels.HandOperatedMasterSquirrel;
import org.junit.Test;

/**
 * Created by tillm on 06.06.2017.
 */
public class PathFinderTest {
    @Test
    public void directionTo() throws Exception {
        BotCom botCom = new BotCom();
        PathFinder pathFinder = new PathFinder(botCom);

        Board board = new Board();
        GoodPlant goodPlant = new GoodPlant(1, new XY(3,3));
        Wall wall1 = new Wall(2, new XY(3, 7));
        Wall wall2 = new Wall(4, new XY(2, 7));
        Wall wall3 = new Wall(4, new XY(4, 7));
        Wall wall4 = new Wall(4, new XY(2, 8));
        Wall wall5 = new Wall(4, new XY(4, 8));
        Wall wall6 = new Wall(4, new XY(4, 9));
        Wall wall7 = new Wall(4, new XY(2, 9));
        HandOperatedMasterSquirrel handOperatedMasterSquirrel = new HandOperatedMasterSquirrel(3, new XY(3, 8));

        board.add(goodPlant, wall1, handOperatedMasterSquirrel, wall2, wall3, wall4, wall5, wall6, wall7);

        FlattenedBoard context = board.flatten();

        /*XY to = pathFinder.directionTo(handOperatedMasterSquirrel.getCoordinate(), goodPlant.getCoordinate(), context);
        XY toTwo = new PathFinder().directionTo(new XY(3,3), new XY(2,4), context);
        assertTrue(XY.DOWN.equals(to));
        assertTrue(XY.LEFT_DOWN.equals(toTwo));*/

    }

}