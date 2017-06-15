package de.hsa.games.fatsquirrel.botimpls.ExCells26;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;

/**
 * Created by Shiroten on 15.06.2017.
 */
public class ExCells26ReaperMini implements BotController{

    private BotCom botCom;
    private Cell myCell;

    public ExCells26ReaperMini(BotCom botCom){
        this.botCom = botCom;
        myCell = botCom.addMini(this);
    }

    @Override
    public void nextStep(ControllerContext view) {
        //Todo: implementation of hunting inside the cell
    }
}
