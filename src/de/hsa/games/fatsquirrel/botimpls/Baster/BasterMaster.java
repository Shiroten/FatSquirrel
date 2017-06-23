package de.hsa.games.fatsquirrel.botimpls.Baster;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.SpawnException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BasterMaster implements BotController {
    private final BasterSupport basterSupport = new BasterSupport();

    @Override
    public void nextStep(ControllerContext view) {
        //Default random move
        if(view.getEnergy() > 500){
            try {
                view.spawnMiniBot(XY.RIGHT, 250);
            } catch (SpawnException e){
                Logger logger = Logger.getLogger(Launcher.class.getName());
                logger.log(Level.FINE, e.getMessage());
            }
        }
        view.move(basterSupport.preferredDirection(view));
    }

}
