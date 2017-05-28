package de.hsa.games.fatsquirrel.botimpls.Baster;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;

/**
 * Created by Shiroten on 13.05.2017.
 */
public class BasterMini implements BotController {
    private BasterSupport basterSupport = new BasterSupport();
    @Override
    public void nextStep(ControllerContext view) {
        view.move(basterSupport.preferredDirection(view));
    }
}
