package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shiroten on 17.06.2017.
 */
public class BotComTest {
    @Test
    public void cellAt() throws Exception {
        BotCom botCom = new BotCom();
        XY position1ToCheck = new XY(106,148); //should be at 6,8
        XY position2ToCheck = new XY(105,147); //should be at 5,7
        XY returnedCellfor1 = new XY(116,158); //Cell for 6,8
        XY returnedCellfor2 = new XY(95,137); //Cell for 5,7

        assertEquals(returnedCellfor1,botCom.cellAt(position1ToCheck));
        assertEquals(returnedCellfor2,botCom.cellAt(position2ToCheck));

    }

}