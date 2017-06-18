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
        botCom.setForNextMini(cellOfMini);
        botCom.grid.put(cellOfMini.getQuadrant(), cellOfMini);

        ExCells26ReaperMini reaper = new ExCells26ReaperMini(botCom);
        assertTrue(reaper.isInside(new XY(53,53)));
        assertTrue(reaper.isInside(new XY(43,63)));
        assertTrue(reaper.isInside(new XY(63,43)));
        assertFalse(reaper.isInside(new XY(22,22)));
        assertFalse(reaper.isInside(new XY(100,100)));
        assertFalse(reaper.isInside(new XY(42,53)));
        assertFalse(reaper.isInside(new XY(42,64)));

    }




}