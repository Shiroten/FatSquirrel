package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

public abstract class Character extends Entity {
    private XY lastVector = XY.ZERO_ZERO;

    public XY getLastVector() {
        return lastVector;
    }

    public void setLastVector(XY lastVector) {
        this.lastVector = lastVector;
    }

    Character(int energy, int id, XY coordinate) {
        super(energy, id, coordinate);
    }

    Character(int id, XY coordinate) {
        super(id, coordinate);
    }

    Character() {
    }


    public abstract void nextStep(EntityContext context);

    public XY possibleMove(EntityContext context, XY wantedDirection) {
        //TODO: possibleMove implementieren
        return XY.ZERO_ZERO;
    }

    void implode(EntityContext context, int impactRadius) {

        context.implode(this, impactRadius);

    }

    void tryUnStuck(EntityContext context, XY direction, freeFieldMode ffm) {
        XY toMove = direction;
        toMove = goodMove(context, toMove, ffm);

        switch (this.getEntityType()) {
            case MINISQUIRREL:
                context.tryMove((MiniSquirrel) this, toMove);
                break;
            case GOODBEAST:
                context.tryMove((GoodBeast) this, toMove);
                break;
            case BADBEAST:
                context.tryMove((BadBeast) this, toMove);
                break;
        }
    }

    private XY goodMove(EntityContext view, XY directionVector, freeFieldMode ffm) {
        XYsupport.Rotation rotation = XYsupport.Rotation.clockwise;
        int nor = 1;
        boolean stuck = true;
        XY checkPostion = getCoordinate().plus(directionVector);
        if (freeField(view, checkPostion, ffm)) {
            return directionVector;
        }
        XY newVector;
        while (stuck) {
            newVector = XYsupport.rotate(rotation, directionVector, nor);
            checkPostion = getCoordinate().plus(newVector);
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

    enum freeFieldMode {
        master,
        spawnmini,
        goodBeast,
        badBeast,
    }

    private boolean freeField(EntityContext view, XY location, freeFieldMode ffm) {
        EntityType et = view.getEntityType(location);
        switch (ffm) {
            case master:
                switch (et) {
                    case WALL:
                    case BADBEAST:
                    case BADPLANT:
                    case MASTERSQUIRREL:
                        return false;
                    case NONE:
                    case GOODBEAST:
                    case GOODPLANT:
                    case MINISQUIRREL:
                        return true;
                }
                break;
            case spawnmini:
                switch (et) {
                    case NONE:
                        return true;
                    default:
                        return false;
                }
            case goodBeast:
                switch (et) {
                    case NONE:
                        return true;
                    default:
                        return false;
                }

            case badBeast:
                switch (et) {
                    case NONE:
                    case MASTERSQUIRREL:
                    case MINISQUIRREL:
                        return true;
                    default:
                        return false;
                }
        }
        return false;
    }


    boolean gotStuck(XY xy) {
        return this.getCoordinate().getX() == xy.getX() && this.getCoordinate().getY() == xy.getY();
    }
}
