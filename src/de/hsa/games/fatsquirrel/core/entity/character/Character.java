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

    //TODO: Dijkstra implementieren und zu XY ändern.
    void findIdealPath(EntityContext context, XY direction, freeFieldMode ffm) {
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
        XY checkPosition = getCoordinate().plus(directionVector);
        if (freeField(view, checkPosition, ffm)) {
            return directionVector;
        }
        XY newVector;
        while (true) {
            newVector = XYsupport.rotate(rotation, directionVector, nor);
            checkPosition = getCoordinate().plus(newVector);
            if (freeField(view, checkPosition, ffm)) {
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

}
