package de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.character.Character;

public class GoodBeastChaserMini implements BotController {
    private XY lastPosition = XY.ZERO_ZERO;
    private XY maxSize = XY.ZERO_ZERO;

    @Override
    public void nextStep(ControllerContext view) {
        if (maxSize.getX() < view.getViewUpperRight().getX())
            maxSize = maxSize.plus(new XY(view.getViewUpperRight().getX(), 0));

        if (maxSize.getY() < view.getViewLowerLeft().getY())
            maxSize = maxSize.plus(new XY(view.getViewLowerLeft().getY(), 0));

        boolean shouldImplode = false;
        int counterForPoints = 0;

        /*for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; i++) {
                try {
                    int x = view.locate().getX() + i;
                    int y = view.locate().getY() + j;

                    if (x > view.getViewUpperRight().getX())
                        x = view.getViewUpperRight().getX();
                    else if (x < view.getViewLowerLeft().getX())
                        x = view.getViewLowerLeft().getX();

                    if (y < view.getViewUpperRight().getY())
                        y = view.getViewUpperRight().getY();
                    else if (y > view.getViewLowerLeft().getY())
                        y = view.getViewLowerLeft().getY();

                    System.out.println(x + " " + y);
                    System.out.println(view.getViewLowerLeft());
                    System.out.println(view.getViewUpperRight());

                    EntityType toCheck = view.getEntityAt(view.locate().plus(new XY(x, y)));
                    if (toCheck == EntityType.GOODBEAST || toCheck == EntityType.GOODPLANT) {
                        counterForPoints++;
                    }
                } catch (OutOfViewException e) {
                    e.printStackTrace();
                }
            }
        }
        if (counterForPoints > 7) {
            shouldImplode = true;
        } else {
            counterForPoints = 0;
        }
        */
        if (shouldImplode)
            view.implode(5);

        XY toMove;

        if (view.getEnergy() > 7500) {
            toMove = view.directionOfMaster();
            toMove = GoodBeastChaserHelper.goodMove(view, toMove, GoodBeastChaserHelper.freeFieldMode.mini);
            view.move(toMove);
        }

        toMove = GoodBeastChaserHelper.toMove(view, lastPosition, maxSize);
        toMove = GoodBeastChaserHelper.goodMove(view, toMove, GoodBeastChaserHelper.freeFieldMode.master);
        view.move(toMove);
        lastPosition = view.locate().plus(toMove);


    }


}
