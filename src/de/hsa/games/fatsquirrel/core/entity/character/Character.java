package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

/**
 * The parent class for all entities that can make an action, i.e. have a nextStep()
 * Extends Entity
 */
public abstract class Character extends Entity {
    private XY lastDirection = XY.ZERO_ZERO;
    private final int viewDistance;
    private final int waitDuration;

    Character(int energy, int id, XY coordinate) {
        super(energy, id, coordinate);
        viewDistance = 10;
        waitDuration = 1;
    }

    /*TODO: Überlegen, ob diesen Konstruktor verwendet werden soll. Erheblicher Arbeitsaufwand, da von allen
    Characters die Aufrufe angepasst werden müssen*/
    Character(int energy, int id, XY coordinate, int viewDistance, int waitDuration){
        super(energy, id, coordinate);
        this.viewDistance = viewDistance;
        this.waitDuration = waitDuration;
    }

    enum freeFieldMode {
        master,
        spawnmini,
        goodBeast,
        badBeast,
    }

    /**
     *
     * @return The last direction the character was going
     */
    public XY getLastDirection() {
        return lastDirection;
    }

    /**
     *
     * @param lastDirection the last direction the character was going
     */
    public void setLastDirection(XY lastDirection) {
        this.lastDirection = lastDirection;
    }

    public abstract void nextStep(EntityContext context);

    /*private XY goodMove(EntityContext view, XY directionVector, XY origin, EntityType entityType) {
        XYsupport.Rotation rotation = XYsupport.Rotation.clockwise;
        int nor = 1;
        XY checkPosition = origin.plus(directionVector);
        if (freeField(view, checkPosition, entityType)) {
            return directionVector;
        }
        XY newVector;
        while (true) {
            newVector = XYsupport.rotate(rotation, directionVector, nor);
            checkPosition = origin.plus(newVector);
            if (freeField(view, checkPosition, entityType)) {
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
    }

    private boolean freeField(EntityContext view, XY location, EntityType type) {
        EntityType et = view.getEntityType(location);
        switch (type) {
            case MASTERSQUIRREL:
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
            case MINISQUIRREL:
                switch (et) {
                    case NONE:
                        return true;
                    default:
                        return false;
                }
            case GOODBEAST:
                switch (et) {
                    case NONE:
                        return true;
                    default:
                        return false;
                }

            case BADBEAST:
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
    }*/

}
