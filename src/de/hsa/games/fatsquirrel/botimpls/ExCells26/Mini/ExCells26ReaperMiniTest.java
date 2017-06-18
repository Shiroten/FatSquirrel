package de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.BotCom;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.Cell;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.GoodPlant;
import de.hsa.games.fatsquirrel.core.entity.character.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MiniSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MiniSquirrelBot;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shiroten on 18.06.2017.
 */
public class ExCells26ReaperMiniTest {

    @Test
    public void isInsideTest(){
        BotCom botCom = new BotCom();
        Cell cellOfMini = new Cell(new XY (11,11));
        cellOfMini.setActive(cellOfMini);
        botCom.grid.put(cellOfMini.getQuadrant(), cellOfMini);

        ExCells26ReaperMini reaper = new ExCells26ReaperMini(botCom);
        assertTrue(reaper.isInside(new XY(11,11)));
        assertTrue(reaper.isInside(new XY(1,1)));
        assertTrue(reaper.isInside(new XY(21,21)));
        assertFalse(reaper.isInside(new XY(22,22)));
        assertFalse(reaper.isInside(new XY(100,100)));
    }

    @Test
    public  void goodTargetTest(){
        Board board = new Board("wettbewerb.props");
        GoodPlant gp = new GoodPlant(0,new XY (0,0));
        HandOperatedMasterSquirrel handOperatedMasterSquirrel = new HandOperatedMasterSquirrel(4, new XY(2,2));
        MiniSquirrelBot miniSquirrel = new MiniSquirrelBot(3, new XY(1,1), 100, handOperatedMasterSquirrel );
        board.add(gp);
        board.add(handOperatedMasterSquirrel);
        board.add(miniSquirrel);

        FlattenedBoard fb = board.flatten();

        BotCom botCom = new BotCom();
        Cell cellOfMini = new Cell(new XY (11,11));
        cellOfMini.setActive(cellOfMini);
        botCom.grid.put(cellOfMini.getQuadrant(), cellOfMini);
        ExCells26ReaperMini reaper = new ExCells26ReaperMini(botCom);

        MiniSquirrelBot.ControllerContextImpl cc = new MiniSquirrelBot.ControllerContextImpl(fb,miniSquirrel);

        try {
            XY toMove = reaper.getGoodTarget(cc);
            System.out.println(toMove);
        } catch (NoGoodTargetException e) {
            e.printStackTrace();
        }
    }


}