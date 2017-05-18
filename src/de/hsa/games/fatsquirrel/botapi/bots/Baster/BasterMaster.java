package de.hsa.games.fatsquirrel.botapi.bots.Baster;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;

public class BasterMaster implements BotController {
    @Override
    public void nextStep(ControllerContext view) {

        //Default random move
        view.move(new XY((int) (Math.random()*3)-1,(int) (Math.random() * 3)-1));

    }
}
