package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.ExCells26Master;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shiroten on 17.06.2017.
 */
public class BotComTest {
    @Test
    public void cellAt() throws Exception {
        BotCom botCom = new BotCom();
        XY position1ToCheck = new XY(106, 148); //should be at 6,8
        XY position2ToCheck = new XY(105, 147); //should be at 5,7
        XY returnedCellfor1 = new XY(116, 158); //Cell for 6,8
        XY returnedCellfor2 = new XY(95, 137); //Cell for 5,7

        assertEquals(returnedCellfor1, botCom.cellAt(position1ToCheck));
        assertEquals(returnedCellfor2, botCom.cellAt(position2ToCheck));
    }

    @Test
    public void getAllCellsTest(){
        BotCom botCom = new BotCom();
        botCom.setFieldLimit(new XY(80, 60));

        ExCells26Master master = new ExCells26Master(botCom);
        botCom.setMaster(master);
        botCom.startPositionOfMaster = new XY(66, 55);

        botCom.getAllCells();
        System.out.println(botCom.getGrid().size());
    }

    @Test
    public void cellCreationTest() {


        BotCom botCom = new BotCom();
        botCom.setFieldLimit(new XY(80, 60));

        ExCells26Master master = new ExCells26Master(botCom);
        botCom.setMaster(master);
        botCom.startPositionOfMaster = new XY(66, 55);
        botCom.init();
        try {
            for (int i = 0; i < 20; i++) {
                botCom.expand();
                if (i % 3 == 0)
                master.setCurrentCell(master.getCurrentCell().getNextCell());
            }
        } catch (NoConnectingNeighbourException noConnectingNeighbourException) {
            noConnectingNeighbourException.printStackTrace();
        }
        Cell startingCell = master.getCurrentCell();
        Cell current = startingCell;
        System.out.println("Starting print");
        while (true) {
            System.out.println(current);
            current = current.getNextCell();
            if (current == startingCell) {
                return;
            }
        }

    }

    @Test
    public void cellToStringTest() {
        Cell c = new Cell(new XY(0, 0));
        Cell c2 = new Cell(new XY(1, 1));
        Cell c3 = new Cell(new XY(2, 2));
        Cell c4 = new Cell(new XY(3, 4));

        c.addNeighbour(c2);
        c.addNeighbour(c3);
        c.addNeighbour(c4);

        System.out.println(c);
    }

    @Test
    public void dynamicCellTest(){
        BotCom botCom = new BotCom();
        botCom.setFieldLimit(new XY(80, 60));
        botCom.calculateCellSize();
        botCom.getAllCells();

        assertEquals(20,botCom.cellDistanceX);
        assertEquals(20, botCom.cellDistanceY);

        assertEquals(12, botCom.grid.size());
    }
}