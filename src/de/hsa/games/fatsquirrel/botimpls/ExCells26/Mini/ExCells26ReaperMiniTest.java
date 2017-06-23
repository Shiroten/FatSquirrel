package de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.BotCom;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.Cell;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shiroten on 18.06.2017.
 */
public class ExCells26ReaperMiniTest {

    @Test
    public void isInsideTest(){
        BotCom botCom = new BotCom();
        Cell cellOfMini = new Cell(new XY (53,53));
        cellOfMini.setActive(cellOfMini);
        botCom.setCellForNextMini(cellOfMini);
        botCom.grid.put(cellOfMini.getQuadrant(), cellOfMini);

        ExCells26ReaperMini reaper = new ExCells26ReaperMini(botCom);
        assertTrue(cellOfMini.isInside(new XY(53,53),botCom));
        assertTrue(cellOfMini.isInside(new XY(43,63),botCom));
        assertTrue(cellOfMini.isInside(new XY(63,43),botCom));
        assertFalse(cellOfMini.isInside(new XY(22,22),botCom));
        assertFalse(cellOfMini.isInside(new XY(100,100),botCom));
        assertFalse(cellOfMini.isInside(new XY(42,53),botCom));
        assertFalse(cellOfMini.isInside(new XY(42,64),botCom));

    }

    @Test
    public void unRechableTest(){
        BotCom botCom = new BotCom();
        botCom.setCellForNextMini(new Cell(new XY(100,100)));
        ExCells26ReaperMini mini = new ExCells26ReaperMini(botCom);

        mini.unReachableGoodies.add(new XY (10,10));

        assertTrue(mini.unReachableGoodies.contains(new XY (10,10)));
        assertFalse(mini.unReachableGoodies.contains(new XY (10,11)));

        mini.unReachableGoodies.clear();
        assertFalse(mini.unReachableGoodies.contains(new XY(10,10)));

    }




}