package de.hsa.games.fatsquirrel.botimpls.Baster;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.SpawnException;

public class BasterMaster implements BotController {
    private BasterSupport basterSupport = new BasterSupport();

    @Override
    public void nextStep(ControllerContext view) {
        //Default random move
        if(view.getEnergy() > 500){
            try {
                view.spawnMiniBot(XY.RIGHT, 250);
            } catch (SpawnException e){

            }
        }
        view.move(basterSupport.preferredDirection(view));
    }

}
