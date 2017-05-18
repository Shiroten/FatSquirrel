package de.hsa.games.fatsquirrel.botapi.bots.Player;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

public class PlayerMini implements BotController {
    @Override
    public void nextStep(ControllerContext view) {
        try {
            XY nearestEntityOfGOODPLANT = nearestSearchedEntity(view, EntityType.GOODPLANT);
            XY nearestEntityOfGOODBEAST = nearestSearchedEntity(view, EntityType.GOODBEAST);
            XY nearestEntityOf = nearestEntityOfGOODBEAST.distanceFrom(view.locate()) >
                    nearestEntityOfGOODPLANT.distanceFrom(view.locate())
                    ? nearestEntityOfGOODBEAST : nearestEntityOfGOODPLANT;
            XY toMove = XYsupport.oppositeVector(XYsupport.normalizedVector(view.locate().minus(nearestEntityOf)));

            if (view.getEntityAt(view.locate().plus(toMove)) == EntityType.WALL) {
                XY lookAtNewToMove = XYsupport.rotate(XYsupport.Rotation.clockwise, toMove, 1);
                if (view.getEntityAt(view.locate().plus(lookAtNewToMove)) != EntityType.WALL) {
                    XY newToMove = XYsupport.rotate(XYsupport.Rotation.clockwise, toMove, 1);
                    view.move(newToMove);
                    return;
                } else {
                    XY newToMove = XYsupport.rotate(XYsupport.Rotation.anticlockwise, toMove, 1);
                    view.move(newToMove);
                    return;
                }

            } else{
                view.move(toMove);
                return;
            }


        } catch (OutOfViewException e) {
            e.printStackTrace();
        }
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
