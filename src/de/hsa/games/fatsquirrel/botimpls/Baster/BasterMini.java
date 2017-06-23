package de.hsa.games.fatsquirrel.botimpls.Baster;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;

public class BasterMini implements BotController {
    private final BasterSupport basterSupport = new BasterSupport();
    @Override
    public void nextStep(ControllerContext view) {
        view.move(basterSupport.preferredDirection(view));
    }
}
