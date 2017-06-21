package de.hsa.games.fatsquirrel.botimpls.GoodBeastChaser;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.SpawnException;


public class GoodBeastChaserMaster implements BotController {
    private int energyCutoff = 2000;
    private XY lastPosition = XY.ZERO_ZERO;
    private XY maxSize = XY.ZERO_ZERO;

    @Override
    public void nextStep(ControllerContext view) {
        maxSize = view.getViewLowerRight();

        XY toMove = GoodBeastChaserHelper.toMove(view, lastPosition, maxSize);

        try {
            if (view.getEnergy() > energyCutoff) {
                XY toSpawnDirection = GoodBeastChaserHelper.dodgeMove(view, toMove.times(-1), GoodBeastChaserHelper.freeFieldMode.spawnmini);
                if (GoodBeastChaserHelper.freeField(view, view.locate().plus(toSpawnDirection), GoodBeastChaserHelper.freeFieldMode.spawnmini)) {
                    view.spawnMiniBot(toSpawnDirection, 1000);
                    energyCutoff = energyCutoff + 1200;
                    return;
                }
            } else {
                toMove = GoodBeastChaserHelper.dodgeMove(view, toMove, GoodBeastChaserHelper.freeFieldMode.master);
                lastPosition = view.locate().plus(toMove);
                view.move(toMove);

            }
        } catch (SpawnException e) {
            e.printStackTrace();
        }

    }


}

