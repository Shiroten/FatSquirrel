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

    @Override
    public void nextStep(ControllerContext view) {
        try {

            XY toMove = XY.UP;
            if (view.getEnergy() > energyCutoff) {
                XY toSpawnDirection = goodMove(view, toMove, freeFieldMode.spawnmini);
                if (freeField(view, view.locate().plus(toSpawnDirection), freeFieldMode.spawnmini)) {
                    view.spawnMiniBot(toSpawnDirection, 1000);
                    energyCutoff = energyCutoff + 1200;
                }
            } else {
                XY nearestEntityOfGOODPLANT = nearestSearchedEntity(view, EntityType.GOODPLANT);
                XY nearestEntityOfGOODBEAST = nearestSearchedEntity(view, EntityType.GOODBEAST);
                XY nearestEntityOf = nearestEntityOfGOODBEAST.distanceFrom(view.locate()) <
                        nearestEntityOfGOODPLANT.distanceFrom(view.locate())
                        ? nearestEntityOfGOODBEAST : nearestEntityOfGOODPLANT;

                toMove = XYsupport.oppositeVector(XYsupport.normalizedVector(view.locate().minus(nearestEntityOf)));
                toMove = goodMove(view, toMove, freeFieldMode.master);
                view.move(toMove);
            }

        } catch (SpawnException e) {
            e.printStackTrace();
        }
    }

    private XY goodMove(ControllerContext view, XY directionVector, freeFieldMode ffm) {
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

    private enum freeFieldMode {
        master,
        spawnmini
    }

    private boolean freeField(ControllerContext view, XY location, freeFieldMode ffm) {

        try {
            EntityType et = view.getEntityAt(location);
            switch (ffm) {
                case master: {
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
                    break;
                }
                case spawnmini: {
                    switch (et) {
                        case NONE:
                            return true;
                        default:
                            return false;
                    }
                }
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

