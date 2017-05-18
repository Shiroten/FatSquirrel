package de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

public class GoodBeastChaserMini implements BotController {
    @Override
    public void nextStep(ControllerContext view) {

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
            toMove = goodMove(view, toMove);
            view.move(toMove);
        } else {

            XY nearestEntityOfGOODPLANT = nearestSearchedEntity(view, EntityType.GOODPLANT);
            XY nearestEntityOfGOODBEAST = nearestSearchedEntity(view, EntityType.GOODBEAST);
            XY nearestEntityOf = nearestEntityOfGOODBEAST.distanceFrom(view.locate()) <
                    nearestEntityOfGOODPLANT.distanceFrom(view.locate())
                    ? nearestEntityOfGOODBEAST : nearestEntityOfGOODPLANT;

            toMove = XYsupport.oppositeVector(XYsupport.normalizedVector(view.locate().minus(nearestEntityOf)));
            toMove = goodMove(view, toMove);
            view.move(toMove);
            return;
        }
    }

    private XY goodMove(ControllerContext view, XY direction) {

        XYsupport.Rotation rotation = XYsupport.Rotation.clockwise;
        int nor = 1;
        boolean stuck = true;

        if (freeField(view, view.locate().plus(direction))) {
            return direction;
        }

        while (stuck) {
            if (freeField(view, view.locate().plus(XYsupport.rotate(rotation, direction, nor)))) {
                return XYsupport.rotate(rotation, direction, nor);
            } else {
                if (rotation == XYsupport.Rotation.clockwise) {
                    rotation = XYsupport.Rotation.anticlockwise;
                } else {
                    rotation = XYsupport.Rotation.clockwise;
                    nor++;
                }
                if (nor > 3)
                    return direction;
            }
        }
        return null;
    }

    private boolean freeField(ControllerContext view, XY location) {

        try {
            EntityType et = view.getEntityAt(location);
            switch (et) {
                case WALL:
                case BADBEAST:
                case BADPLANT:
                    return false;
                case NONE:
                case GOODBEAST:
                case GOODPLANT:
                    return true;
                case MINISQUIRREL:
                case MASTERSQUIRREL:
                    if (view.isMine(location))
                        return true;
                    else
                        return false;

            }

        } catch (OutOfViewException e) {
            e.printStackTrace();
        }
        return false;
    }

    private XY nearestSearchedEntity(ControllerContext view, EntityType et) {

        XY pos = view.locate();
        int minX = view.getViewLowerLeft().getX(), maxX = view.getViewUpperRight().getX();
        int minY = view.getViewUpperRight().getY(), maxY = view.getViewLowerLeft().getY();

        try {
            XY nearestEntity = new XY(100, 100);
            for (int i = minX; i < maxX; i++) {
                for (int j = minY; j < maxY; j++) {
                    if (view.getEntityAt(new XY(i, j)) == et) {
                        double distanceToNew = pos.distanceFrom(new XY(i, j));
                        if (distanceToNew < pos.distanceFrom(nearestEntity)) {
                            nearestEntity = new XY(i, j);
                        }
                    }

                }
            }
            return nearestEntity;
        } catch (OutOfViewException e) {
            e.printStackTrace();

        }
        return null;
    }

    private int normalizeNumber(int i) {
        if (i >= 1)
            return 1;
        else if (i <= -1)
            return -1;
        else
            return 0;
    }
}
