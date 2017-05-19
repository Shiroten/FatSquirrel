package de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

/**
 * Created by Shiroten on 19.05.2017.
 */
public class GoodBeastChaserHelper {

    protected static XY goodMove(ControllerContext view, XY directionVector, freeFieldMode ffm) {
        XYsupport.Rotation rotation = XYsupport.Rotation.clockwise;
        int nor = 1;
        boolean stuck = true;

        XY checkPostion = view.locate().plus(directionVector);
        if (freeField(view, checkPostion, ffm)) {
            return directionVector;
        }
        XY newVector;
        while (stuck) {
            newVector = XYsupport.rotate(rotation, directionVector, nor);
            checkPostion = view.locate().plus(newVector);
            if (freeField(view, checkPostion, ffm)) {
                return newVector;
            } else {
                if (rotation == XYsupport.Rotation.clockwise) {
                    rotation = XYsupport.Rotation.anticlockwise;
                } else {
                    rotation = XYsupport.Rotation.clockwise;
                    nor++;
                }
                if (nor > 3)
                    return XYsupport.oppositeVector(directionVector);
            }
        }
        return null;
    }

    protected enum freeFieldMode {
        master,
        mini,
        spawnmini,

    }

    protected static boolean freeField(ControllerContext view, XY location, freeFieldMode ffm) {

        try {
            EntityType et = view.getEntityAt(location);
            switch (ffm) {
                case master:
                case mini:
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
                case spawnmini:
                    switch (et) {
                        case NONE:
                            return true;
                        default:
                            return false;
                    }
            }
        } catch (OutOfViewException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected static XY nearestSearchedEntity(ControllerContext view, EntityType et) {

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

    protected static XY toMove(ControllerContext view, XY lastPosition, XY maxSize) {

        XY toMove = XY.ZERO_ZERO;

        XY nearestPositionOfBB = GoodBeastChaserHelper.nearestSearchedEntity(view, EntityType.BADBEAST);
        XY nearestPositionOfBP = GoodBeastChaserHelper.nearestSearchedEntity(view, EntityType.GOODPLANT);
        XY nearestPositionOfGB = GoodBeastChaserHelper.nearestSearchedEntity(view, EntityType.GOODBEAST);
        XY nearestPositionOfPositive = nearestPositionOfGB.distanceFrom(view.locate()) <
                nearestPositionOfBP.distanceFrom(view.locate())
                ? nearestPositionOfGB : nearestPositionOfBP;

        if (view.locate().distanceFrom(nearestPositionOfBB) < 4) {
            if (view.locate().distanceFrom(nearestPositionOfPositive) > view.locate().distanceFrom(nearestPositionOfBB)) {
                toMove = XYsupport.normalizedVector(view.locate().minus(nearestPositionOfBB));
            }
        } else if (view.locate().distanceFrom(nearestPositionOfPositive) < 16) {
            toMove = XYsupport.normalizedVector(nearestPositionOfPositive.minus(view.locate()));
        } else {
            if (lastPosition != view.locate()) {
                toMove = XYsupport.normalizedVector(new XY(maxSize.getX() / 2, maxSize.getY() / 2).minus(view.locate()));
            } else {
                toMove = XY.RIGHT_DOWN;
            }
        }
        toMove = GoodBeastChaserHelper.goodMove(view, toMove, GoodBeastChaserHelper.freeFieldMode.master);
        return toMove;
    }
}
