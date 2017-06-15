package de.hsa.games.fatsquirrel.botimpls.ExCells26;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.BotCom;

public class ExCells26Master implements BotController {

    private BotCom botCom;
    private boolean firstCall = true;
    public ExCells26Master(BotCom botCom) {
        this.botCom = botCom;
    }

    @Override
    public void nextStep(ControllerContext view) {
        if (firstCall) {
            botCom.init();
            botCom.setStartPositionOfMaster(view.locate());
            firstCall = false;
        }

        botCom.checkAttendance(view.getRemainingSteps());
        //Todo: Spawn or go
    }
}
