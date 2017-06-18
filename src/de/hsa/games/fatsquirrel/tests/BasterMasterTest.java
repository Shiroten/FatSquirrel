package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.Baster.BasterFactory;
import de.hsa.games.fatsquirrel.botimpls.Baster.BasterSupport;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.entity.BadPlant;
import de.hsa.games.fatsquirrel.core.entity.GoodPlant;
import de.hsa.games.fatsquirrel.core.entity.character.BadBeast;
import de.hsa.games.fatsquirrel.core.entity.character.GoodBeast;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by tillm on 26.05.2017.
 */
public class BasterMasterTest {
    private MasterSquirrelBot.ControllerContextImpl controllerContext;
    @Before
    public void setup(){
        Board board = new Board();
        MasterSquirrelBot masterSquirrelBot = new MasterSquirrelBot(1, new XY(15, 20), new BasterFactory());
        GoodBeast goodBeastOne = new GoodBeast(2, new XY(15, 19));
        GoodBeast goodBeastTwo = new GoodBeast(3, new XY(17, 17));
        GoodBeast goodBeastThree = new GoodBeast(4, new XY(15, 4));
        GoodPlant goodPlantOne = new GoodPlant(5, new XY(19, 16));

        GoodBeast goodBeastFour = new GoodBeast(6, new XY(17, 23));

        BadBeast badBeast = new BadBeast(7, new XY(18, 18));

        BadPlant badPlant = new BadPlant(8, new XY(10, 24));

        board.add(masterSquirrelBot, goodBeastOne, goodBeastTwo, goodBeastThree, goodPlantOne, goodBeastFour, badBeast, badPlant);

        controllerContext = new MasterSquirrelBot.ControllerContextImpl(board.flatten(), masterSquirrelBot);
    }

    @Test
    public void preferredDirection() throws Exception {
        BasterSupport basterSupport = new BasterSupport();
        int[] result = basterSupport.rateMyView(controllerContext);

        assertEquals(200 + 200 / 3, result[0]);
        assertEquals(200 / 3, result[4]);
        assertEquals(- 1000 / 3, result[2]);
        assertEquals(- 100 / 6, result[6]);
    }

}