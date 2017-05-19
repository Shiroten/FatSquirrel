package de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.botapi.SpawnException;
import de.hsa.games.fatsquirrel.core.entity.EntityType;


public class GoodBeastChaserMaster implements BotController {
    private int energyCutoff = 2000;
    private XY lastPosition = XY.ZERO_ZERO;
    private XY maxSize = XY.ZERO_ZERO;

    @Override
    public void nextStep(ControllerContext view) {
        if (maxSize.getX() < view.getViewUpperRight().getX())
            maxSize = maxSize.plus(new XY(view.getViewUpperRight().getX(), 0));

        if (maxSize.getY() < view.getViewLowerLeft().getY())
            maxSize = maxSize.plus(new XY(view.getViewLowerLeft().getY(), 0));

        XY toMove = GoodBeastChaserHelper.toMove(view, lastPosition, maxSize);

        try {
            if (view.getEnergy() > energyCutoff) {
                XY toSpawnDirection = GoodBeastChaserHelper.goodMove(view, XYsupport.oppositeVector(toMove), GoodBeastChaserHelper.freeFieldMode.spawnmini);
                if (GoodBeastChaserHelper.freeField(view, view.locate().plus(toSpawnDirection), GoodBeastChaserHelper.freeFieldMode.spawnmini)) {
                    view.spawnMiniBot(toSpawnDirection, 1000);
                    energyCutoff = energyCutoff + 1200;
                    return;
                }
            } else {
                toMove = GoodBeastChaserHelper.goodMove(view, toMove, GoodBeastChaserHelper.freeFieldMode.master);
                view.move(toMove);
                lastPosition = view.locate().plus(toMove);
            }
        } catch (SpawnException e) {
            e.printStackTrace();
        }

    }


}

