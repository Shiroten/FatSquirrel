package de.hsa.games.fatsquirrel.botimpls.GoodBeastChaser;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;

public class GoodBeastChaserMini implements BotController {
    private XY lastPosition = XY.ZERO_ZERO;
    private XY maxSize = XY.ZERO_ZERO;

    @Override
    public void nextStep(ControllerContext view) {
        if (maxSize.getX() < view.getViewUpperRight().getX())
            maxSize = new XY (view.getViewUpperRight().getX(),maxSize.getY());

        if (maxSize.getY() < view.getViewLowerLeft().getY())
            maxSize = new XY (maxSize.getX(), view.getViewLowerLeft().getY());


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
            toMove = GoodBeastChaserHelper.dodgeMove(view, toMove, GoodBeastChaserHelper.freeFieldMode.mini);
            lastPosition = view.locate().plus(toMove);
            view.move(toMove);
        }

        toMove = GoodBeastChaserHelper.toMove(view, lastPosition, maxSize);
        toMove = GoodBeastChaserHelper.dodgeMove(view, toMove, GoodBeastChaserHelper.freeFieldMode.master);
        lastPosition = view.locate().plus(toMove);
        view.move(toMove);



    }


}
